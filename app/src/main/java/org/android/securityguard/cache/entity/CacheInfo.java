package org.android.securityguard.cache.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Hepsilion on 2017/1/4.
 */

/**
 * 手机缓存信息
 */
public class CacheInfo {
    public String packageName;//应用包名
    public long cacheSize;    //缓存大小
    public Drawable appIcon;  //应用图标
    public String appName;    //应用名称
}
