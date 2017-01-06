package org.android.securityguard.advance.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.android.securityguard.advance.db.AppLockOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/6.
 */
public class AppLockDao {
    private Context context;
    private AppLockOpenHelper openHelper;
    private Uri uri= Uri.parse("content://org.android.securityguard.applock");

    public AppLockDao(Context context){
        this.context=context;
        openHelper=new AppLockOpenHelper(context);
    }

    /**
     * 添加一条数据
     * @param packageName
     * @return
     */
    public boolean insert(String packageName){
        SQLiteDatabase sqLiteDatabase=openHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("packageName", packageName);
        long rowId=sqLiteDatabase.insert("applock", null, values);
        if(rowId==-1){//插入不成功
            return false;
        }else{        //插入成功
            context.getContentResolver().notifyChange(uri, null);
            return true;
        }
    }

    /**
     * 删除一条数据
     * @param packageName
     * @return
     */
    public boolean delete(String packageName){
        SQLiteDatabase sqLiteDatabase=openHelper.getWritableDatabase();
        int rowId=sqLiteDatabase.delete("applock", "packageName=?", new String[]{packageName});
        if(rowId==0){
            return false;
        }else{
            context.getContentResolver().notifyChange(uri, null);
            return true;
        }
    }

    /**
     * 查询某个包名是否存在
     * @param packageName
     * @return
     */
    public boolean find(String packageName){
        SQLiteDatabase sqLiteDatabase=openHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query("applock", null, "packageName=?", new String[]{packageName}, null, null, null);
        if(cursor.moveToNext()){
            cursor.close();
            sqLiteDatabase.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }

    /**
     * 查询表中所有的包名
     * @return
     */
    public List<String> findAll(){
        SQLiteDatabase sqLiteDatabase=openHelper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query("applock", null, null, null, null, null, null);
        List<String> packages=new ArrayList<String>();
        while(cursor.moveToNext()){
            String string=cursor.getString(cursor.getColumnIndex("packageName"));
            packages.add(string);
        }
        return packages;
    }
}