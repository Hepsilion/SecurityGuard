package org.android.securityguard.safe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.android.securityguard.R;

/**
 * 设置向导的防盗指令界面
 *
 * Created by Hepsilion on 2017/1/2.
 */

public class LostFindActivity extends Activity {
    private TextView mSafePhoneTV;
    private ToggleButton mToggleButton;
    private TextView mProtectStatusTV;
    private RelativeLayout mEnterSetupRL;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lostfind);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        //如果没有进入过设置向导，则进入
        if(!isSetup()){
            startSetup1Activity();
        }

        TextView mTitleTV= (TextView) findViewById(R.id.tv_title);
        mTitleTV.setText(R.string.lostfind_safe);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.rl_inter_setup_wizard:
                        startSetup1Activity();
                        break;
                    case R.id.imgv_leftbtn:
                        finish();
                        break;
                }
            }
        });
        mLeftImgV.setImageResource(R.drawable.back);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.purple));

        mSafePhoneTV= (TextView) findViewById(R.id.tv_safephone);
        mSafePhoneTV.setText(mSharedPreferences.getString("safephone", ""));

        mEnterSetupRL= (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
        mEnterSetupRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.rl_inter_setup_wizard:
                        startSetup1Activity();
                        break;
                    case R.id.imgv_leftbtn:
                        finish();
                        break;
                }
            }
        });

        mProtectStatusTV= (TextView) findViewById(R.id.tv_lostfind_protectstatus);
        mToggleButton= (ToggleButton) findViewById(R.id.togglebtn_lostfind);
        boolean protecting=mSharedPreferences.getBoolean("protecting", true);
        if(protecting){
            mProtectStatusTV.setText(R.string.setup_safe_switched_on);
            mToggleButton.setEnabled(true);
        }else{
            mProtectStatusTV.setText(R.string.setup_safe_switched_off);
            mToggleButton.setEnabled(false);
        }
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mProtectStatusTV.setText(R.string.setup_safe_switched_on);
                }else{
                    mProtectStatusTV.setText(R.string.setup_safe_switched_off);
                }
                SharedPreferences.Editor editor=mSharedPreferences.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
    }

    private boolean isSetup(){
        return mSharedPreferences.getBoolean("isSetup", false);
    }

    private void startSetup1Activity(){
        Intent intent=new Intent(LostFindActivity.this, Setup1Activity.class);
        startActivity(intent);
        finish();
    }
}