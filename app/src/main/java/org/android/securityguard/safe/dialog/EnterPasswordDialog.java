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
 * 输入密码对话框
 */
public class EnterPasswordDialog extends Dialog implements View.OnClickListener {
    private TextView mTitleTV;   //标题栏
    public EditText mEnterPwdET;//输入密码文本框
    private MyCallback callback;

    public EnterPasswordDialog(Context context) {
        super(context, R.style.dialog_custom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.enter_password_dialog);
        super.onCreate(savedInstanceState);

        mTitleTV= (TextView) findViewById(R.id.tv_setuppwd_title);
        mEnterPwdET= (EditText) findViewById(R.id.et_enter_password);
        findViewById(R.id.btn_comfirm).setOnClickListener(this);
        findViewById(R.id.btn_dismiss).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_comfirm:
                callback.confirm();
                break;
            case R.id.btn_dismiss:
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
        void confirm();
        void cancle();
    }
}