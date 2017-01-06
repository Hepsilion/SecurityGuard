package org.android.securityguard.traffic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.CorrectionInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.traffic.db.dao.TrafficDao;
import org.android.securityguard.traffic.service.TrafficMonitoringService;
import org.android.securityguard.traffic.utils.SystemInfoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrafficMonitoringActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private Button mCorrectFlowBtn;
    private TextView mTotalTV;
    private TextView mUsedTV;
    private TextView mTodayTV;

    private TrafficDao dao;
    private ImageView mRemindImgV;
    private TextView mRemindTV;
    private CorrectFlowReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_traffic_monitoring);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        boolean flag=mSharedPreferences.getBoolean("isset_operator",false);//是否设置过运营商信息
        if(!flag){
            startActivity(new Intent(TrafficMonitoringActivity.this, OperatorSetActivity.class));
            finish();
        }
        if(!SystemInfoUtils.isServiceRunning(TrafficMonitoringActivity.this, "org.android.securityguard.traffic.service.TrafficMonitoringService")){
            startService(new Intent(TrafficMonitoringActivity.this, TrafficMonitoringService.class));
        }

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.traffic_traffic_monitoring);

        ImageView mLeftImgV= (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgV.setImageResource(R.drawable.back);
        mLeftImgV.setOnClickListener(listener);

        mCorrectFlowBtn= (Button) findViewById(R.id.btn_correction_flow);
        mCorrectFlowBtn.setOnClickListener(listener);

        mTotalTV= (TextView) findViewById(R.id.tv_month_totalgprs);
        mUsedTV= (TextView) findViewById(R.id.tv_month_usedgprs);
        mTodayTV= (TextView) findViewById(R.id.tv_today_gprs);
        mRemindImgV= (ImageView) findViewById(R.id.imgv_traffic_remind);
        mRemindTV= (TextView) findViewById(R.id.tv_traffic_remind);

        receiver=new CorrectFlowReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addCategory("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);

        initData();
    }

    private void initData(){
        long totalFlow=mSharedPreferences.getLong("totalFlow", 0);
        long usedFlow=mSharedPreferences.getLong("usedFlow", 0);
        if(totalFlow>0 && usedFlow>=0){
            float scale=usedFlow/totalFlow;
            if(scale>0.9){
                mRemindImgV.setEnabled(false);
                mRemindTV.setText(R.string.traffic_traffic_to_finish);
            }else{
                mRemindImgV.setEnabled(true);
                mRemindTV.setText(R.string.traffic_traffic_enough);
            }
        }

        mTodayTV.setText("本月流量: "+Formatter.formatFileSize(TrafficMonitoringActivity.this, totalFlow));
        mUsedTV.setText("本月已用: "+Formatter.formatFileSize(TrafficMonitoringActivity.this, usedFlow));

        dao=new TrafficDao(TrafficMonitoringActivity.this);
        Date date=new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        String dataString=sdf.format(date);
        long mobileGPRS=dao.getMobileGPRS(dataString);
        if(mobileGPRS<0){
            mobileGPRS=0;
        }
        mTodayTV.setText("本日已用: "+Formatter.formatFileSize(TrafficMonitoringActivity.this, mobileGPRS));
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
        super.onDestroy();
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imgv_leftbtn:
                    finish();
                    break;
                case R.id.btn_correction_flow:
                    //判断是哪个运营商
                    int i=mSharedPreferences.getInt("operator", 0);
                    SmsManager smsManager=SmsManager.getDefault();
                    switch (i){
                        case 0://没有设置运营商
                            Toast.makeText(TrafficMonitoringActivity.this, R.string.traffic_not_set_operator, Toast.LENGTH_SHORT).show();
                            break;
                        case 1://中国移动
                            break;
                        case 2://中国联通
                            smsManager.sendTextMessage("10010",null, "LLCX", null, null);
                            break;
                        case 3://中国电信
                            break;
                    }
            }
        }
    };

    class CorrectFlowReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs= (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objs){
                SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
                String body=smsMessage.getMessageBody();
                String address=smsMessage.getOriginatingAddress();
                if(!address.equals("10010")){
                    return;
                }

                String[] split=body.split(",");
                long left=0;   //本月剩余流量
                long used=0;   //本月已用流量
                long beyond=0; //本月超出流量
                for(int i=0; i<split.length; i++){
                    if(split[i].contains("本月流量已使用")){
                        String usedFlow=split[i].substring(7, split[i].length());
                        used=getStringToFloat(usedFlow);
                    }else if(split[i].contains("剩余流量")){
                        String leftFlow=split[i].substring(4, split[i].length());
                        left=getStringToFloat(leftFlow);
                    }else if(split[i].contains("套餐外流量")){
                        String beyondFlow=split[i].substring(5, split[i].length());
                        beyond=getStringToFloat(beyondFlow);
                    }
                }

                SharedPreferences.Editor editor=mSharedPreferences.edit();
                editor.putLong("totalFlow", used+left);
                editor.putLong("usedFlow", used+beyond);
                editor.commit();

                mTotalTV.setText("本月流量: "+ Formatter.formatFileSize(context, (used+left)));
                mUsedTV.setText("本月已用: "+Formatter.formatFileSize(context, (used+beyond)));
            }
        }
    }

    private long getStringToFloat(String str){
        long flow=0;
        if(!TextUtils.isEmpty(str)){
            if(str.contains("KB")){
                String[] split=str.split("KB");
                float m=Float.parseFloat(split[0]);
                flow= (long) (m*1024);
            }else if(str.contains("MB")){
                String[] split=str.split("MB");
                float m=Float.parseFloat(split[0]);
                flow= (long) (m*1024*1024);
            }else if(str.contains("GB")){
                String[] split=str.split("MB");
                float m=Float.parseFloat(split[0]);
                flow= (long) (m*1024*1024*1024);
            }
        }
        return flow;
    }
}