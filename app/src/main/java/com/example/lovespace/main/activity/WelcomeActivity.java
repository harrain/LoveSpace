package com.example.lovespace.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.lovespace.DemoCache;
import com.example.lovespace.MainActivity;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.login.LoginActivity;
import com.example.lovespace.main.model.bean.User;
import com.example.lovespace.main.model.dao.UserDao;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.List;

import cn.bmob.v3.exception.BmobException;

public class WelcomeActivity extends UI {

    private static final String TAG = "WelcomeActivity";

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入
    private String account;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        if (savedInstanceState != null) {
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }
        if (!firstEnter) {
            onIntent();
        } else {
            customSplash = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstEnter){
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(canAutoLogin()){
                        onIntent();
                    }else {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };
            if (customSplash){
                getHandler().postDelayed(runnable,1000);
            }else {
                runnable.run();
            }
        }

    }

    private void onIntent() {
        if (TextUtils.isEmpty(DemoCache.getAccount())){
            LoginActivity.startMine(this);
            finish();
        }else{
            Intent intent = getIntent();
            if (!firstEnter && intent == null){

                finish();
            }else {
                load();
                searchFriend();
                MainActivity.startMine(WelcomeActivity.this);
                finish();
            }
        }
    }

    /**
     * 已经登录过，自动登录
     * @return
     */
    private boolean canAutoLogin() {
        account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }

    private void load(){
        UserDao.queryUserInfo(account, new OnCompleteListener<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (data.size() == 1){
                    User user = data.get(0);
                    Log.e(TAG,user.toString());
                    saveTolocal(user);
                }else {
                    Log.e(TAG,"数据源出错，有"+data.size()+"个同名昵称用户");
                }
            }

            @Override
            public void onFailure(BmobException e) {
                Log.e(TAG,"queryUserInfo:"+e.getMessage());
                if (e.getErrorCode() == 9010){
                    Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                }else if (e.getErrorCode() == 9016){
                    Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void saveTolocal(User user) {
        try {
            if (Preferences.getUserId() == null)
                Preferences.saveUserId(user.getObjectId());
            if (Preferences.getUserSex() == null)
                Preferences.saveUserSex(user.getSex());
            if (Preferences.getCoupleId() == null)
                Preferences.saveCoupleId(user.getCoupleid());
            if (Preferences.getUserBirth() == null)
                Preferences.saveUserBirth(user.getBirth().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void searchFriend(){
        if (TextUtils.isEmpty(Preferences.getOtherAccount())) {
            List<String> friendAccounts = NIMClient.getService(FriendService.class).getFriendAccounts();
            Log.e("frend", friendAccounts.toString());
            if (friendAccounts.size() > 0) {
                for (String othername : friendAccounts) {
                    Preferences.saveOtherAccount(othername);
                    break;
                }
            }
        }
    }
}
