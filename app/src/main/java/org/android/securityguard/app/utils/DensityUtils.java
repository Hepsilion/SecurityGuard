package org.android.securityguard.app.utils;

import android.content.Context;

/**
 * Created by Hepsilion on 2017/1/3.
 */
public class DensityUtils {
    /**
     * dip转换为像素px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue){
        try{
            //获取屏幕分辨率
            final float scale=context.getResources().getDisplayMetrics().density;
            return (int) (dpValue*scale+0.5f);
        }catch(Exception e){
            e.printStackTrace();
        }
        return (int) dpValue;
    }

    /**
     * 像素px转换为dip
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        try{
            final float scale=context.getResources().getDisplayMetrics().density;
            return (int) (pxValue/scale+0.5f);
        }catch(Exception e){
            e.printStackTrace();
        }
        return (int) pxValue;
    }
}
