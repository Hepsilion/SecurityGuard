package org.android.securityguard.advance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.advance.fragments.AppLockFragment;
import org.android.securityguard.advance.fragments.AppUnlockFragment;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends AppCompatActivity {
    private TextView mLockTV;
    private TextView mUnlockTV;
    private View slideLockView;
    private View slideUnlockView;
    List<Fragment> mFragments=new ArrayList<Fragment>();
    private ViewPager mAppViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_lock);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_red));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.advanced_app_lock);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mLockTV= (TextView) findViewById(R.id.tv_lock);
        mUnlockTV= (TextView) findViewById(R.id.tv_unlock);
        mLockTV.setOnClickListener(listener);
        mUnlockTV.setOnClickListener(listener);

        slideLockView=findViewById(R.id.view_slide_lock);
        slideUnlockView=findViewById(R.id.view_slide_unlock);

        AppUnlockFragment unlockFragment=new AppUnlockFragment();
        AppLockFragment lockFragment=new AppLockFragment();
        mFragments.add(unlockFragment);
        mFragments.add(lockFragment);

        mAppViewPager= (ViewPager) findViewById(R.id.vp_applock);
        mAppViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mAppViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    slideUnlockView.setBackgroundResource(R.drawable.slide_view);
                    slideLockView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //未加锁
                    mLockTV.setTextColor(getResources().getColor(R.color.black));
                    mUnlockTV.setTextColor(getResources().getColor(R.color.bright_red));
                }else{
                    slideLockView.setBackgroundResource(R.drawable.slide_view);
                    slideUnlockView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    //加锁
                    mLockTV.setTextColor(getResources().getColor(R.color.bright_red));
                    mUnlockTV.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.tv_lock:
                    mAppViewPager.setCurrentItem(1);
                    break;
                case R.id.tv_unlock:
                    mAppViewPager.setCurrentItem(0);
                    break;
            }
        }
    };

    /**
     * ViewPager的Adapter，用于控制ViewPager的页面个数与加载条目
     */
    class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
