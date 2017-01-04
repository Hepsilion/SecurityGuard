package org.android.securityguard.virus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VirusScanActivity extends AppCompatActivity {
    private TextView mLastTimeTV;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virus_scan);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.virus_scan);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        findViewById(R.id.rl_allscanvirus).setOnClickListener(listener);

        mLastTimeTV= (TextView) findViewById(R.id.tv_lastscantime);

        copyDB("antivirus.db");
    }

    @Override
    protected void onResume() {
        String string=mSharedPreferences.getString("lastVirusScan", "您还没有查杀病毒!");
        mLastTimeTV.setText(string);
        super.onResume();
    }

    /**
     * 复制病毒数据库
     * @param dbname
     */
    private void copyDB(final String dbname){
        new Thread(){
            @Override
            public void run() {
                try {
                    File file=new File(getFilesDir(), dbname);
                    if(file.exists() && file.length()>0){System.out.println(file.getPath());
                        Log.i("VirusScanActivity", "数据库已存在!");
                        return;
                    }

                    InputStream is=getAssets().open(dbname);
                    FileOutputStream fos=openFileOutput(dbname, MODE_PRIVATE);
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

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.rl_allscanvirus:
                    startActivity(new Intent(VirusScanActivity.this, VirusScanSpeedActivity.class));
                    break;
            }
        }
    };
}
