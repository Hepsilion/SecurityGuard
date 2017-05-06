package org.android.securityguard.safe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.android.securityguard.safe.App;

/**
 * Created by Hepsilion on 2017/1/2.
 */

/**
 * 监听开机的广播接收者，主要用于检查SIM卡是否被更换，如果给更换则发送短信给安全号码
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private final String TAG=BootCompleteReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        ((App)context.getApplicationContext()).checkSim();
    }
}
