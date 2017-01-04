package org.android.securityguard.process;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.process.adapter.ProcessManagerAdapter;
import org.android.securityguard.process.entity.TaskInfo;
import org.android.securityguard.process.utils.SystemInfoUtils;
import org.android.securityguard.process.utils.TaskInfoParser;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProcessManagerActivity extends AppCompatActivity {
    private TextView mRunProcessNum;
    private TextView mMemoryTV;
    private TextView mProcessNumTV;
    private ListView mListView;

    ProcessManagerAdapter adapter;

    private List<TaskInfo> runningTaskInfos;
    private List<TaskInfo> userTaskInfos=new ArrayList<TaskInfo>();
    private List<TaskInfo> sysTaskInfos=new ArrayList<TaskInfo>();

    private ActivityManager manager;

    private int runningProcessCount;
    private long totalMen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_process_manager);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.bright_green));
        ((TextView)findViewById(R.id.tv_title)).setText("进程管理");

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        ImageView mRightImgV= (ImageView) findViewById(R.id.imgv_rightbtn);
        mRightImgV.setImageResource(R.drawable.processmanager_setting_icon);
        mRightImgV.setOnClickListener(listener);

        mProcessNumTV= (TextView) findViewById(R.id.tv_user_runningprocess);

        runningProcessCount= SystemInfoUtils.getRunningProcessCount(ProcessManagerActivity.this);
        mRunProcessNum= (TextView) findViewById(R.id.tv_runningprocess_num);
        mRunProcessNum.setText("运行中的进程: "+runningProcessCount+"个");

        long totalAvailMem=SystemInfoUtils.getAvailMem(ProcessManagerActivity.this);
        totalMen=SystemInfoUtils.getTotalMem();
        mMemoryTV= (TextView) findViewById(R.id.tv_memory_processmanager);
        mMemoryTV.setText("可用/总内存: "+Formatter.formatFileSize(ProcessManagerActivity.this, totalAvailMem)+"/"+Formatter.formatFileSize(ProcessManagerActivity.this, totalMen));



        findViewById(R.id.btn_selectall).setOnClickListener(listener);
        findViewById(R.id.btn_select_inverse).setOnClickListener(listener);
        findViewById(R.id.btn_cleanprocess).setOnClickListener(listener);

        mListView= (ListView) findViewById(R.id.lv_runningapps);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object object=mListView.getItemAtPosition(position);
                if(object!=null && object instanceof TaskInfo){
                    TaskInfo taskInfo= (TaskInfo) object;
                    if(taskInfo.packageName.equals(getPackageName())){//当前点击的条目是本应用程序
                        return;
                    }
                    taskInfo.isChecked=!taskInfo.isChecked;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem>=userTaskInfos.size()+1){
                    mProcessNumTV.setText("系统进程: "+sysTaskInfos.size()+"个");
                }else{
                    mProcessNumTV.setText("用户进程: "+userTaskInfos.size()+"个");
                }
            }
        });

        fillData();
    }

    @Override
    protected void onResume() {
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    private void fillData(){
        userTaskInfos.clear();
        sysTaskInfos.clear();

        new Thread(){
            @Override
            public void run() {
                runningTaskInfos= TaskInfoParser.getRunningTaskInfos(getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(TaskInfo taskInfo:runningTaskInfos){
                            if(taskInfo.isUserTask){
                                userTaskInfos.add(taskInfo);
                            }else{
                                sysTaskInfos.add(taskInfo);
                            }
                        }

                        if(adapter==null){
                            adapter=new ProcessManagerAdapter(getApplicationContext(), userTaskInfos, sysTaskInfos);
                            mListView.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }

                        if(userTaskInfos.size()>0){
                            mProcessNumTV.setText("用户进程: "+userTaskInfos.size()+"个");
                        }else{
                            mProcessNumTV.setText("系统进程: "+sysTaskInfos.size()+"个");
                        }
                    }
                });
            }
        }.start();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.imgv_rightbtn:
                    startActivity(new Intent(ProcessManagerActivity.this, ProcessManagerSettingActivity.class));
                    break;
                case R.id.btn_selectall:
                    selectAll();
                    break;
                case R.id.btn_select_inverse:
                    inverse();
                    break;
                case R.id.btn_cleanprocess:
                    cleanProcess();
                    break;
            }
        }
    };

    /**
     * 全选
     */
    private void selectAll(){
        for(TaskInfo taskInfo:userTaskInfos){
            if(taskInfo.packageName.equals(getPackageName())){
                continue;
            }
            taskInfo.isChecked=true;
        }
        for(TaskInfo taskInfo:sysTaskInfos){
            taskInfo.isChecked=true;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选
     */
    private void inverse(){
        for(TaskInfo taskInfo:userTaskInfos){
            if(taskInfo.packageName.equals(getPackageName())){
                continue;
            }
            boolean checked=taskInfo.isChecked;
            taskInfo.isChecked=!checked;
        }
        for(TaskInfo taskInfo:sysTaskInfos){
            boolean checked=taskInfo.isChecked;
            taskInfo.isChecked=!checked;
        }
        adapter.notifyDataSetChanged();
    }

    private void cleanProcess(){
        manager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);


        long saveMemory=0;

        int count=0;
        List<TaskInfo> killedTaskInfos=new ArrayList<TaskInfo>();
        for(TaskInfo taskInfo:userTaskInfos){
            if(taskInfo.isChecked){
                count++;
                saveMemory+=taskInfo.appMemory;
                manager.killBackgroundProcesses(taskInfo.packageName);
                killedTaskInfos.add(taskInfo);
            }
        }
        for(TaskInfo taskInfo:sysTaskInfos){
            if(taskInfo.isChecked){
                count++;
                saveMemory+=taskInfo.appMemory;
                manager.killBackgroundProcesses(taskInfo.packageName);
                killedTaskInfos.add(taskInfo);
            }
        }

        for(TaskInfo taskInfo:killedTaskInfos){
            if(taskInfo.isUserTask){
                userTaskInfos.remove(taskInfo);
            }else{
                sysTaskInfos.remove(taskInfo);
            }
        }
        runningProcessCount-=count;
        mRunProcessNum.setText("运行中的进程: "+runningProcessCount+"个");
        mMemoryTV.setText("可用/总内存: "+ Formatter.formatFileSize(ProcessManagerActivity.this, SystemInfoUtils.getAvailMem(ProcessManagerActivity.this))+"/"+Formatter.formatFileSize(ProcessManagerActivity.this, totalMen));
        Toast.makeText(ProcessManagerActivity.this, "清理了"+count+"个进程,释放了"+Formatter.formatFileSize(ProcessManagerActivity.this, saveMemory)+"内存", Toast.LENGTH_LONG).show();
        mProcessNumTV.setText("用户进程"+userTaskInfos.size()+"个");
        adapter.notifyDataSetChanged();
    }
}
