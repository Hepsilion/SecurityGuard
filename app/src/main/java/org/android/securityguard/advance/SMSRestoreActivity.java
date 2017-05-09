package org.android.securityguard.advance;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.advance.utils.SMSRestoreUtils;
import org.android.securityguard.advance.utils.UIUtils;
import org.android.securityguard.advance.widget.MyCircleProgress;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SMSRestoreActivity extends AppCompatActivity {
    private MyCircleProgress mProgressButton;
    private boolean flag=false;
    private SMSRestoreUtils smsRestoreUtils;

    private static final int CHANGE_BUTTON_TEXT=101;

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
        setContentView(R.layout.activity_smsrestore);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.advanced_sms_restore);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mProgressButton= (MyCircleProgress) findViewById(R.id.mcp_sms_restore);
        mProgressButton.setOnClickListener(listener);

        smsRestoreUtils=new SMSRestoreUtils();
    }

    @Override
    protected void onDestroy() {
        flag=false;
        smsRestoreUtils.setFlag(flag);
        super.onDestroy();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.mcp_sms_restore:
                    if(flag){
                        flag=false;
                        mProgressButton.setText(R.string.advanced_one_key_restore);
                    }else{
                        flag=true;
                        mProgressButton.setText(R.string.advanced_restore_cancel);
                    }
                    smsRestoreUtils.setFlag(flag);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                smsRestoreUtils.restoreSMS(SMSRestoreActivity.this, new SMSRestoreUtils.SMSRestoreCallback() {
                                    @Override
                                    public void beforeSMSRestore(int size) {
                                        mProgressButton.setMax(size);
                                    }

                                    @Override
                                    public void onSMSRestore(int process) {
                                        mProgressButton.setProcess(process);
                                    }
                                });
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                                UIUtils.showToast(SMSRestoreActivity.this, "文件格式错误");
                            } catch (IOException e) {
                                e.printStackTrace();
                                UIUtils.showToast(SMSRestoreActivity.this, "读写错误");
                            } finally{
                                flag=false;
                                mProgressButton.setProcess(0);
                                mHandler.sendEmptyMessage(CHANGE_BUTTON_TEXT);
                            }
                        }
                    }.start();
                    break;
            }
        }
    };
}