package org.android.securityguard.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.process.utils.SystemInfoUtils;
import org.android.securityguard.settings.widget.SettingView;

public class SettingsActivity extends AppCompatActivity {
    private SettingView mBlackNumSV;
    private SettingView mAppLockSV;
    private SharedPreferences mSharedPreferences;
    private boolean running;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_blue));
        ((TextView)findViewById(R.id.tv_title)).setText("设置中心");

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
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

        mBlackNumSV= (SettingView) findViewById(R.id.sv_blacknumber_set);
        mAppLockSV= (SettingView) findViewById(R.id.sv_applock_set);
        mBlackNumSV.setOnCheckedStatusIsChanged(statusListener);
        mAppLockSV.setOnCheckedStatusIsChanged(statusListener);
    }

    @Override
    protected void onStart() {
        running= SystemInfoUtils.isServiceRunning(SettingsActivity.this, );
        mBlackNumSV.setChecked(mSharedPreferences.getBoolean("BlackNumStatus", true));
        super.onStart();
    }

    SettingView.OnCheckedStatusIsChanged statusListener=new SettingView.OnCheckedStatusIsChanged() {
        @Override
        public void onCheckedChanged(View view, boolean isChecked) {
            switch (R.id.sv_blacknumber_set){
                case R.id.sv_blacknumber_set:
                    saveStatus("BlackNumStatus", isChecked);
                    break;
                case R.id.sv_applock_set:
                    saveStatus("AppLockStatus", isChecked);
                    if(isChecked){
                        intent=new Intent(SettingsActivity.this, AppLockService.class);
                        startService(intent);
                    }else{
                        stopService(intent);
                    }
                    break;
            }
        }
    };

    private void saveStatus(String keyname, boolean isChecked){
        if(!TextUtils.isEmpty(keyname)){
            SharedPreferences.Editor editor=mSharedPreferences.edit();
            editor.putBoolean(keyname, isChecked);
            editor.commit();
        }
    }
}
