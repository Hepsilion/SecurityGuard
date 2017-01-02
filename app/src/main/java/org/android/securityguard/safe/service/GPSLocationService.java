package org.android.securityguard.safe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class GPSLocationService extends Service {
    private LocationManager locationManager;
    private MyListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyListener();

        //查询条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        String name = locationManager.getBestProvider(criteria, true);
        Log.i("", "最好的位置提供者" + name);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(name, 0, 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
        listener=null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyListener implements LocationListener{
        /**
         * 当位置提供者位置发生变化时调用的方法
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            StringBuilder sb=new StringBuilder();
            sb.append("accuracy:"+location.getAccuracy()+"\n");
            sb.append("speed:"+location.getSpeed()+"\n");
            sb.append("longitude:"+location.getLongitude()+"\n");
            sb.append("latitude:"+location.getLatitude()+"\n");
            String result=sb.toString();

            SharedPreferences mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
            String safenumber=mSharedPreferences.getString("safephone", "");

            SmsManager.getDefault().sendTextMessage(safenumber, null, result, null, null);
            stopSelf();
        }

        /**
         * 当位置提供者状态发生变化时调用的方法
         * @param s
         * @param i
         * @param bundle
         */
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        /**
         * 当某个位置提供者可用时调用的方法
         * @param s
         */
        @Override
        public void onProviderEnabled(String s) {

        }

        /**
         * 当某个位置提供者不可用时调用的方法
         * @param s
         */
        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
