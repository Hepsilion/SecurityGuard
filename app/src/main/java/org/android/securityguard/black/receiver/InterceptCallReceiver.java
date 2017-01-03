package org.android.securityguard.black.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.android.securityguard.black.db.dao.BlackNumberDao;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;


/**
 * Created by Hepsilion on 2017/1/2.
 */

public class InterceptCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mSharedPreferences=context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean BlackNumStatus=mSharedPreferences.getBoolean("BlackNumStatus", true);
        if(!BlackNumStatus){//黑名单拦截关闭
            return;
        }

        BlackNumberDao dao=new BlackNumberDao(context);
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            String mIncomingNumber="";
            TelephonyManager manager= (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            switch (manager.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber=intent.getStringExtra("incoming_number");
                    int blackContactMode=dao.getBlackContactMode(mIncomingNumber);
                    if(blackContactMode==1 || blackContactMode==3){
                        //观察呼叫记录的变化，如果呼叫记录生成了，就把呼叫记录给删除掉
                        Uri uri=Uri.parse("content://call_log/calls");
                        context.getContentResolver().registerContentObserver(uri, true, new CallLogObserver(new Handler(), mIncomingNumber, context));
                        endCall(context);
                    }
                    break;
            }
        }
    }

    /**
     * 清除呼叫记录
     * @param incomingNumber
     * @param context
     */
    public void deleteCallLog(String incomingNumber, Context context){
        ContentResolver resolver=context.getContentResolver();
        Uri uri=Uri.parse("content://call_log/calls");
        Cursor cursor=resolver.query(uri, new String[]{"_id"}, "number=?", new String[]{incomingNumber}, "_id desc limit 1");
        if(cursor.moveToNext()){
            String id=cursor.getString(0);
            resolver.delete(uri, "_id=?", new String[]{id});
        }
    }

    /**
     * 挂断电话，需要复制两个AIDL
     * @param context
     */
    public void endCall(Context context){
        try {
            Class clazz=context.getClassLoader().loadClass("androd.os.ServiceManager");
            Method method=clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder= (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony=ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过内容观察者观察数据库变化
     */
    private class CallLogObserver extends ContentObserver{
        private String incomingNumber;
        private Context context;

        public CallLogObserver(Handler handler, String incomingNumber, Context context){
            super(handler);
            this.incomingNumber=incomingNumber;
            this.context=context;
        }

        /**
         * 观察到数据库内容变化调用的方法
         * @param selfChange
         * @param uri
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.i("CallLogObserver", "呼叫记录数据库的内容变化了.");
            context.getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber, context);
            super.onChange(selfChange, uri);
        }
    }
}
