package org.android.securityguard.safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.android.securityguard.R;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class Setup3Activity extends BaseSetupActivity {
    private EditText mInputPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        ((RadioButton)findViewById(R.id.rb_3)).setChecked(true);
        findViewById(R.id.btn_addcontact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btn_addcontact:
                        startActivityForResult(new Intent(Setup3Activity.this, ContactSelectActivity.class), 0);
                        break;
                }
            }
        });
        mInputPhone= (EditText) findViewById(R.id.et_inputphone);
        String safePhone=mSharedPreferences.getString("safephone", null);
        if(!TextUtils.isEmpty(safePhone)){
            mInputPhone.setText(safePhone);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String phone=data.getStringExtra("phone");
            mInputPhone.setText(phone);
        }
    }

    @Override
    public void showNext() {
        String safePhone=mInputPhone.getText().toString().trim();
        if(!TextUtils.isEmpty(safePhone)){
            Toast.makeText(this, R.string.setup_enter_security_phoneno, Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString("safephone", safePhone);
        editor.commit();
        startActivityAndFinishSelf(Setup4Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup2Activity.class);
    }
}
