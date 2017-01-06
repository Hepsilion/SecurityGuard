package org.android.securityguard.advance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;

public class AdvancedToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advanced_tools);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.advanced_tools_title);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        findViewById(R.id.atv_app_lock).setOnClickListener(listener);
        findViewById(R.id.atv_num_belongto).setOnClickListener(listener);
        findViewById(R.id.atv_sms_backup).setOnClickListener(listener);
        findViewById(R.id.atv_sms_restore).setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.atv_app_lock:     //程序锁
                    startActivity(AppLockActivity.class);
                    break;
                case R.id.atv_num_belongto://归属地查询
                    startActivity(NumBelongtoActivity.class);
                    break;
                case R.id.atv_sms_backup: //短信备份
                    startActivity(SMSBackupActivity.class);
                    break;
                case R.id.atv_sms_restore:  //短信还原
                    startActivity(SMSRestoreActivity.class);
                    break;
            }
        }
    };

    /**
     * 开启新的Activity，不关闭自己
     * @param clazz
     */
    public void startActivity(Class<?> clazz){
        Intent intent=new Intent(AdvancedToolsActivity.this, clazz);
        startActivity(intent);
    }
}