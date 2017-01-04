package org.android.securityguard.process.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/4.
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

    /**
     * 获取手机的总内存大小单位byte
     * @return
     */
    public static long getTotalMem(){
        try {
            StringBuffer sb=new StringBuffer();

            FileInputStream fis=new FileInputStream(new File("/proc/meminfo"));
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String totalInfo=br.readLine();
            for(char c:totalInfo.toCharArray()){
                if(c>='0' && c<='9'){
                    sb.append(c);
                }
            }
            long bytesize=Long.parseLong(sb.toString())*1024;
            return bytesize;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取可用的内存信息
     * @param context
     * @return
     */
    public static long getAvailMem(Context context){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long availMem=memoryInfo.availMem;
        return availMem;
    }

    /**
     * 得到正在运行的进程的数量
     * @param context
     * @return
     */
    public static int getRunningProcessCount(Context context){
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos=activityManager.getRunningAppProcesses();
        return runningAppProcessInfos.size();
    }
}
