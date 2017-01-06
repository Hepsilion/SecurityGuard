package org.android.securityguard.traffic.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/5.
 */
public class SystemInfoUtils {
    /**
     * 判断一个服务是否处于运行状态
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos=activityManager.getRunningServices(200);
        for(ActivityManager.RunningServiceInfo info:infos){
            String serviceClassName=info.service.getClassName();
            if(className.equals(serviceClassName)){
                return true;
            }
        }
        return false;
    }
}
