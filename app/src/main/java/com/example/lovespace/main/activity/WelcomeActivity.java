package com.example.lovespace.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.lovespace.DemoCache;
import com.example.lovespace.MainActivity;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.login.LoginActivity;
import com.netease.nim.uikit.common.activity.UI;

public class WelcomeActivity extends UI {

    private static final String TAG = "WelcomeActivity";

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

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
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }
}
