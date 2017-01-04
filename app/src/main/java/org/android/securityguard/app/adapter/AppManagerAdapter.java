package org.android.securityguard.app.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.app.entity.AppInfo;
import org.android.securityguard.app.utils.DensityUtils;
import org.android.securityguard.app.utils.EngineUtils;

import java.util.List;

/**
 * Created by Hepsilion on 2017/1/3.
 */
public class AppManagerAdapter extends BaseAdapter {
    private List<AppInfo> userAppInfos;
    private List<AppInfo> sysAppInfos;
    private Context context;

    public AppManagerAdapter(List<AppInfo> userAppInfos, List<AppInfo> sysAppInfos, Context context){
        this.userAppInfos=userAppInfos;
        this.sysAppInfos=sysAppInfos;
        this.context=context;
    }

    @Override
    public int getCount() {
        return userAppInfos.size()+sysAppInfos.size()+2;
    }

    @Override
    public Object getItem(int position) {
        if(position==0){
            return null;  //第0个位置显示的是用户程序个数的标签
        }else if(position==userAppInfos.size()+1){
            return null;
        }

        AppInfo appInfo;
        if(position< userAppInfos.size()+1){ //用户程序
            appInfo=userAppInfos.get(position-1); //多了一个textview标签，位置-1
        }else{  //系统程序
            int location=position-userAppInfos.size()-2;
            appInfo=sysAppInfos.get(location);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            TextView tv=getTextView();
            tv.setText("用户程序: "+userAppInfos.size()+"个");
            return tv;
        }else if(position==userAppInfos.size()+1){
            TextView tv=getTextView();
            tv.setText("系统程序: "+sysAppInfos.size()+"个");
            return tv;
        }

        AppInfo appInfo;
        if(position<userAppInfos.size()+1){
            appInfo=userAppInfos.get(position-1);
        }else{
            appInfo=sysAppInfos.get(position-userAppInfos.size()-2);
        }

        ViewHolder viewHolder=null;
        if(convertView!=null && convertView instanceof LinearLayout){
            viewHolder= (ViewHolder) convertView.getTag();
        }else{
            viewHolder=new ViewHolder();
            convertView=View.inflate(context, R.layout.item_appmanager_list, null);
            viewHolder.mAppIconImgV= (ImageView) convertView.findViewById(R.id.imgv_appicon);
            viewHolder.mAppLoctionTV= (TextView) convertView.findViewById(R.id.tv_appisroom);
            viewHolder.mAppSizeTV= (TextView) convertView.findViewById(R.id.tv_appsize);
            viewHolder.mAppNameTV= (TextView) convertView.findViewById(R.id.tv_appname);
            viewHolder.mLaunchAppTV= (TextView) convertView.findViewById(R.id.tv_launch_app);
            viewHolder.mSettingAppTV= (TextView) convertView.findViewById(R.id.tv_setting_app);
            viewHolder.mShareAppTV= (TextView) convertView.findViewById(R.id.tv_share_app);
            viewHolder.mUninstallTV= (TextView) convertView.findViewById(R.id.tv_uninstall_app);
            viewHolder.mAppOptionLL= (LinearLayout) convertView.findViewById(R.id.l1_option_app);
            convertView.setTag(viewHolder);
        }

        if(appInfo!=null){
            viewHolder.mAppLoctionTV.setText(appInfo.getAppLocation(appInfo.isInRoom));
            viewHolder.mAppIconImgV.setImageDrawable(appInfo.icon);
            viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context, appInfo.appSize));
            viewHolder.mAppNameTV.setText(appInfo.appName);
            if(appInfo.isSelected){
                viewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
        }

        MyClickListener listener=new MyClickListener(appInfo);
        viewHolder.mLaunchAppTV.setOnClickListener(listener);
        viewHolder.mSettingAppTV.setOnClickListener(listener);
        viewHolder.mShareAppTV.setOnClickListener(listener);
        viewHolder.mUninstallTV.setOnClickListener(listener);

        return convertView;
    }

    /**
     * 创建一个TextView
     * @return
     */
    private TextView getTextView(){
        TextView tv=new TextView(context);
        tv.setBackgroundColor(context.getResources().getColor(R.color.graye5));
        tv.setPadding(DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5), DensityUtils.dip2px(context, 5));
        tv.setTextColor(context.getResources().getColor(R.color.black));
        return tv;
    }

    static class ViewHolder{
        TextView mLaunchAppTV;    //启动App
        TextView mUninstallTV;    //卸载App
        TextView mShareAppTV;     //分享App
        TextView mSettingAppTV;   //设置App
        ImageView mAppIconImgV;   //App图标
        TextView mAppLoctionTV;   //App位置
        TextView mAppSizeTV;      //App大小
        TextView mAppNameTV;      //App名称
        LinearLayout mAppOptionLL;//操作App的线性布局
    }

    class MyClickListener implements View.OnClickListener{
        private AppInfo appInfo;

        public MyClickListener(AppInfo appInfo){
            super();
            this.appInfo=appInfo;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_launch_app:
                    EngineUtils.startApplication(context, appInfo);
                    break;
                case R.id.tv_share_app:
                    EngineUtils.shareApplication(context, appInfo);
                    break;
                case R.id.tv_setting_app:
                    EngineUtils.setAppDetail(context, appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    //卸载应用，需要注册广播接收者
                    if(appInfo.packageName.equals(context.getPackageName())){
                        Toast.makeText(context, R.string.app_manager_app_uninstall_no_permission, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.uninstallApplication(context, appInfo);
                    break;
            }
        }
    }
}
