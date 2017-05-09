package org.android.securityguard.virus.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.virus.entity.ScanAppInfo;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/4.
 */

public class ScanVirusAdapter extends BaseAdapter {
    private List<ScanAppInfo> mScanAppInfos;
    private Context context;

    public ScanVirusAdapter(List<ScanAppInfo> mScanAppInfos, Context context){
        super();
        this.mScanAppInfos=mScanAppInfos;
        this.context=context;
    }

    @Override
    public int getCount() {
        return mScanAppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            view=View.inflate(context, R.layout.item_list_applock, null);
            holder=new ViewHolder();
            holder.mAppIconImgV= (ImageView) view.findViewById(R.id.imgv_appicon);
            holder.mAppNameTV= (TextView) view.findViewById(R.id.tv_appname);
            holder.mScanIconImgV= (ImageView) view.findViewById(R.id.imgv_lock);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        ScanAppInfo scanAppInfo=mScanAppInfos.get(position);
        if(!scanAppInfo.isVirus){
            holder.mScanIconImgV.setBackgroundResource(R.drawable.blue_right_icon);
            holder.mAppNameTV.setTextColor(context.getResources().getColor(R.color.black));
            holder.mAppNameTV.setText(scanAppInfo.appName);
        }else{
            holder.mAppNameTV.setTextColor(context.getResources().getColor(R.color.bright_red));
            holder.mAppNameTV.setText(scanAppInfo.appName+"("+scanAppInfo.description+")");
        }

        holder.mAppIconImgV.setImageDrawable(scanAppInfo.appicon);
        return view;
    }

    static class ViewHolder{
        ImageView mAppIconImgV;
        TextView mAppNameTV;
        ImageView mScanIconImgV;
    }
}
