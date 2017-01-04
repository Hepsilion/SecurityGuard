package org.android.securityguard.process;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.android.securityguard.R;
import org.android.securityguard.process.service.AutoKillProcessService;
import org.android.securityguard.process.utils.SystemInfoUtils;

public class ProcessManagerSettingActivity extends AppCompatActivity {
    private ToggleButton mShowSysAppsTgb;
    private ToggleButton mKillProcessTgb;
    private SharedPreferences mSharedPreferences;
    private boolean running;

    MyListener listener=new MyListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager_setting);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ((TextView)findViewById(R.id.tv_title)).setText("进程管理设置");

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);

        mShowSysAppsTgb= (ToggleButton) findViewById(R.id.tgb_showsys_process);
        mShowSysAppsTgb.setChecked(mSharedPreferences.getBoolean("showSystemProcess", true));
        mShowSysAppsTgb.setOnCheckedChangeListener(listener);
        mKillProcessTgb= (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
        running= SystemInfoUtils.isServiceRunning(ProcessManagerSettingActivity.this, "org.android.securityguard.process.service.AutoKillProcessService");
        mKillProcessTgb.setChecked(running);
        mKillProcessTgb.setOnCheckedChangeListener(listener);
    }

    class MyListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            switch (compoundButton.getId()){
                case R.id.tgb_showsys_process:
                    saveStatus("showSystemProcess", isChecked);
                    break;
                case R.id.tgb_killprocess_lockscreen:
                    Intent service=new Intent(ProcessManagerSettingActivity.this, AutoKillProcessService.class);
                    if(isChecked){
                        startService(service);
                    }else{
                        stopService(service);
                    }
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
            }
        }
    }


    private void saveStatus(String string, boolean isChecked){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putBoolean(string, isChecked);
        editor.commit();
    }
}
