package org.android.securityguard.virus;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.virus.adapter.ScanVirusAdapter;
import org.android.securityguard.virus.dao.AntiVirusDao;
import org.android.securityguard.virus.entity.ScanAppInfo;
import org.android.securityguard.virus.utils.MD5Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VirusScanSpeedActivity extends AppCompatActivity {
    protected static final int SCAN_BEGIN=100;
    protected static final int SCANNING=101;
    protected static final int SCAN_FINISH=102;

    private int total;
    private int process;

    private TextView mProcessTV;
    private PackageManager packageManager;
    private boolean flag;
    private boolean isStop;
    private TextView mScanAppTV;
    private Button mCancleBtn;
    private ImageView mScanningIcon;
    private RotateAnimation rani;
    private ListView mScanListView;
    private ScanVirusAdapter adapter;

    private List<ScanAppInfo> mScanAppInfos=new ArrayList<ScanAppInfo>();
    private SharedPreferences mSharedPreferences;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCAN_BEGIN:
                    mScanAppTV.setText(R.string.virus_scan_begin);
                    break;
                case SCANNING:
                    ScanAppInfo scanAppInfo= (ScanAppInfo) msg.obj;
                    mScanAppTV.setText("正在扫描: "+ scanAppInfo.appName);
                    int speed=msg.arg1;
                    mProcessTV.setText((speed*100/total)+"%");
                    mScanAppInfos.add(scanAppInfo);

                    adapter.notifyDataSetChanged();
                    mScanListView.setSelection(mScanAppInfos.size());
                    break;
                case SCAN_FINISH:
                    mScanAppTV.setText(R.string.virus_scan_complete);
                    mScanningIcon.clearAnimation();
                    mCancleBtn.setBackgroundResource(R.drawable.scan_complete);
                    saveScanTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virus_scan_speed);

        packageManager=getPackageManager();
        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.virus_scan_title_process);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mProcessTV= (TextView) findViewById(R.id.tv_scanprocess);
        mScanAppTV= (TextView) findViewById(R.id.tv_scansapp);
        mScanningIcon= (ImageView) findViewById(R.id.imgv_scanningicon);

        mCancleBtn= (Button) findViewById(R.id.btn_canclescan);
        mCancleBtn.setOnClickListener(listener);

        adapter=new ScanVirusAdapter(mScanAppInfos, VirusScanSpeedActivity.this);
        mScanListView= (ListView) findViewById(R.id.lv_scanapps);
        mScanListView.setAdapter(adapter);

        startAnim();
        scanVirus();
    }

    private void saveScanTime(){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime=sdf.format(new Date());
        currentTime="上次查杀: "+currentTime;
        editor.putString("lastVirusScan", currentTime);
        editor.commit();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.btn_canclescan:
                    if(process==total && process>0){//扫描完成
                        finish();
                    }else if(process>0 && process<total && isStop==false){//取消扫描
                        mScanningIcon.clearAnimation();
                        flag=false;
                        mCancleBtn.setBackgroundResource(R.drawable.restart_scan_btn);
                    }else if(isStop){//重新扫描
                        startAnim();
                        scanVirus();
                        mCancleBtn.setBackgroundResource(R.drawable.cancle_scan_btn_selector);                    }
                break;
            }
        }
    };

    private void startAnim(){
        if(rani==null){
            rani=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        rani.setRepeatCount(Animation.INFINITE);
        rani.setDuration(2000);
        mScanningIcon.startAnimation(rani);
    }

    private void scanVirus(){
        flag=true;
        isStop=false;
        process=0;
        mScanAppInfos.clear();
        new Thread(){
            @Override
            public void run() {
                Message msg=Message.obtain();
                msg.what=SCAN_BEGIN;
                mHandler.sendMessage(msg);

                List<PackageInfo> installedPackages=packageManager.getInstalledPackages(0);
                total=installedPackages.size();
                for(PackageInfo info:installedPackages){
                    if(!flag){
                        isStop=true;
                        return;
                    }
                    String apkpath=info.applicationInfo.sourceDir;
                    String md5= MD5Utils.getFileMd5(apkpath);
                    String result= AntiVirusDao.checkVirus(md5);
                    msg=Message.obtain();
                    msg.what=SCANNING;
                    ScanAppInfo scanAppInfo=new ScanAppInfo();
                    if(result==null){
                        scanAppInfo.description="扫描安全";
                        scanAppInfo.isVirus=false;
                    }else{
                        scanAppInfo.description=result;
                        scanAppInfo.isVirus=true;
                    }

                    process++;
                    scanAppInfo.packageName=info.packageName;
                    scanAppInfo.appName=info.applicationInfo.loadLabel(packageManager).toString();
                    scanAppInfo.appicon=info.applicationInfo.loadIcon(packageManager);
                    msg.obj=scanAppInfo;
                    msg.arg1=process;
                    mHandler.sendMessage(msg);

                    try{
                        Thread.sleep(300);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                msg=Message.obtain();
                msg.what=SCAN_FINISH;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        flag=false;
        super.onDestroy();
    }
}
