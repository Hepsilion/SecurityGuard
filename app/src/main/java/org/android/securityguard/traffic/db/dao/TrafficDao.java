package org.android.securityguard.traffic.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.android.securityguard.traffic.db.TrafficOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hepsilion on 2017/1/4.
 */

public class TrafficDao {
    private TrafficOpenHelper helper;

    public TrafficDao(Context context){
        helper=new TrafficOpenHelper(context);
    }

    /**
     * 获取某一天的流量
     * @param dataString
     * @return
     */
    public long getMobileGPRS(String dataString){
        SQLiteDatabase db=helper.getReadableDatabase();
        long gprs=0;
        Cursor cursor=db.rawQuery("select gprs from traffic where date=?", new String[]{"datetime("+dataString+")"});
        if(cursor.moveToNext()){
            String gprsStr=cursor.getString(0);
            if(!TextUtils.isEmpty(gprsStr)){
                gprs=Long.parseLong(gprsStr);
            }
        }else{
            gprs=-1;
        }
        return gprs;
    }

    /**
     * 添加今天的流量信息
     * @param gprs
     */
    public void insertTodayGPRS(long gprs){
        SQLiteDatabase db=helper.getReadableDatabase();
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dataString=sdf.format(date);
        ContentValues values=new ContentValues();
        values.put("gprs", String.valueOf(gprs));
        values.put("date", "datetime ("+dataString+")");
        db.insert("traffic", null, values);
    }

    /**
     * 修改今天的流量信息
     * @param gprs
     */
    public void updateTodayGPRS(long gprs){
        SQLiteDatabase db=helper.getWritableDatabase();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dataString=sdf.format(date);
        ContentValues values=new ContentValues();
        values.put("gprs", String.valueOf(gprs));
        values.put("date", "datetime ("+dataString+")");
        db.update("traffic", values, "date=?", new String[]{"datetime("+dataString+")"});
    }
}
