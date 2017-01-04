package org.android.securityguard.process.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Hepsilion on 2017/1/4.
 */
public class AutoKillProcessService extends Service {
    private ScreenLockReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver=new ScreenLockReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver=null;
        super.onDestroy();
    }

    class ScreenLockReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager activityManager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            for(ActivityManager.RunningAppProcessInfo info:activityManager.getRunningAppProcesses()){
                String packageName=info.processName;
                activityManager.killBackgroundProcesses(packageName);
            }
        }
    }
}
