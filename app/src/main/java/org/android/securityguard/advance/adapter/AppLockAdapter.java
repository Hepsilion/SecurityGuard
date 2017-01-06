package org.android.securityguard.advance.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.advance.entity.AppInfo;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/6.
 */
public class AppLockAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> appInfos;

    public AppLockAdapter(List<AppInfo> appInfos, Context context){
        super();
        this.context=context;
        this.appInfos=appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view!=null && view instanceof RelativeLayout){
            holder= (ViewHolder) view.getTag();
        }else{
            view=View.inflate(context, R.layout.item_list_applock, null);
            holder=new ViewHolder();
            holder.mAppIconImgV= (ImageView) view.findViewById(R.id.imgv_appicon);
            holder.mAppNameTV= (TextView) view.findViewById(R.id.tv_appname);
            holder.mLockIcon= (ImageView) view.findViewById(R.id.imgv_lock);
            view.setTag(holder);
        }

        final AppInfo appInfo=appInfos.get(position);
        holder.mAppIconImgV.setImageDrawable(appInfo.icon);
        holder.mAppNameTV.setText(appInfo.appName);
        if(appInfo.isLock){
            holder.mLockIcon.setBackgroundResource(R.drawable.applock_icon);
        }else{
            holder.mLockIcon.setBackgroundResource(R.drawable.appunlock_icon);
        }
        return view;
    }

    static class ViewHolder{
        TextView mAppNameTV;
        ImageView mAppIconImgV;
        ImageView mLockIcon; //控制图片显示加锁还是不加锁
    }
}
