package org.android.securityguard.traffic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.android.securityguard.traffic.utils.SystemInfoUtils;

/**
 * Created by Hepsilion on 2017/1/5.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!SystemInfoUtils.isServiceRunning(context, "org.android.securityguard.traffic.service.TrafficMonitoringService.class")){
            context.startActivity(new Intent(context, TrafficMonitoringService.class));
        }
    }
}
