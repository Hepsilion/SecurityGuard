package org.android.securityguard.black;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.black.adapter.BlackContactAdapter;
import org.android.securityguard.black.db.dao.BlackNumberDao;
import org.android.securityguard.black.entity.BlackContactInfo;

import java.util.ArrayList;
import java.util.List;

public class SecurityPhoneActivity extends AppCompatActivity {
    private FrameLayout mHaveBlackNumber; //有黑名单时显示的帧布局
    private FrameLayout mNoBlackNumber;   //没有黑名单时显示的帧布局

    private BlackNumberDao dao;
    private ListView mListView;
    private int pagenumber=0;
    private int pagesize=15;
    private int totalNumber;
    private List<BlackContactInfo> contacts=new ArrayList<BlackContactInfo>();
    private BlackContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_securityphone);

        initView();
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(totalNumber!=dao.getTotalNumber()){
            if(dao.getTotalNumber()>0){
                mHaveBlackNumber.setVisibility(View.VISIBLE);
                mNoBlackNumber.setVisibility(View.GONE);
            }else{
                mHaveBlackNumber.setVisibility(View.GONE);
                mNoBlackNumber.setVisibility(View.VISIBLE);
            }
            pagenumber=0;
            contacts.clear();
            contacts.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_purple));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.security_title_text);

        mHaveBlackNumber= (FrameLayout) findViewById(R.id.fl_haveblacknumber);
        mNoBlackNumber= (FrameLayout) findViewById(R.id.fl_noblacknumber);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setOnClickListener(listener);
        mLeftImgV.setImageResource(R.drawable.back);

        findViewById(R.id.btn_addblacknumber).setOnClickListener(listener);

        mListView= (ListView) findViewById(R.id.lv_blacknumbers);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                switch (state){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://没有滑动的状态
                        int lastVisiblePosition=mListView.getLastVisiblePosition();
                        if(lastVisiblePosition==contacts.size()-1){
                            pagenumber++;
                            if(pagenumber*pagesize>=totalNumber){
                                Toast.makeText(SecurityPhoneActivity.this, R.string.security_no_more_data, Toast.LENGTH_SHORT).show();
                            }else{
                                contacts.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
                                adapter.notifyDataSetChanged();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    /**
     * 用于填充数据，重新刷新页面
     */
    private void fillData(){
        dao=new BlackNumberDao(SecurityPhoneActivity.this);
        totalNumber=dao.getTotalNumber();
        if(totalNumber==0){
            mHaveBlackNumber.setVisibility(View.GONE);
            mNoBlackNumber.setVisibility(View.VISIBLE);
        }else if(totalNumber>0){
            mHaveBlackNumber.setVisibility(View.VISIBLE);
            mNoBlackNumber.setVisibility(View.GONE);
            pagenumber=0;
            if(contacts.size()>0){
                contacts.clear();
            }
            contacts.addAll(dao.getPageBlackNumber(pagenumber, pagesize));
            if(adapter==null){
                adapter=new BlackContactAdapter(contacts, SecurityPhoneActivity.this);
                adapter.setCallBack(new BlackContactAdapter.BlackContactCallback(){
                    @Override
                    public void dataSizeChanged() {
                        fillData();
                    }
                });
                mListView.setAdapter(adapter);
            }else{
                adapter.notifyDataSetChanged();
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.btn_addblacknumber:
                    startActivity(new Intent(SecurityPhoneActivity.this, AddBlackNumberActivity.class));
                    break;
            }
        }
    };
}
