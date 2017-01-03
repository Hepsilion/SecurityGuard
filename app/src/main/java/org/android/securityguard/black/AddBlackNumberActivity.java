package org.android.securityguard.black;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.black.db.dao.BlackNumberDao;
import org.android.securityguard.black.entity.BlackContactInfo;
import org.android.securityguard.safe.ContactSelectActivity;
import org.w3c.dom.Text;

public class AddBlackNumberActivity extends AppCompatActivity {
    private CheckBox mSmsCB;
    private CheckBox mTelCB;
    private EditText mNumET;
    private EditText mNameET;
    private BlackNumberDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_black_number);

        dao=new BlackNumberDao(this);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.security_add_to_blacklist);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setOnClickListener(listener);
        mLeftImgV.setImageResource(R.drawable.back);

        mSmsCB= (CheckBox) findViewById(R.id.cb_blacknumber_sms);
        mTelCB= (CheckBox) findViewById(R.id.cb_blacknumber_tel);
        mNumET= (EditText) findViewById(R.id.et_blacknumber);
        mNameET= (EditText) findViewById(R.id.et_blackname);

        findViewById(R.id.add_blacknum_btn).setOnClickListener(listener);
        findViewById(R.id.add_fromcontact_btn).setOnClickListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone=data.getStringExtra("phone");
            String name=data.getStringExtra("name");
            mNameET.setText(name);
            mNumET.setText(phone);
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.add_blacknum_btn:
                    String number=mNumET.getText().toString().trim();
                    String name=mNameET.getText().toString().trim();
                    if(TextUtils.isEmpty(number) || TextUtils.isEmpty(name)){
                        Toast.makeText(AddBlackNumberActivity.this, R.string.security_phone_name_blank, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        BlackContactInfo contact=new BlackContactInfo();
                        contact.phoneNumber=number;
                        contact.contactName=name;
                        if(mSmsCB.isChecked() && mTelCB.isChecked()){
                            contact.mode=3;
                        }else if(mSmsCB.isChecked() && !mTelCB.isChecked()){
                            contact.mode=2;
                        }else if(!mSmsCB.isChecked() && mTelCB.isChecked()){
                            contact.mode=1;
                        }else{
                            Toast.makeText(AddBlackNumberActivity.this, R.string.security_must_select_mode, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!dao.isBlackNumber(contact.phoneNumber)){
                            dao.add(contact);
                        }else{
                            Toast.makeText(AddBlackNumberActivity.this, R.string.security_phone_added, Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                    break;
                case R.id.add_fromcontact_btn:
                    startActivityForResult(new Intent(AddBlackNumberActivity.this, ContactSelectActivity.class), 0);
                    break;
            }
        }
    };
}
