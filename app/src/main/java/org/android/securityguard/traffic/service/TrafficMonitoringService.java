package org.android.securityguard.traffic.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.android.securityguard.traffic.db.dao.TrafficDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hepsilion on 2017/1/5.
 */

public class TrafficMonitoringService extends Service{
    private Long mOldRxBytes;
    private Long mOldTxBytes;
    private TrafficDao dao;
    private SharedPreferences mSharedPreferences;
    private long usedFlow;
    private boolean flag=true;

    private Thread mThread=new Thread(){
        @Override
        public void run() {
            while(flag){
                try {
                    Thread.sleep(2000*6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateTodayGPRS();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mOldRxBytes= TrafficStats.getMobileRxBytes();
        mOldTxBytes=TrafficStats.getMobileTxBytes();
        dao=new TrafficDao(TrafficMonitoringService.this);
        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        mThread.start();
    }

    @Override
    public void onDestroy() {
        if(mThread!=null && !mThread.isInterrupted()){
            flag=false;
            mThread.interrupt();
            mThread=null;
        }
        super.onDestroy();
    }

    private void updateTodayGPRS(){
        usedFlow=mSharedPreferences.getLong("", 0);
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.DAY_OF_MONTH==1 && calendar.HOUR_OF_DAY==0 && calendar.MINUTE<1 && calendar.SECOND<30){
            usedFlow=0;
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dataString=sdf.format(date);
        long mobileGPRS=dao.getMobileGPRS(dataString);
        long mobileRxBytes=TrafficStats.getMobileRxBytes();
        long mobileTxBytes=TrafficStats.getMobileTxBytes();

        //新产生的流量
        long newGPRS=(mobileRxBytes+mobileTxBytes)-mOldRxBytes-mOldTxBytes;
        mOldRxBytes=mobileRxBytes;
        mOldTxBytes=mobileTxBytes;

        if(newGPRS<0){//网络切换过
            newGPRS=mobileRxBytes+mobileTxBytes;
        }
        if(mobileGPRS==-1){
            dao.insertTodayGPRS(newGPRS);
        }else{
            if(mobileGPRS<0){
                mobileGPRS=0;
            }
            dao.updateTodayGPRS(mobileGPRS+newGPRS);
        }

        usedFlow=usedFlow+newGPRS;
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putLong("usedFlow", usedFlow);
        editor.commit();
    }
}
