package org.android.securityguard.safe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.android.securityguard.R;

/**
 * 设置完成界面
 *
 * Created by Hepsilion on 2017/1/2.
 */

public class Setup4Activity extends BaseSetupActivity {
    private TextView mStatusTV;
    private ToggleButton mToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        ((RadioButton)findViewById(R.id.rb_4)).setChecked(true);
        mStatusTV= (TextView) findViewById(R.id.tv_setup4_status);
        mToggleButton= (ToggleButton) findViewById(R.id.togglebtn_securityfunction);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mStatusTV.setText(R.string.setup_safe_switched_on);
                }else{
                    mStatusTV.setText(R.string.setup_safe_switched_off);
                }
                SharedPreferences.Editor editor=mSharedPreferences.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
        boolean protecting=mSharedPreferences.getBoolean("protecting", true);
        if(protecting){
            mStatusTV.setText(R.string.setup_safe_switched_on);
            mToggleButton.setChecked(true);
        }else{
            mStatusTV.setText(R.string.setup_safe_switched_off);
            mToggleButton.setChecked(false);
        }
    }

    /**
     * 跳转至防盗保护页面
     */
    @Override
    public void showNext() {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putBoolean("isSetup", true);
        editor.commit();
        startActivityAndFinishSelf(LostFindActivity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup3Activity.class);
    }
}
