package org.android.securityguard.splash.apkutils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by Hepsilion on 2017/1/1.
 */

/**
 * Apk下载器
 */
public class ApkDownloader {
    public void download(String url, String localTargetFile, final MyCallBack callBack){
        HttpUtils httpUtils=new HttpUtils();
        // 使用HttpUtils下载方法下载指定文件
        httpUtils.download(url, localTargetFile, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                callBack.onSuccess(responseInfo);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                callBack.onFailure(error, msg);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                callBack.onLoadding(total, current, isUploading);
            }
        });
    }

    /**
     * 回调接口：用于监听文件的下载状态
     */
    interface MyCallBack{
        /**下载成功时调用*/
        void onSuccess(ResponseInfo<File> responseInfo);
        /**下载失败时调用*/
        void onFailure(HttpException error, String msg);
        /**下载过程中调用*/
        void onLoadding(long total, long current, boolean isUploading);
    }
}
