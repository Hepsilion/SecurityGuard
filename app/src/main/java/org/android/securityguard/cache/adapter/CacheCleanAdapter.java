package org.android.securityguard.cache.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.cache.entity.CacheInfo;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/4.
 */
public class CacheCleanAdapter extends BaseAdapter {
    private Context context;
    private List<CacheInfo> cacheInfos;

    public CacheCleanAdapter(Context context, List<CacheInfo> cacheInfos){
        super();
        this.context=context;
        this.cacheInfos=cacheInfos;
    }

    @Override
    public int getCount() {
        return cacheInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return cacheInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if(view==null){
            holder=new ViewHolder();
            view=View.inflate(context, R.layout.item_cacheclean_list, null);
            holder.mAppIconImgV= (ImageView) view.findViewById(R.id.imgv_appicon_cacheclean);
            holder.mAppNameTV= (TextView) view.findViewById(R.id.tv_appname_cacheclean);
            holder.mCacheSizeTV= (TextView) view.findViewById(R.id.tv_appsize_cacheclean);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        CacheInfo cacheInfo=cacheInfos.get(position);
        holder.mAppIconImgV.setImageDrawable(cacheInfo.appIcon);
        holder.mAppNameTV.setText(cacheInfo.appName);
        holder.mCacheSizeTV.setText(Formatter.formatFileSize(context, cacheInfo.cacheSize));

        return view;
    }

    static class ViewHolder{
        ImageView mAppIconImgV;
        TextView mAppNameTV;
        TextView mCacheSizeTV;
    }
}
