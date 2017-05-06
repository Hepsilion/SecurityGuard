package org.android.securityguard.safe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import org.android.securityguard.R;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public abstract class BaseSetupActivity extends Activity {
    public SharedPreferences mSharedPreferences;
    private GestureDetector mGestureDetector;    //手势识别器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        mGestureDetector=new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            /**
             *
             * @param e1 代表手指第一次触摸屏幕的事件
             * @param e2 代表手指离开屏幕一瞬间的事件
             * @param velocityX 水平方向的速度 pix/s
             * @param velocityY 垂直方向的速度 pix/s
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(velocityX)<200){
                    Toast.makeText(getApplicationContext(), R.string.setup_invalid_action, Toast.LENGTH_SHORT).show();
                    return true;
                }
                //从左向右滑动屏幕，显示上一个界面
                if(e2.getRawX()-e1.getRawX()>200){
                    showPre();
                    overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                    return true;
                }
                //从右向左滑动屏幕，显示下一个界面
                if(e1.getRawX()-e2.getRawX()>200){
                    showNext();
                    overridePendingTransition(R.anim.next_in, R.anim.next_out);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 用手势识别器去识别事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 开启新的Activity并结束自己
     * @param clazz
     */
    public void startActivityAndFinishSelf(Class<?> clazz){
        Intent intent=new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    public abstract void showNext();
    public abstract void showPre();
}
