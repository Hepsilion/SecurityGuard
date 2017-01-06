package org.android.securityguard.advance.fragments;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.advance.adapter.AppLockAdapter;
import org.android.securityguard.advance.db.dao.AppLockDao;
import org.android.securityguard.advance.entity.AppInfo;
import org.android.securityguard.advance.utils.AppInfoParser;

import java.util.ArrayList;
import java.util.List;

public class AppLockFragment extends Fragment {
    private TextView mLockTV;
    private ListView mLockLV;
    List<AppInfo> lockApps=new ArrayList<AppInfo>();
    private List<AppInfo> appInfos;
    private AppLockAdapter adapter;
    private AppLockDao dao;
    private Uri uri= Uri.parse("content://org.android.securityguard.applock");
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    lockApps.clear();
                    lockApps.addAll((List<AppInfo>) msg.obj);
                    if(adapter==null){
                        adapter=new AppLockAdapter(lockApps, getActivity());
                        mLockLV.setAdapter(adapter);
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                    mLockTV.setText("加锁应用"+lockApps.size()+"个");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_app_lock, null);
        mLockTV= (TextView) view.findViewById(R.id.tv_fragment_lock);
        mLockLV= (ListView) view.findViewById(R.id.lv_fragment_lock);
        return view;
    }

    @Override
    public void onResume() {
        dao=new AppLockDao(getActivity());
        appInfos= AppInfoParser.getAppInfos(getActivity());
        fillData();
        initListener();
        getActivity().getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                fillData();
            }
        });
        super.onResume();
    }

    public void fillData(){
        final List<AppInfo> appInfos=new ArrayList<AppInfo>();
        new Thread(){
            @Override
            public void run() {
                for(AppInfo appInfo:appInfos){
                    if(!dao.find(appInfo.packageName)){
                        appInfo.isLock=true;
                        appInfos.add(appInfo);
                    }
                }
                Message msg=new Message();
                msg.obj=appInfos;
                msg.what=10;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void initListener(){
        if(mLockLV==null)
            Log.i("", "11111111");
        mLockLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                TranslateAnimation ta=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                ta.setDuration(300);
                view.startAnimation(ta);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //程序锁信息被从数据库中删除
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dao.delete(lockApps.get(position).packageName);
                                lockApps.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }.start();
            }
        });
    }
}