package org.android.securityguard.home;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.android.securityguard.R;
import org.android.securityguard.app.AppManagerActivity;
import org.android.securityguard.black.SecurityPhoneActivity;
import org.android.securityguard.home.adapter.HomeAdapter;
import org.android.securityguard.safe.LostFindActivity;
import org.android.securityguard.safe.dialog.EnterPasswordDialog;
import org.android.securityguard.safe.dialog.SetupPasswordDialog;
import org.android.securityguard.safe.receiver.MyDeviceAdminReceiver;
import org.android.securityguard.safe.utils.MD5;
import org.android.securityguard.virus.VirusScanActivity;

public class HomeActivity extends Activity {
    private GridView gv_home;

    private SharedPreferences mSharedPreferences; //存储手机防盗密码
    private DevicePolicyManager policyManager;    //设备管理员
    private ComponentName componentName;          //申请权限

    private long mExitTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        mSharedPreferences=getSharedPreferences("config", MODE_PRIVATE);

        gv_home= (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗
                        if(haveSetupPassword()){
                            showEnterPasswordDialog(); //弹出输入密码对话框
                        }else{
                            showSetupPasswordDialog(); //弹出设置密码对话框
                        }
                        break;
                    case 1://通讯卫士
                        startActivity(SecurityPhoneActivity.class);
                        break;
                    case 2://软件管家
                        startActivity(AppManagerActivity.class);
                        break;
                    case 3://病毒查杀
                        startActivity(VirusScanActivity.class);
                        break;
                    case 4://缓存清理
                        break;
                    case 5://进程管理
                        break;
                    case 6://流量统计
                        break;
                    case 7://高级工具
                        break;
                    case 8://设置中心
                        break;
                }
            }
        });

        //获取设备管理员
        policyManager= (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        //申请权限
        componentName=new ComponentName(this, MyDeviceAdminReceiver.class);
        boolean active=policyManager.isAdminActive(componentName);
        if(!active){
            Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.request_admin_permission);
            startActivity(intent);
        }
    }

    public void startActivity(Class<?> clazz){
        Intent intent=new Intent(HomeActivity.this, clazz);
        startActivity(intent);
    }

    /**
     * 判断用户是否设置过手机防盗密码
     * @return
     */
    private boolean haveSetupPassword(){
        String password=mSharedPreferences.getString("PhoneAntiTheftPWD", null);
        if(TextUtils.isEmpty(password)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 弹出设置密码对话框
     */
    private void showSetupPasswordDialog(){
        final SetupPasswordDialog setupPasswordDialog=new SetupPasswordDialog(HomeActivity.this);
        setupPasswordDialog.setCallback(new SetupPasswordDialog.MyCallback() {
            @Override
            public void ok() {
                String firstPwd=setupPasswordDialog.mFirstPWDET.getText().toString().trim();
                String confirmPwd=setupPasswordDialog.mConfirmET.getText().toString().trim();
                if(!TextUtils.isEmpty(firstPwd) && !TextUtils.isEmpty(confirmPwd)){
                    if(firstPwd.equals(confirmPwd)){
                        savePassword(confirmPwd);
                        setupPasswordDialog.dismiss();
                        showEnterPasswordDialog();
                    }else{
                        Toast.makeText(HomeActivity.this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                setupPasswordDialog.dismiss();
            }
        });
        setupPasswordDialog.setCancelable(true);
        setupPasswordDialog.show();
    }

    /**
     * 弹出输入密码对话框
     */
    private void showEnterPasswordDialog(){
        final String password=getPassword();
        final EnterPasswordDialog enterPasswordDialog=new EnterPasswordDialog(HomeActivity.this);
        enterPasswordDialog.setCallback(new EnterPasswordDialog.MyCallback() {
            @Override
            public void confirm() {
                if(TextUtils.isEmpty(enterPasswordDialog.mEnterPwdET.getText().toString())){
                    Toast.makeText(HomeActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
                }else if(password.equals(MD5.encode(enterPasswordDialog.mEnterPwdET.getText().toString()))){
                    enterPasswordDialog.dismiss();
                    startActivity(LostFindActivity.class);
                }else{
                    enterPasswordDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "密码有误,请重新输入!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cancle() {
                enterPasswordDialog.dismiss();
            }
        });
        enterPasswordDialog.setCancelable(true);
        enterPasswordDialog.show();
    }

    /**
     * 保存密码
     * @param password
     */
    private void savePassword(String password){
        SharedPreferences.Editor edit=mSharedPreferences.edit();
        edit.putString("PhoneAntiTheftPWD", MD5.encode(password));
        edit.commit();
    }

    /**
     * 获取密码
     * @return
     */
    private String getPassword(){
        String password=mSharedPreferences.getString("PhoneAntiTheftPWD", null);
        if(TextUtils.isEmpty(password)){
            return "";
        }else{
            return password;
        }
    }

    /**
     * 按两次返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-mExitTime<2000){
                System.exit(0);
            }else{
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime=System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}