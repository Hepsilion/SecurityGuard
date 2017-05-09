package org.android.securityguard.black.entity;

/**
 * Created by Hepsilion on 2017/1/2.
 */

public class BlackContactInfo {
    /**黑名单号码*/
    public String phoneNumber;
    /**黑名单联系人名称*/
    public String contactName;
    /**黑名单拦截模式<br>：1.电话拦截，2.短信拦截，3.电话、短信都拦截*/
    public int mode;

    public String getModeString(int mode){
        switch (mode){
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话、短信拦截";
        }
        return "";
    }
}
