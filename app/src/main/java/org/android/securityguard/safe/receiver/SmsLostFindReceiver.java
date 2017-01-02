package org.android.securityguard.safe.receiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import org.android.securityguard.R;
import org.android.securityguard.safe.service.GPSLocationService;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class SmsLostFindReceiver extends BroadcastReceiver {
    private static final String TAG=SmsLostFindReceiver.class.getSimpleName();
    private SharedPreferences mSharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        mSharedPreferences=context.getSharedPreferences("config", Activity.MODE_PRIVATE);
        boolean protecting=mSharedPreferences.getBoolean("protecting", true);
        if(protecting){
            //获取超级管理员
            DevicePolicyManager dpm= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

            //遍历数据库中的短信
            Object[] objs= (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage msg=SmsMessage.createFromPdu((byte[]) obj);
                String sender=msg.getDisplayOriginatingAddress();
                String body=msg.getEmailBody();
                String safephone=mSharedPreferences.getString("safephone", null);
                if(!TextUtils.isEmpty(safephone) && sender.equals(safephone)){
                    if("#*locate*#".equals(body)){
                        Log.i(TAG, "返回位置信息");
                        Intent service=new Intent(context, GPSLocationService.class);
                        context.startService(service);
                        abortBroadcast();
                    }else if("#*alarm*#".equals(body)){
                        Log.i(TAG, "播放报警音乐");
                        MediaPlayer player=MediaPlayer.create(context, R.raw.ylzs);
                        player.setVolume(1.0f, 1.0f);
                        player.start();
                        abortBroadcast();
                    }else if("#*wipedata*#".equals(body)){//
                        Log.i(TAG, "远程清除数据");
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//没有管理员权限，调用时会奔溃
                        abortBroadcast();
                    }else if("#*lockScreen*#".equals(body)){
                        Log.i(TAG, "远程锁屏");
                        dpm.resetPassword("123", 0);
                        dpm.lockNow(); //没有管理员权限，调用时会奔溃
                        abortBroadcast();
                    }
                }
            }
        }
    }
}
