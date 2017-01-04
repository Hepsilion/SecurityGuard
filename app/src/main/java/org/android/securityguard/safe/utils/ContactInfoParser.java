package org.android.securityguard.safe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.android.securityguard.safe.entity.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class ContactInfoParser {
    public static List<ContactInfo> getSystemContact(Context context){
        List<ContactInfo> contacts=new ArrayList<ContactInfo>();

        ContentResolver resolver=context.getContentResolver();
        Uri uri= Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri=Uri.parse("content://com.android.contacts/data");

        //1. 查询raw_contacts表，把联系人的id取出来
        Cursor cursor=resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while(cursor.moveToNext()){
            String id=cursor.getString(0);
            if(id!=null){
                ContactInfo contact=new ContactInfo();
                contact.id=id;

                //2. 根据联系人的id，查询data表，把这个id的数据取出来
                Cursor dataCursor=resolver.query(datauri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                while(dataCursor.moveToNext()){
                    String data1=dataCursor.getString(0);
                    String mimetype=dataCursor.getString(1);
                    if("vnd.android.cursor.item/name".equals(mimetype)){
                        contact.name=data1;
                    }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        contact.phone=data1;
                    }
                }
                contacts.add(contact);
                dataCursor.close();
            }
        }
        cursor.close();
        return contacts;
    }
}
