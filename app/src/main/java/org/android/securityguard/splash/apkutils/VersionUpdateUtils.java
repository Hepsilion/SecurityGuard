package org.android.securityguard.splash.apkutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

import org.android.securityguard.splash.Constants;
import org.android.securityguard.R;
import org.android.securityguard.home.HomeActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Hepsilion on 2017/1/1.
 */

public class VersionUpdateUtils {
    private String mVersion;   //本地版本号
    private Activity context;
    private VersionEntity versionEntity;
    private ProgressDialog mProgressDialog;

    public VersionUpdateUtils(String version, Activity activity){
        this.mVersion=version;
        this.context=activity;
    }

    private static final int MESSAGE_NET_ERROR=101;
    private static final int MESSAGE_IO_ERROR=102;
    private static final int MESSAGE_JSON_ERROR=103;
    private static final int MESSAGE_SHOW_DIALOG=104;
    private static final int MESSAGE_ENTERHOME=105;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_IO_ERROR:
                    Toast.makeText(context, "IO异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_JSON_ERROR:
                    Toast.makeText(context, "Json解析异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_NET_ERROR:
                    Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MESSAGE_SHOW_DIALOG:
                    showUpdateDialog(versionEntity);
                    break;
                case MESSAGE_ENTERHOME:
                    Intent intent=new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                    context.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 获取服务器版本号
     */
    public void getCloudVersion(){
        try{
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000); //连接超时
            HttpConnectionParams.setSoTimeout(client.getParams(), 5000);         //请求超时
            HttpGet httpGet=new HttpGet(Constants.APK_SERVER_IP+Constants.APK_UPDATE_HTML);
            HttpResponse execute=client.execute(httpGet);

            //请求和响应成功
            if(execute.getStatusLine().getStatusCode()==200){
                HttpEntity entity=execute.getEntity();
                String result= EntityUtils.toString(entity, "gbk");

                JSONObject jsonObject=new JSONObject(result);
                String code=jsonObject.getString("code");
                String desc=jsonObject.getString("desc");
                String apkurl=jsonObject.getString("apkurl");

                versionEntity=new VersionEntity();
                versionEntity.versionCode=code;
                versionEntity.description=desc;
                versionEntity.apkurl=apkurl;

                if(!mVersion.equals(versionEntity.versionCode)) {
                    //版本不一致
                    handler.sendEmptyMessage(MESSAGE_SHOW_DIALOG);
                }
            }
        } catch(ClientProtocolException e){
            handler.sendEmptyMessage(MESSAGE_NET_ERROR);
            e.printStackTrace();
        } catch (IOException e) {
            handler.sendEmptyMessage(MESSAGE_IO_ERROR);
            e.printStackTrace();
        } catch(JSONException e) {
            handler.sendEmptyMessage(MESSAGE_JSON_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * 弹出更新提示对话框
     * @param versionEntity
     */
    private void showUpdateDialog(final VersionEntity versionEntity){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("检测到新版本："+versionEntity.versionCode);
        builder.setMessage(versionEntity.description);
        builder.setCancelable(false);   //设置不能点击手机返回按钮隐藏对话框
        builder.setIcon(R.drawable.ic_launcher);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                initProgressDialog();
                downloadNewApk(versionEntity.apkurl);
            }
        });
        builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 初始化进度条对话框
     */
    private void initProgressDialog(){
        mProgressDialog=new ProgressDialog(context);
        mProgressDialog.setMessage("准备下载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }

    /**
     * 下载新版本
     * @param url
     */
    protected void downloadNewApk(String url){
        ApkDownloader downloader=new ApkDownloader();
        downloader.download(url, Constants.APK_LOCAL_FILE, new ApkDownloader.MyCallBack() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                mProgressDialog.dismiss();
                ApkUtils.installApk(context);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mProgressDialog.setMessage("下载失败");
                mProgressDialog.dismiss();
                enterHome();
            }

            @Override
            public void onLoadding(long total, long current, boolean isUploading) {
                mProgressDialog.setMax((int) total);
                mProgressDialog.setMessage("正在下载...");
                mProgressDialog.setProgress((int) current);
            }
        });
    }

    private void enterHome(){
        handler.sendEmptyMessageDelayed(MESSAGE_ENTERHOME, 2000);
    }
}