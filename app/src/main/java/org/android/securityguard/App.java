package org.android.securityguard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        checkSim();
    }

    /**
     * 检查SIM卡是否发生变化
     */
    public void checkSim(){
        SharedPreferences mSharedPreferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean protecting=mSharedPreferences.getBoolean("protecting", true);
        if(protecting){
            TelephonyManager telephonyManager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            //手机现在的SIM卡串号
            String realSim=telephonyManager.getSimSerialNumber();
            //绑定的SIM卡串号
            String bindSim=mSharedPreferences.getString("sim", "");

            if(bindSim.equals(realSim)){
                Log.i("", "SIM卡未发生变化，还是您的手机");
            }else{
                Log.i("", "SIM卡变化了");
                String safephone=mSharedPreferences.getString("safephone", "");
                if(!TextUtils.isEmpty(safephone)){
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(safephone, null, "您的亲友手机的SIM卡已经被更换! ", null, null);
                }
            }
        }
    }
}
