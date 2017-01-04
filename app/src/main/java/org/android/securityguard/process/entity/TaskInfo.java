package org.android.securityguard.process.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Hepsilion on 2017/1/4.
 */
public class TaskInfo {
    public String appName;
    public Drawable appIcon;
    public String packageName;
    public long appMemory;
    public boolean isChecked;  //标记task是否被选中
    public boolean isUserTask;
}
