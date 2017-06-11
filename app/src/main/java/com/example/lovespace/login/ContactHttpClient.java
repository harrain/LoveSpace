package com.example.lovespace.login;/**
 * Created by stephen on 2017/5/13.
 */

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.lovespace.DemoCache;
import com.example.lovespace.common.util.CheckSumBuilder;
import com.example.lovespace.config.DemoServers;
import com.example.lovespace.config.preference.Preferences;
import com.netease.nim.uikit.common.http.NimHttpClient;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.string.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名： com.example.lovespace.login
 * 类名:  ContactHttpClient
 * 类功能：注册账号到webserver,通讯录数据获取协议的实现
 * 创建者：  stephen
 * 创建日期: 2017/5/13
 */
public class ContactHttpClient {

    private static final String TAG = "ContactHttpClient";

    // code
    private static final int RESULT_CODE_SUCCESS = 200;

    // api
    //private static final String API_NAME_REGISTER = "createDemoUser";
    private static final String API_NAME_REGISTER = "user/create.action";
    // header
    private static final String HEADER_KEY_APP_KEY = "AppKey";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_USER_AGENT = "User-Agent";
    private static final String HEADER_NONCE = "Nonce";
    private static final String HEADER_CURTIME = "CurTime";
    private static final String HEADER_CHECKSUM = "CheckSum";


    // request
    //private static final String REQUEST_USER_NAME = "username";
    private static final String REQUEST_USER_NAME = "accid";
    //private static final String REQUEST_NICK_NAME = "nickname";
    private static final String REQUEST_NICK_NAME = "name";
    //private static final String REQUEST_PASSWORD = "password";
    private static final String REQUEST_PASSWORD = "token";

    // result
    //private static final String RESULT_KEY_RES = "res";
    private static final String RESULT_KEY_RES = "code";
    private static final String RESULT_KEY_ERROR_MSG = "errmsg";


    public interface ContactHttpCallback<T> {
        void onSuccess(T t);

        void onFailed(int code, String errorMsg);
    }

    private static ContactHttpClient instance;

    public static synchronized ContactHttpClient getInstance() {
        if (instance == null) {
            instance = new ContactHttpClient();
        }

        return instance;
    }

    private ContactHttpClient() {
        NimHttpClient.getInstance().init(DemoCache.getContext());
    }


    /**
     * 向应用服务器创建账号（注册账号）
     * 由应用服务器调用WEB SDK接口将新注册的用户数据同步到云信服务器
     */
    public void register(String account, String nickName, String password, final ContactHttpCallback<Void> callback) {
        String url = DemoServers.apiServer() + API_NAME_REGISTER;
        password = MD5.getStringMD5(password);
        try {
            nickName = URLEncoder.encode(nickName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>(1);
        String appKey = readAppKey();

        String appSecret = "2ff76972ff11";
        String nonce =  "12345";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);

        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
        //headers.put(HEADER_USER_AGENT, "nim_demo_android");
        headers.put(HEADER_KEY_APP_KEY, appKey);
        headers.put(HEADER_NONCE,nonce);
        headers.put(HEADER_CURTIME,curTime);
        headers.put(HEADER_CHECKSUM,checkSum);


        StringBuilder body = new StringBuilder();
        body.append(REQUEST_USER_NAME).append("=").append(account.toLowerCase()).append("&")
                .append(REQUEST_NICK_NAME).append("=").append(nickName).append("&")
                .append(REQUEST_PASSWORD).append("=").append(password);
        String bodyString = body.toString();

        NimHttpClient.getInstance().execute(url, headers, bodyString, new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "register failed : response = " + response + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    if (callback != null) {
                        callback.onFailed(code, exception != null ? exception.getMessage() : "null");
                    }
                    return;
                }
                LogUtil.e(TAG, "register: response = " + response + ", errorMsg = "
                        + (exception != null ? exception.getMessage() : "null"));
                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    JSONObject obj = resObj.getJSONObject("info");
                    String token = obj.getString("token");
                    Preferences.saveUserToken(token);
                    int resCode = resObj.getIntValue(RESULT_KEY_RES);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        callback.onSuccess(null);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                }
            }
        });
    }

    private String readAppKey() {
        try {
            ApplicationInfo appInfo = DemoCache.getContext().getPackageManager()
                    .getApplicationInfo(DemoCache.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
