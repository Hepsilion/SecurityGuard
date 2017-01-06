package org.android.securityguard.advance.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Hepsilion on 2017/1/6.
 */
public class AppInfo {
    public String packageName; //应用程序包名
    public Drawable icon;      //应用程序图标
    public String appName;     //应用程序名称
    public String apkPath;     //应用程序路径
    public boolean isLock;     //应用程序是否加锁
}
