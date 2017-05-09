package org.android.securityguard.advance.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.android.securityguard.advance.EnterPasswordActivity;
import org.android.securityguard.advance.db.dao.AppLockDao;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/6.
 */
public class AppLockService extends Service {
    private boolean flag=false; //是否开启程序锁服务的标志
    private AppLockDao dao;
    private Uri uri= Uri.parse("content://org.android.securityguard.applock");

    private List<String> packageNames;
    private Intent intent;
    private ActivityManager activityManager;
    private List<ActivityManager.RunningTaskInfo> taskInfos;
    private ActivityManager.RunningTaskInfo taskInfo;
    private String packageName;
    private String tempStopProtectPackage;
    private AppLockReceiver receiver;
    private MyObserver observer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println(22222);
        dao=new AppLockDao(AppLockService.this);
        packageNames=dao.findAll();

        observer=new MyObserver(new Handler());
        getContentResolver().registerContentObserver(uri, true, observer);

        receiver=new AppLockReceiver();
        IntentFilter filter=new IntentFilter("org.android.securityguard.applock");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);

        //创建Intent实例，用来打开输入密码界面
        intent=new Intent(AppLockService.this, EnterPasswordActivity.class);

        activityManager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        startAppLockService();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        flag=false;
        unregisterReceiver(receiver);
        receiver=null;
        getContentResolver().unregisterContentObserver(observer);
        observer=null;
        super.onDestroy();
    }

    class AppLockReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if("org.android.securityguard.applock".equals(intent.getAction())){
                tempStopProtectPackage=intent.getStringExtra("packageName");
            }else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
                tempStopProtectPackage=null;
                flag=false; //停止监控程序
            }else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
                if(flag==false){
                    startAppLockService();
                }
            }
        }
    }

    /**
     * 开启子线程，实时监视被打开的程序
     */
    private void startAppLockService(){
        new Thread(){
            @Override
            public void run() {
                flag=true;
                while(flag){
                    //监视任务栈的情况，最近打开的任务栈在集合的最前面
                    taskInfos=activityManager.getRunningTasks(1);
                    taskInfo=taskInfos.get(0);  //最近使用的任务栈
                    packageName=taskInfo.topActivity.getPackageName();System.out.println("--111--");
                    //判断这个包名是否需要被保护
                    if(packageNames.contains(packageName)){
                        //判断当前应用程序是否需要临时停止保护(输入了正确的密码)
                        if(!packageName.equals(tempStopProtectPackage)){
                            //需要保护，弹出一个输入密码的界面
                            intent.putExtra("packageName", packageName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    class MyObserver extends ContentObserver {
        public MyObserver(Handler mHandler){
            super(mHandler);
        }

        @Override
        public void onChange(boolean selfChange) {
            packageNames=dao.findAll();
            super.onChange(selfChange);
        }
    }
}