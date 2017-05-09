package org.android.securityguard.black.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import org.android.securityguard.black.db.BlackNumberOpenHelper;
import org.android.securityguard.black.entity.BlackContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class BlackNumberDao {
    private BlackNumberOpenHelper blackNumberOpenHelper;

    public BlackNumberDao(Context context){
        super();
        blackNumberOpenHelper=new BlackNumberOpenHelper(context);
    }

    /**
     * 添加数据
     * @param blackContactInfo
     * @return
     */
    public boolean add(BlackContactInfo blackContactInfo){
        SQLiteDatabase db=blackNumberOpenHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        if(blackContactInfo.phoneNumber.startsWith("+86")){
            blackContactInfo.phoneNumber=blackContactInfo.phoneNumber.substring(3, blackContactInfo.phoneNumber.length());
        }
        values.put("number", blackContactInfo.phoneNumber);
        values.put("name", blackContactInfo.contactName);
        values.put("mode", blackContactInfo.mode);

        long rowid=db.insert("blacknumber", null, values);
        if(rowid==-1){//插入不成功
            return false;
        }else{        //插入成功
            return true;
        }
    }

    /**
     * 删除数据
     * @param blackContactInfo
     * @return
     */
    public boolean delete(BlackContactInfo blackContactInfo){
        SQLiteDatabase db=blackNumberOpenHelper.getWritableDatabase();
        int rownumber=db.delete("blacknumber", "number=?", new String[]{blackContactInfo.phoneNumber});
        if(rownumber==0){ //删除数据不成功
            return false;
        }else{            //删除数据成功
            return true;
        }
    }

    /**
     * 分页查询数据库的记录
     * @param pagenumber 第几页，页码从第0页开始
     * @param pagesize 每一个页面的大小
     * @return
     */
    public List<BlackContactInfo> getPageBlackNumber(int pagenumber, int pagesize){
        SQLiteDatabase db=blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select number, mode, name from blacknumber limit ? offset ?", new String[]{String.valueOf(pagesize), String.valueOf(pagesize*pagenumber)});
        List<BlackContactInfo> contacts=new ArrayList<BlackContactInfo>();
        while(cursor.moveToNext()){
            BlackContactInfo contact=new BlackContactInfo();
            contact.phoneNumber=cursor.getString(0);
            contact.mode=cursor.getInt(1);
            contact.contactName=cursor.getString(2);
            contacts.add(contact);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(30);
        return contacts;
    }

    /**
     * 判断号码是否在黑名单中存在
     * @param number
     * @return
     */
    public boolean isBlackNumber(String number){
        SQLiteDatabase db=blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor=db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
        if(cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }else{
            cursor.close();
            db.close();
            return false;
        }
    }

    /**
     * 查询号码在黑名单中的模式
     * @param number
     * @return
     */
    public int getBlackContactMode(String number){
        SQLiteDatabase db=blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor=db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        int mode=0;
        if(cursor.moveToNext()){
            mode=cursor.getInt(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }

    /**
     * 获取数据库中黑名单总条目数
     * @return
     */
    public int getTotalNumber(){
        SQLiteDatabase db=blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count=cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
