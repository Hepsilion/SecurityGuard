package org.android.securityguard.process.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.android.securityguard.R;
import org.android.securityguard.app.utils.DensityUtils;
import org.android.securityguard.process.entity.TaskInfo;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/4.
 */
public class ProcessManagerAdapter extends BaseAdapter {
    private Context context;
    private List<TaskInfo> mUserTaskInfos;
    private List<TaskInfo> mSysTaskInfos;

    private SharedPreferences mSharedPreferences;

    public ProcessManagerAdapter(Context context, List<TaskInfo> mUserTaskInfos, List<TaskInfo> mSysTaskInfos){
        super();
        this.context=context;
        this.mUserTaskInfos=mUserTaskInfos;
        this.mSysTaskInfos=mSysTaskInfos;

        mSharedPreferences=context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        if(mSysTaskInfos.size()>0 && mSharedPreferences.getBoolean("showSystemProcess", true)){
            return mUserTaskInfos.size()+mSysTaskInfos.size()+2;
        }else{
            return mUserTaskInfos.size()+1;
        }
    }

    @Override
    public Object getItem(int position) {
        if(position==0 || position==mUserTaskInfos.size()+1){
            return null;
        }else if(position<=mUserTaskInfos.size()){
            return mUserTaskInfos.get(position-1);
        }else{
            return mSysTaskInfos.get(position-mUserTaskInfos.size()-2);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(position==0){
            TextView tv=getTextView();
            tv.setText("用户进程: "+mUserTaskInfos.size()+"个");
            return tv;
        }else if(position==mUserTaskInfos.size()+1){
            TextView tv=getTextView();
            if(mSysTaskInfos.size()>0){
                tv.setText("系统进程: "+mSysTaskInfos.size()+"个");
                return tv;
            }
        }

        TaskInfo taskInfo=null;
        if(position<=mUserTaskInfos.size()){
            taskInfo=mUserTaskInfos.get(position-1);
        }else if(mSysTaskInfos.size()>0){
            taskInfo=mSysTaskInfos.get(position-mUserTaskInfos.size()-2);
        }

        ViewHolder holder=null;
        if(view!=null && view instanceof RelativeLayout){
            holder= (ViewHolder) view.getTag();
        }else{
            view=View.inflate(context, R.layout.item_processmanager_list, null);
            holder=new ViewHolder();
            holder.mAppIconImgV= (ImageView) view.findViewById(R.id.imgv_appicon_processmana);
            holder.mAppMemoryTV= (TextView) view.findViewById(R.id.tv_appmemeory_processmana);
            holder.mAppNameTV= (TextView) view.findViewById(R.id.tv_appname_processmana);
            holder.mCheckBox= (CheckBox) view.findViewById(R.id.checkbox_processmana);
            view.setTag(holder);
        }

        if(taskInfo!=null){
            holder.mAppNameTV.setText(taskInfo.appName);
            holder.mAppMemoryTV.setText("占用内存: "+ Formatter.formatFileSize(context, taskInfo.appMemory));
            holder.mAppIconImgV.setImageDrawable(taskInfo.appIcon);

            if(taskInfo.packageName.equals(context.getPackageName())){
                holder.mCheckBox.setVisibility(View.GONE);
            }else{
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    private TextView getTextView(){
        TextView tv=new TextView(context);
        tv.setBackgroundColor(context.getResources().getColor(R.color.graye5));
        tv.setPadding(DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5));
        tv.setTextColor(context.getResources().getColor(R.color.black));
        return tv;
    }

    static class ViewHolder{
        ImageView mAppIconImgV;
        TextView mAppNameTV;
        TextView mAppMemoryTV;
        CheckBox mCheckBox;
    }
}
