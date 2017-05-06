package org.android.securityguard.safe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import org.android.securityguard.R;

/**
 * 绑定SIM卡界面
 *
 * Created by Hepsilion on 2017/1/2.
 */

public class Setup2Activity extends BaseSetupActivity {
    private TelephonyManager mTelephonyManager;
    private Button mBindSimButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        mTelephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        ((RadioButton)findViewById(R.id.rb_2)).setChecked(true);
        mBindSimButton= (Button) findViewById(R.id.btn_bind_sim);
        mBindSimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_bind_sim:
                        bindSim();
                        break;
                }
            }
        });
        if(isBind()){
            mBindSimButton.setEnabled(false);
        }else{
            mBindSimButton.setEnabled(true);
        }
    }

    @Override
    public void showNext() {
        if(!isBind()){
            Toast.makeText(this, R.string.setup_sim_not_bind, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivityAndFinishSelf(Setup3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup1Activity.class);
    }

    /**
     * 绑定SIM卡
     */
    private void bindSim(){
        if(!isBind()){
            String simSerialNumber=mTelephonyManager.getSimSerialNumber();
            SharedPreferences.Editor editor=mSharedPreferences.edit();
            editor.putString("sim", simSerialNumber);
            editor.commit();
            Toast.makeText(this, R.string.setup_sim_bind_success, Toast.LENGTH_SHORT).show();
            mBindSimButton.setEnabled(false);
        }else{
            Toast.makeText(this, R.string.setup_sim_binded, Toast.LENGTH_SHORT).show();
            mBindSimButton.setEnabled(false);
        }
    }

    private boolean isBind(){
        String simString=mSharedPreferences.getString("sim", null);
        if(TextUtils.isEmpty(simString)){
            return false;
        }else{
            return true;
        }
    }
}
