package org.android.securityguard.virus.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Hepsilion on 2017/1/3.
 */

public class AntiVirusDao {
    /**
     * 检查某个md5是否为病毒
     * @param md5
     * @return 代表扫描安全
     */
    public static String checkVirus(String md5){
        String desc=null;

        SQLiteDatabase sqLiteDatabase=SQLiteDatabase.openDatabase("/data/data/org.android.securityguard/files/antivirus.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=sqLiteDatabase.rawQuery("select desc from datable where md5=?", new String[]{md5});
        if(cursor.moveToNext()){
            desc=cursor.getString(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return desc;
    }
}