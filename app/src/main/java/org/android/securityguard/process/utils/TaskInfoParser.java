package org.android.securityguard.process.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import org.android.securityguard.R;
import org.android.securityguard.process.entity.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/4.
 */

/**
 * 任务信息 & 进程信息的解析器
 */
public class TaskInfoParser {
    /**
     * 获取正在运行的所有进程的信息
     * @param context
     * @return 进程信息的集合
     */
    public static List<TaskInfo> getRunningTaskInfos(Context context){
        List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();

        PackageManager packageManager=context.getPackageManager();

        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos=activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo:processInfos){
            TaskInfo taskInfo=new TaskInfo();

            String packageName=processInfo.processName;
            taskInfo.packageName=packageName; //进程名称

            Debug.MemoryInfo[] memoryInfos=activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});
            long mensize=memoryInfos[0].getTotalPrivateDirty()*1024;
            taskInfo.appMemory=mensize;       //程序占用的内存空间

            try {
                PackageInfo packageInfo=packageManager.getPackageInfo(packageName, 0);
                Drawable icon=packageInfo.applicationInfo.loadIcon(packageManager);
                taskInfo.appIcon=icon;        //程序图标

                String appName=packageInfo.applicationInfo.loadLabel(packageManager).toString();
                taskInfo.appName=appName;     //程序名称

                //是否为用户进程
                if((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags)!=0){
                    taskInfo.isUserTask=false;
                }else{
                    taskInfo.isUserTask=true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                taskInfo.appName=packageName;
                taskInfo.appIcon=context.getResources().getDrawable(R.drawable.ic_default);
            }
            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }
}
