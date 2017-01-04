package org.android.securityguard.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.app.entity.AppInfo;

/**
 * Created by Hepsilion on 2017/1/3.
 */
public class EngineUtils {
    /**
     * 分享应用
     * @param context
     * @param appInfo
     */
    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent=new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件，名称叫: "+appInfo.appName+", 下载路径: https://play.google.com/store/apps/details?id="+appInfo.packageName);
        context.startActivity(intent);
    }

    /**
     * 开启应用程序
     * @param context
     * @param appInfo
     */
    public static void startApplication(Context context, AppInfo appInfo){
        PackageManager packageManager=context.getPackageManager();
        Intent intent=packageManager.getLaunchIntentForPackage(appInfo.packageName);
        if(intent!=null){
            context.startActivity(intent);
        }else{
            Toast.makeText(context, R.string.app_manager_app_no_launch, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启应用设置页面
     * @param context
     * @param appInfo
     */
    public static void setAppDetail(Context context, AppInfo appInfo){
        Intent intent=new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);
    }

    /**
     * 卸载应用
     * @param context
     * @param appInfo
     */
    public static void uninstallApplication(Context context, AppInfo appInfo){
        if(appInfo.isUserApp){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package: "+appInfo.packageName));
            context.startActivity(intent);
        }
//        else{//系统应用需要root权限，利用linux命令删除文件
//            if(!RootTools.isRootAvailable()){
//                Toast.makeText(context, R.string.app_manager_app_uninstall_need_root, Toast.LENGTH_SHORT).show();
//                return;
//            }
//            try{
//                if(RootTools.isAccessGiven()){
//                    Toast.makeText(context, R.string.app_manager_app_uninstall_permission_grant, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                RootTools.sendSheel("mount -o remount, rw /system", 3000);
//                RootTools.sendSheel("rm -r "+appInfo.apkPath, 30000);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}
