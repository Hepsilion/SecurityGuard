package org.android.securityguard.advance.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Hepsilion on 2017/1/5.
 */

/**
 * 短信的工具类，提供短信备份的API
 */
public class SMSBackupUtils {
    private boolean flag=true;

    /**
     * 备份短信
     * @param context
     * @param callback
     * @return
     */
    public boolean backupSMS(Context context, BackupStatusCallback callback) throws IOException {
        XmlSerializer serializer= Xml.newSerializer();
        File sdDir= Environment.getExternalStorageDirectory();
        long freesize=sdDir.getFreeSpace();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && freesize>1024*1024){
            File file=new File(Environment.getExternalStorageDirectory(), "backup.xml");
            FileOutputStream fos=new FileOutputStream(file);
            serializer.setOutput(fos, "utf-8");     //初始化xml文件的序列化器
            serializer.startDocument("utf-8", true);//写xml文件的头

            //写根节点
            ContentResolver resolver=context.getContentResolver();
            Uri uri= Uri.parse("content://sms/");
            Cursor cursor=resolver.query(uri, new String[]{"address", "body", "type", "date"}, null, null, null);

            int size=cursor.getCount();
            callback.beforeSMSBackup(size);

            serializer.startTag(null, "smss");
            serializer.attribute(null, "size", String.valueOf(size));
            int process=0;
            while(cursor.moveToNext() && flag){
                serializer.startTag(null, "sms");

                serializer.startTag(null, "body");
                //可能会有乱码问题需要处理，如果出现乱码会导致备份失败
                try {
                    String bodyencrypt= Cryptor.encrypt("123", cursor.getString(1));
                    serializer.text(bodyencrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                    serializer.text("短信读取失败");
                }
                serializer.endTag(null, "body");

                serializer.startTag(null, "address");
                serializer.text(cursor.getString(0));
                serializer.endTag(null, "address");

                serializer.startTag(null, "type");
                serializer.text(cursor.getString(2));
                serializer.endTag(null, "type");

                serializer.startTag(null, "date");
                serializer.text(cursor.getString(3));
                serializer.endTag(null, "date");

                serializer.endTag(null, "sms");

                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //设置进度条对话框的进度
                process++;
                callback.onSMSBackup(process);
            }
            cursor.close();
            serializer.endTag(null, "smss");
            serializer.endDocument();

            fos.flush();
            fos.close();
            return flag;
        }else{
            throw new IllegalStateException("sd卡不存在或空间不足");
        }
    }

    public void setFlag(boolean flag){
        this.flag=flag;
    }

    public interface BackupStatusCallback{
        /**
         * 在短信备份之前调用的方法
         * @param size 总的短信的个数
         */
        public void beforeSMSBackup(int size);

        /**
         * 在sms短信备份过程中调用的方法
         * @param process 当前备份的进度
         */
        public void onSMSBackup(int process);
    }
}