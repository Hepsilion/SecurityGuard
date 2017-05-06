package org.android.securityguard.safe;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import org.android.securityguard.R;

/**
 * 手机防盗模块的展示界面
 *
 * Created by Hepsilion on 2017/1/2.
 */

public class Setup1Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

        ((RadioButton)findViewById(R.id.rb_1)).setChecked(true);
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(Setup2Activity.class);
    }

    @Override
    public void showPre() {
        Toast.makeText(this, R.string.setup_first_page, Toast.LENGTH_SHORT).show();
    }
}
