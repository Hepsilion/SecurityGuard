package org.android.securityguard.safe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.safe.adapter.ContactAdapter;
import org.android.securityguard.safe.entity.ContactInfo;
import org.android.securityguard.safe.utils.ContactInfoParser;

import java.util.List;

public class ContactSelectActivity extends Activity {
    private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> contacts;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (contacts != null) {
                        adapter = new ContactAdapter(contacts, ContactSelectActivity.this);
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact_select);

        ((TextView)findViewById(R.id.tv_title)).setText(R.string.setup_select_contact);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.imgv_leftbtn:
                        finish();
                        break;
                }
            }
        });
        mLeftImgV.setImageResource(R.drawable.back);

        //设置导航栏颜色
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));

        mListView= (ListView) findViewById(R.id.lv_content);
        new Thread(){
            @Override
            public void run() {
                //contacts= ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                contacts.addAll(ContactInfoParser.getSystemContact(ContactSelectActivity.this));
                mHandler.sendEmptyMessage(10);
            }
        }.start();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ContactInfo item= (ContactInfo) adapter.getItem(position);
                Intent intent=new Intent();
                intent.putExtra("phone", item.phone);
                setResult(0, intent);
                finish();
            }
        });
    }
}
