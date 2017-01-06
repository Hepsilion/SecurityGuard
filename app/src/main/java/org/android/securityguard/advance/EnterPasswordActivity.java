package org.android.securityguard.advance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.safe.utils.MD5;
import org.android.securityguard.virus.utils.MD5Utils;

public class EnterPasswordActivity extends AppCompatActivity {
    private ImageView mAppIcon;
    private TextView mAppNameTV;
    private EditText mPasswdET;
    private ImageView mGoImgV;
    private LinearLayout mEnterPasswdLL;
    private SharedPreferences mSharedPreferences;
    private String password;
    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enter_password);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        password=mSharedPreferences.getString("PhoneAntiTheftPWD", null);

        Intent intent=getIntent();
        packageName=intent.getStringExtra("packageName");

        mEnterPasswdLL= (LinearLayout) findViewById(R.id.ll_enter_password);
        mPasswdET= (EditText) findViewById(R.id.et_enter_password_password);
        mGoImgV= (ImageView) findViewById(R.id.imgv_enter_password_go);
        mGoImgV.setOnClickListener(listener);

        mAppIcon= (ImageView) findViewById(R.id.imgv_enter_password_appicon);
        mAppNameTV= (TextView) findViewById(R.id.tv_enter_password_appname);
        PackageManager packageManager=getPackageManager();
        try {
            mAppIcon.setImageDrawable(packageManager.getApplicationInfo(packageName, 0).loadIcon(packageManager));
            mAppNameTV.setText(packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager).toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_enter_password_go:
                    String input=mPasswdET.getText().toString().trim();
                    if(TextUtils.isEmpty(input)){
                        startAnim();
                        Toast.makeText(EnterPasswordActivity.this, R.string.advanced_enter_password, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if(!TextUtils.isEmpty(password)){
                            if(MD5.encode(input).equals(password)){
                                Intent intent=new Intent();
                                intent.setAction("org.android.securityguard.applock");
                                intent.putExtra("packageName", packageName);
                                sendBroadcast(intent);
                                finish();
                            }else{
                                startAnim();
                                Toast.makeText(EnterPasswordActivity.this, R.string.advanced_wrong_password, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    break;
            }
        }
    };

    private void startAnim(){
        Animation animation= AnimationUtils.loadAnimation(EnterPasswordActivity.this, R.anim.shake);
        mEnterPasswdLL.startAnimation(animation);
    }
}
