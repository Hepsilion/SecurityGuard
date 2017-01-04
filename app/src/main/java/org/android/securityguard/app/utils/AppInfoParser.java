package org.android.securityguard.app.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import org.android.securityguard.app.entity.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/3.
 */
public class AppInfoParser {
    /**
     * 获取手机里面的所有的应用程序
     * @param context
     * @return
     */
    public static List<AppInfo> getAppInfos(Context context){
        PackageManager packageManager=context.getPackageManager();
        List<PackageInfo> packageInfos=packageManager.getInstalledPackages(0);
        List<AppInfo> appInfos=new ArrayList<AppInfo>();

        for(PackageInfo packageInfo:packageInfos){
            String packageName=packageInfo.packageName;
            Drawable icon=packageInfo.applicationInfo.loadIcon(packageManager);
            String appName=packageInfo.applicationInfo.loadLabel(packageManager).toString();
            String apkPath=packageInfo.applicationInfo.sourceDir;
            Long appSize=new File(apkPath).length();
            boolean isInRoom=false;
            boolean isUserApp=false;

            int flag=packageInfo.applicationInfo.flags;
            if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flag) !=0){
                isInRoom=false; //外部存储
            }else{
                isInRoom=true;  //手机内存
            }
            if((ApplicationInfo.FLAG_SYSTEM & flag) !=0){
                isUserApp=false;//系统应用
            }else{
                isUserApp=true; //用户应用
            }

            AppInfo appInfo=new AppInfo();
            appInfo.packageName=packageName;
            appInfo.icon=icon;
            appInfo.appName=appName;
            appInfo.appSize=appSize;
            appInfo.isInRoom=isInRoom;
            appInfo.isUserApp=isUserApp;

            appInfos.add(appInfo);
            appInfo=null;
        }
        return appInfos;
    }
}
