package org.android.securityguard.advance.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.android.securityguard.advance.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/6.
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
            AppInfo appInfo=new AppInfo();
            appInfo.packageName=packageInfo.packageName;
            appInfo.icon=packageInfo.applicationInfo.loadIcon(packageManager);
            appInfo.appName=packageInfo.applicationInfo.loadLabel(packageManager).toString();
            appInfo.apkPath=packageInfo.applicationInfo.sourceDir;
            appInfos.add(appInfo);
            appInfo=null;
        }
        return appInfos;
    }
}
