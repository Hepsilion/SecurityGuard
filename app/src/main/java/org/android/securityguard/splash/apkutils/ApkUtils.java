package org.android.securityguard.splash.apkutils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.android.securityguard.Constants;

import java.io.File;

/**
 * Created by Hepsilion on 2017/1/1.
 */

public class ApkUtils {
    /**
     * 获取版本号
     * @param context
     * @return 版本号
     */
    public static String getVersion(Context context){
        //PackageManager可以获取清单文件中的所有信息
        PackageManager manager=context.getPackageManager();
        try {
            PackageInfo packageInfo=manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 安装新版本
     * @param activity
     */
    public static void installApk(Activity activity){
        Intent intent=new Intent("android.intent.action.VIEW");
        //添加默认分类
        intent.addCategory("android.intent.category.DEFAULT");
        //设置数据和类型
        intent.setDataAndType(Uri.fromFile(new File(Constants.APK_LOCAL_FILE)), "application/vnd.android.package-archive");
        activity.startActivityForResult(intent, 0);
    }
}
