package org.android.securityguard.cache;

import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.cache.adapter.CacheCleanAdapter;
import org.android.securityguard.cache.entity.CacheInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CacheScanActivity extends AppCompatActivity {
    protected static final int SCANNING=100;
    protected static final int FINISH=101;

    private AnimationDrawable animation;

    private TextView mRecommandTV;  //建议清理
    private TextView mCanCleanTV;   //可以清理

    private long cacheMemory;
    private List<CacheInfo> cacheInfos=new ArrayList<CacheInfo>();
    private List<CacheInfo> mCacheInfos=new ArrayList<CacheInfo>();

    private PackageManager packageManager;
    private CacheCleanAdapter adapter;

    private ListView mCacheLV;
    private Button mCacheBtn;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCANNING:
                    PackageInfo info= (PackageInfo) msg.obj;
                    mRecommandTV.setText("正在扫描: "+info.packageName);
                    mCanCleanTV.setText("已扫描缓存: "+ Formatter.formatFileSize(CacheScanActivity.this, cacheMemory));
                    mCacheInfos.clear();
                    mCacheInfos.addAll(cacheInfos);
                    adapter.notifyDataSetChanged();
                    mCacheLV.setSelection(mCacheInfos.size());
                    break;
                case FINISH:
                    animation.stop();
                    if(cacheMemory>0){
                        mCacheBtn.setEnabled(true);
                    }else{
                        mCacheBtn.setEnabled(false);
                        Toast.makeText(CacheScanActivity.this, R.string.cacheclean_phone_clear, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cache_scan);

        packageManager=getPackageManager();

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.cache_scan);

        ImageView mLeftImg= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImg.setImageResource(R.drawable.back);
        mLeftImg.setOnClickListener(listener);

        mRecommandTV= (TextView) findViewById(R.id.tv_recommend_clean);
        mCanCleanTV= (TextView) findViewById(R.id.tv_can_clean);
        mCacheLV= (ListView) findViewById(R.id.lv_scancache);

        mCacheBtn= (Button) findViewById(R.id.btn_cleanall);
        mCacheBtn.setOnClickListener(listener);

        animation= (AnimationDrawable) findViewById(R.id.imgv_broom).getBackground();
        animation.setOneShot(false);
        animation.start();

        adapter=new CacheCleanAdapter(CacheScanActivity.this, mCacheInfos);
        mCacheLV.setAdapter(adapter);

        fillData();
    }

    /**
     * 填充数据
     */
    private void fillData(){
        Thread thread=new Thread(){
            @Override
            public void run() {
                cacheInfos.clear();
                List<PackageInfo> infos=packageManager.getInstalledPackages(0);
                for(PackageInfo info:infos){
                    getCacheSize(info);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message msg=Message.obtain();
                    msg.obj=info;
                    msg.what=SCANNING;
                    handler.sendMessage(msg);
                }
                Message msg=Message.obtain();
                msg.what=FINISH;
                handler.sendMessage(msg);
            }
        };
        thread.start();
    }

    /**
     * 获取某个包名对应的应用程序的缓存大小
     * @param info
     */
    public void getCacheSize(PackageInfo info){
        try {
            Method method=PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, info.packageName, new MyPackageObserver(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyPackageObserver extends android.content.pm.IPackageStatsObserver.Stub{
        private PackageInfo info;

        public MyPackageObserver(PackageInfo info){
            this.info=info;
        }

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            Long cacheSize=pStats.cacheSize;
            if(cacheSize>=0){
                CacheInfo cacheInfo=new CacheInfo();
                cacheInfo.cacheSize=cacheSize;
                cacheInfo.packageName=info.packageName;
                cacheInfo.appName=info.applicationInfo.loadLabel(packageManager).toString();
                cacheInfo.appIcon=info.applicationInfo.loadIcon(packageManager);
                cacheInfos.add(cacheInfo);
                cacheMemory+=cacheSize;
            }
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.btn_cleanall:
                    if(cacheMemory>0){
                        Intent intent=new Intent(CacheScanActivity.this, CleanCacheActivity.class);
                        intent.putExtra("cacheMemory", cacheMemory);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    };
}