package org.android.securityguard.advance.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Hepsilion on 2017/1/6.
 */
public class UIUtils {
    public static void showToast(final Activity context, final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}