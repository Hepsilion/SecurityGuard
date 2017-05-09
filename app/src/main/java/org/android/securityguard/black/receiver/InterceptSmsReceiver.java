package org.android.securityguard.black.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.android.securityguard.black.db.dao.BlackNumberDao;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class InterceptSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //黑名单功能是否开启
        SharedPreferences mSharedPreferences=context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumStatus=mSharedPreferences.getBoolean("BlackNumStatus", true);
        if(!BlackNumStatus){//黑名单拦截关闭
            return;
        }

        BlackNumberDao dao=new BlackNumberDao(context);
        Object[] objs= (Object[]) intent.getExtras().get("pdus"); //获取接收到的短信
        for(Object obj:objs){System.out.println(1111111);
            SmsMessage msg=SmsMessage.createFromPdu((byte[]) obj);
            String sender=msg.getOriginatingAddress();
            String body=msg.getMessageBody();
            if(sender.startsWith("+86")){
                sender=sender.substring(3, sender.length());
            }
            int mode=dao.getBlackContactMode(sender); //查询拦截模式
            if(mode==2 || mode==3){System.out.println("getBlackContactMode"+mode);
                abortBroadcast();//需要拦截，拦截广播
            }
        }
    }
}
