package org.android.securityguard.advance.utils;

/**
 * Created by Hepsilion on 2017/1/6.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.android.securityguard.advance.entity.SMSInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 短信还原的工具类
 */
public class SMSRestoreUtils {
    private boolean flag=true;

    public boolean restoreSMS(Activity context, SMSRestoreCallback callback) throws IOException, XmlPullParserException {
        File file=new File(Environment.getExternalStorageDirectory(), "backup.xml");
        if(file.exists()){
            FileInputStream fis=new FileInputStream(file);
            XmlPullParser parser= Xml.newPullParser();
            parser.setInput(fis, "utf-8");
            SMSInfo smsInfo=null;
            int eventType=parser.getEventType();
            Integer max=null;
            int process=0;
            ContentResolver resolver= context.getContentResolver();
            Uri uri= Uri.parse("content://sms/");
            while(eventType!=XmlPullParser.END_DOCUMENT && flag){
                switch (eventType){
                    case XmlPullParser.START_TAG://一个节点的开始
                        if("smss".equals(parser.getName())){
                            String maxStr=parser.getAttributeValue(0);
                            max=new Integer(maxStr);
                            callback.beforeSMSRestore(max);
                        }else if("sms".equals(parser.getName())){
                            smsInfo=new SMSInfo();
                        }else if("body".equals(parser.getName())){
                            try {
                                smsInfo.body=Cryptor.decrypt("123", parser.nextText());
                            } catch (Exception e) {//此条短信还原失败
                                e.printStackTrace();
                                smsInfo.body="短信还原失败";
                            }
                        }else if("address".equals(parser.getName())){
                            smsInfo.address=parser.nextText();
                        }else if("type".equals(parser.getName())){
                            smsInfo.type=parser.nextText();
                        }else if("date".equals(parser.getName())){
                            smsInfo.date=parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG://一个节点的结束
                        if("sms".equals(parser.getName())){
                            ContentValues values=new ContentValues();
                            values.put("address", smsInfo.address);
                            values.put("type", smsInfo.type);
                            values.put("date", smsInfo.date);
                            values.put("body", smsInfo.body);
                            System.out.println("111"+resolver.insert(uri, values).toString());
                            smsInfo=null;
                            process++;
                            callback.onSMSRestore(process);
                            System.out.println("111------------------");
                        }
                        break;
                }
                //得到下一个节点的事件类型
                eventType=parser.next();
            }
            //防止出现在备份未完成的情况下，还原短信
            if(eventType==XmlPullParser.END_DOCUMENT & max!=null){
                if(process<max){
                    callback.onSMSRestore(max);
                }
            }
        }else{
            //如果backup.xml文件不存在，则说明没有备份短信
            UIUtils.showToast(context, "您还没有备份短信!");
        }
        return flag;
    }

    public void setFlag(boolean flag){
        this.flag=flag;
    }

    public interface SMSRestoreCallback{
        /**
         * 在短信还原之前调用的方法
         * @param size 总的短信个数
         */
        public void beforeSMSRestore(int size);

        /**
         * 在短信还原过程中调用的方法
         * @param process 当前的进度
         */
        public void onSMSRestore(int process);
    }
}
