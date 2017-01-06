package org.android.securityguard.advance;

import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.advance.utils.SMSBackupUtils;
import org.android.securityguard.advance.utils.UIUtils;
import org.android.securityguard.advance.widget.MyCircleProgress;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SMSBackupActivity extends AppCompatActivity {
    private MyCircleProgress mProgressButton;
    private boolean flag=false;
    private SMSBackupUtils smsBackupUtils;
    private static final int CHANGE_BUTTON_TEXT=100;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_BUTTON_TEXT:
                    mProgressButton.setText(R.string.advanced_one_key_backup);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_smsbackup);

        smsBackupUtils=new SMSBackupUtils();

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.advanced_sms_backup);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mProgressButton= (MyCircleProgress) findViewById(R.id.mcp_sms_backup);
        mProgressButton.setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.mcp_sms_backup:
                    if(flag){
                        flag=false;
                        mProgressButton.setText(R.string.advanced_one_key_backup);
                    }else{
                        flag=true;
                        mProgressButton.setText(R.string.advanced_backup_cancel);
                    }

                    smsBackupUtils.setFlag(flag);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                boolean backupSms=smsBackupUtils.backupSMS(SMSBackupActivity.this, new SMSBackupUtils.BackupStatusCallback() {
                                    @Override
                                    public void beforeSMSBackup(int size) {
                                        if(size<0){
                                            flag=false;
                                            smsBackupUtils.setFlag(flag);
                                            UIUtils.showToast(SMSBackupActivity.this, "您还没有短信!");
                                            mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                                        }else{
                                            mProgressButton.setMax(size);
                                        }
                                    }

                                    @Override
                                    public void onSMSBackup(int process) {
                                        mProgressButton.setProcess(process);
                                    }
                                });
                                if(backupSms){
                                    UIUtils.showToast(SMSBackupActivity.this, "备份成功");
                                }else{
                                    UIUtils.showToast(SMSBackupActivity.this, "备份失败");
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                UIUtils.showToast(SMSBackupActivity.this, "文件生成失败");
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                                UIUtils.showToast(SMSBackupActivity.this, "SD卡不可用或SD卡内存不足");
                            } catch (IOException e){
                                e.printStackTrace();
                                UIUtils.showToast(SMSBackupActivity.this, "读写错误");
                            }
                        }
                    }.start();
                    break;
            }
        }
    };
}