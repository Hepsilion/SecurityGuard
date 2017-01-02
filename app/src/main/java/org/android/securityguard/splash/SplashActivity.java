package org.android.securityguard.splash;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.splash.apkutils.ApkUtils;
import org.android.securityguard.splash.apkutils.VersionUpdateUtils;

public class SplashActivity extends Activity {
    private String mVersion;
    private TextView mVersionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        mVersion= ApkUtils.getVersion(getApplicationContext());
        mVersionTV= (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号 "+mVersion);

        final VersionUpdateUtils updateUtils=new VersionUpdateUtils(mVersion, SplashActivity.this);
        new Thread() {
            public void run() {
                //获取服务器版本号
                updateUtils.getCloudVersion();
            }
        }.start();
    }
}
