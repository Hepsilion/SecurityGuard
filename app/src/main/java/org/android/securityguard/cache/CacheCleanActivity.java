package org.android.securityguard.cache;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class CacheCleanActivity extends AppCompatActivity {
    protected static final int CLEANNING=100;
    private AnimationDrawable animation;
    private long cacheMemory;

    private TextView mMemoryTV;
    private TextView mMemoryUnitTV;
    private TextView mSizeTV;

    private PackageManager packageManager;

    private FrameLayout mCleanCacheFL;
    private FrameLayout mFinishCleanFL;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CLEANNING:
                    long memory= (long) msg.obj;
                    formatMemory(memory);
                    if(memory==cacheMemory){
                        animation.stop();
                        mCleanCacheFL.setVisibility(View.GONE);
                        mFinishCleanFL.setVisibility(View.VISIBLE);
                        mSizeTV.setText("成功清理: "+Formatter.formatFileSize(CacheCleanActivity.this, cacheMemory));
                    }
                    break;
            }
        }
    };

    private void formatMemory(long memory){
        String cacheMemoryStr= Formatter.formatFileSize(CacheCleanActivity.this, memory);
        String memoryStr;
        String memoryUnit;

        //根据大小判断单位
        if(memory>900){//大于900则单位是两位
            memoryStr=cacheMemoryStr.substring(0, cacheMemoryStr.length()-2);
            memoryUnit=cacheMemoryStr.substring(cacheMemoryStr.length()-2, cacheMemoryStr.length());
        }else{         //单位是一位
            memoryStr=cacheMemoryStr.substring(0, cacheMemoryStr.length()-1);
            memoryUnit=cacheMemoryStr.substring(cacheMemoryStr.length()-1, cacheMemoryStr.length());
        }

        mMemoryTV.setText(memoryStr);
        mMemoryUnitTV.setText(memoryUnit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cache_clean);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.rose_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.cacheclean_clean);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        animation= (AnimationDrawable) findViewById(R.id.imgv_trashbin_cacheclean).getBackground();
        animation.setOneShot(false);
        animation.start();

        mMemoryTV= (TextView) findViewById(R.id.tv_cleancache_memory);
        mMemoryUnitTV= (TextView) findViewById(R.id.tv_cleancache_memoryunit);
        mSizeTV= (TextView) findViewById(R.id.tv_cleanmemorysize);

        mCleanCacheFL= (FrameLayout) findViewById(R.id.fl_cleancache);
        mFinishCleanFL= (FrameLayout) findViewById(R.id.fl_finishclean);

        findViewById(R.id.btn_finish_cleancache).setOnClickListener(listener);

        packageManager=getPackageManager();
        Intent intent=getIntent();
        cacheMemory=intent.getLongExtra("cacheMemory", 0);

        initData();
    }

    private void initData(){
        cleanAll();
        new Thread(){
            @Override
            public void run() {
                long memory=0;
                while(memory<cacheMemory){
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Random rand=new Random();
                    memory+=1024*rand.nextInt(1024);
                    if(memory>cacheMemory){
                        memory=cacheMemory;
                    }
                    Message msg=Message.obtain();
                    msg.what=CLEANNING;
                    msg.obj=memory;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void cleanAll(){
        //清除全部缓存利用Android系统的一个漏洞:freeStorageAndNotify
        Method[] methods=PackageManager.class.getMethods();
        for(Method method:methods){
            if("freeStorageAndNotify".equals(method.getName())){
                try {
                    method.invoke(packageManager, Long.MAX_VALUE, new ClearCacheObserver());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        Toast.makeText(CacheCleanActivity.this, R.string.cacheclean_clean_finish, Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.btn_finish_cleancache:
                    finish();
                    break;
            }
        }
    };

    class ClearCacheObserver extends android.content.pm.IPackageDataObserver.Stub{
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {

        }
    }
}
