package org.android.securityguard.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.app.adapter.AppManagerAdapter;
import org.android.securityguard.app.entity.AppInfo;
import org.android.securityguard.app.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends AppCompatActivity {
    private TextView mPhoneMemoryTV;//手机剩余内存
    private TextView mSDMemoryTV;   //SD卡剩余内存
    private TextView mAppNumTV;
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos=new ArrayList<AppInfo>();
    private List<AppInfo> sysAppInfos=new ArrayList<AppInfo>();
    private AppManagerAdapter adapter;
    private UninstallReceiver receiver;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    if(adapter==null){
                        adapter=new AppManagerAdapter(userAppInfos, sysAppInfos, AppManagerActivity.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);

        receiver=new UninstallReceiver();
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(receiver, intentFilter);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_yellow));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.app_manager_title);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.imgv_leftbtn:
                        finish();
                        break;
                }
            }
        });

        mPhoneMemoryTV= (TextView) findViewById(R.id.tv_phonememory_appmanager);
        mSDMemoryTV= (TextView) findViewById(R.id.tv_sdmemory_appmanager);
        mAppNumTV= (TextView) findViewById(R.id.tv_appnumber);

        mListView= (ListView) findViewById(R.id.lv_appmanager);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(adapter!=null){
                    new Thread(){
                        @Override
                        public void run() {
                            AppInfo mAppInfo= (AppInfo) adapter.getItem(position);
                            boolean flag=mAppInfo.isSelected;
                            for(AppInfo appInfo:userAppInfos){
                                appInfo.isSelected=false;
                            }
                            for(AppInfo appInfo:sysAppInfos){
                                appInfo.isSelected=false;
                            }
                            if(mAppInfo!=null){
                                //如果已经选中，则变为未选中
                                if(flag){
                                    mAppInfo.isSelected=false;
                                }else{
                                    mAppInfo.isSelected=true;
                                }
                                mHandler.sendEmptyMessage(15);
                            }
                        }
                    }.start();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem>=userAppInfos.size()+1){
                    mAppNumTV.setText("系统程序: "+sysAppInfos.size()+"个");
                }else{
                    mAppNumTV.setText("用户程序: "+userAppInfos.size()+"个");
                }
            }
        });

        initData();
        getMemoryFromPhone();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        receiver=null;
        super.onDestroy();
    }

    /**
     * 接收应用程序卸载的广播
     */
    class UninstallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }

    private void initData(){
        appInfos=new ArrayList<AppInfo>();
        new Thread(){
            @Override
            public void run() {
                appInfos.clear();
                userAppInfos.clear();
                sysAppInfos.clear();

                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));
                for(AppInfo appInfo:appInfos){
                    if(appInfo.isUserApp){
                        userAppInfos.add(appInfo);
                    }else{
                        sysAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            }
        }.start();
    }

    /**
     * 获取手机和SD卡剩余内存
     */
    private void getMemoryFromPhone(){
        long avail_sd= Environment.getExternalStorageDirectory().getFreeSpace();
        long avail_rom=Environment.getDataDirectory().getFreeSpace();

        // 由于上面的数值为long比较长，因此对其进行格式化使其返回单位为KB或MB的String
        String str_avail_sd= Formatter.formatFileSize(this, avail_sd);
        String str_avail_rom=Formatter.formatFileSize(this, avail_rom);
        mPhoneMemoryTV.setText("剩余手机内存: "+str_avail_rom);
        mSDMemoryTV.setText("剩余SD卡内存"+str_avail_sd);
    }
}