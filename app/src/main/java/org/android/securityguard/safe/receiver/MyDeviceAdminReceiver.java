package org.android.securityguard.safe.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Hepsilion on 2017/1/2.
 */

/**
 * 1. 定义特殊的广播接收者，系统超级管理员的广播接收者
 */
public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
