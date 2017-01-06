package org.android.securityguard.advance;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.advance.db.dao.NumBelongtoDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class NumBelongtoActivity extends AppCompatActivity {
    private EditText mNumET;
    private TextView mResultTV;
    private Button mSearchBtn;

    private String dbName="address.db";

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_num_belongto);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.advanced_number_belongto_title);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mResultTV= (TextView) findViewById(R.id.tv_searchresult);
        mNumET= (EditText) findViewById(R.id.et_num_num_belongto);
        mNumET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            /**
             * 文本变化之后
             * @param editable
             */
            @Override
            public void afterTextChanged(Editable editable) {
                String string=editable.toString().trim();
                if(string.length()==0){
                    mResultTV.setText("");
                }
            }
        });

        mSearchBtn= (Button) findViewById(R.id.btn_searchnumbelongto);
        mSearchBtn.setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.btn_searchnumbelongto:
                    String phonenumber=mNumET.getText().toString().trim();
                    if(!TextUtils.isEmpty(phonenumber)){
                        File file=new File(getFilesDir(), dbName);
                        if(!file.exists() || file.length()<=0){
                            copyDB(dbName);
                        }
                        //查询数据库
                        String location= NumBelongtoDao.getLocation(phonenumber);
                        mResultTV.setText("归属地: "+location);
                    }else{
                        Toast.makeText(NumBelongtoActivity.this, R.string.advanced_number_belongto_search, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 复制asset目录下的数据库文件
     * @param dbName
     */
    private void copyDB(final String dbName){
        new Thread(){
            @Override
            public void run() {
                File file=new File(getFilesDir(), dbName);
                if(file.exists() && file.length()>0){
                    Log.i("NumBelongtoActivity", "数据库已存在");
                    return;
                }

                try {
                    InputStream is=getAssets().open(dbName);
                    FileOutputStream fos=openFileOutput(dbName, MODE_PRIVATE);
                    byte[] buffer=new byte[1024];
                    int len=0;
                    while((len=is.read(buffer))!=-1){
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}