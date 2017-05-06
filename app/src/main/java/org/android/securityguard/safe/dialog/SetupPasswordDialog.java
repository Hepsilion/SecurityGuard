package org.android.securityguard.safe.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.android.securityguard.R;


/**
 * Created by Hepsilion on 2017/1/1.
 */

/**
 * 设置密码对话框
 */
public class SetupPasswordDialog extends Dialog implements View.OnClickListener {
    private TextView mTitleTV;   //标题栏
    public EditText mFirstPWDET;//首次输入密码文本框
    public EditText mConfirmET; //确认密码文本框
    private MyCallback callback;

    public SetupPasswordDialog(Context context){
        super(context, R.style.dialog_custom);  //引入自定义对话框样式
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.setup_password_dialog);
        super.onCreate(savedInstanceState);

        mTitleTV= (TextView) findViewById(R.id.tv_setuppwd_title);
        mFirstPWDET= (EditText) findViewById(R.id.et_firstpwd);
        mConfirmET= (EditText) findViewById(R.id.et_comfirm_password);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_cancle).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                callback.ok();
                break;
            case R.id.btn_cancle:
                callback.cancle();
                break;
        }
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitleTV.setText(title);
        }
    }

    public void setCallback(MyCallback callback){
        this.callback=callback;
    }

    public interface MyCallback{
        void ok();
        void cancle();
    }
}