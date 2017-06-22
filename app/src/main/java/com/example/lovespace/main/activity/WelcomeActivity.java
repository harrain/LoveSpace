package com.example.lovespace.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lovespace.DemoCache;
import com.example.lovespace.MainActivity;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.login.LoginActivity;
import com.example.lovespace.main.model.bean.Cover;
import com.example.lovespace.main.model.bean.User;
import com.example.lovespace.main.model.dao.UserDao;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WelcomeActivity extends UI {

    private static final String TAG = "WelcomeActivity";

    @BindView(R.id.splash_cover)
    ImageView splashCover;

    private boolean customSplash = false;

    private static boolean firstEnter = true; // 是否首次进入
    private String account;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mContext = this;

        if (!TextUtils.isEmpty(Preferences.getCover())) {
            Glide.with(this).load(Preferences.getCover()).into(splashCover);
        }else {
            fetchBmob();
        }

        if (savedInstanceState != null) {
            setIntent(new Intent()); // 从堆栈恢复，不再重复解析之前的intent
        }

    }

    private void fetchBmob() {
        BmobQuery<Cover> bmobQuery = new BmobQuery<>();
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 先从缓存获取数据，如果没有，再从网络获取。
        bmobQuery.addWhereEqualTo("userid",Preferences.getUserId());
        bmobQuery.findObjects(new FindListener<Cover>() {
            @Override
            public void done(List<Cover> list, BmobException e) {
                boolean isShow = false;
                if (e==null){
                    if (list.size() > 0){
                        Preferences.saveCover(list.get(0).getCoverpath());
                        Glide.with(WelcomeActivity.this).load(list.get(0).getCoverpath()).into(splashCover);
                    }else {
                        Glide.with(WelcomeActivity.this).load(R.drawable.splash_bg).into(splashCover);
                    }
                }else {
                    isShow = true;
                    Log.e(TAG,"查询封面失败");
                }
                /*if (isShow){
                    Glide.with(WelcomeActivity.this).load(R.drawable.splash_bg).into(splashCover);
                }*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (firstEnter) {
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (canAutoLogin()) {
                        onIntent();
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            };

            getHandler().postDelayed(runnable, 1000);

        }else {
            MainActivity.startMine(WelcomeActivity.this);
            finish();
        }

    }

    private void onIntent() {
        if (TextUtils.isEmpty(DemoCache.getAccount())) {
            LoginActivity.startMine(this);
            finish();
        } else {
            /*Intent intent = getIntent();
            if (!firstEnter) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.startMine(WelcomeActivity.this);
                        finish();
                    }
                },1000);

            } else {
                load();
            }*/
            load();
        }
    }

    /**
     * 已经登录过，自动登录
     *
     * @return
     */
    private boolean canAutoLogin() {
        account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();
        Log.e(TAG,"account:"+account);
        Log.e(TAG,"token:"+token);

        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }


    private void load() {
        if (TextUtils.isEmpty(account)){
            Toast.makeText(mContext, "账户异常为空", Toast.LENGTH_SHORT).show();
            Log.e(TAG,"load: 账户异常为空");
            return;
        }
        Log.e(TAG,"load:"+account);
        UserDao.queryUserInfo(account, new OnCompleteListener<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (data.size() == 1) {
                    User user = data.get(0);
                    Log.e(TAG, user.toString());
                    saveTolocal(user);

                    searchFriend();
                    MainActivity.startMine(WelcomeActivity.this);
                    finish();
                } else {
                    Log.e(TAG, "数据源出错，有" + data.size() + "个同名昵称用户");
                }
            }

            @Override
            public void onFailure(BmobException e) {
                Log.e(TAG, "queryUserInfo:" + e.getMessage());
                if (e.getErrorCode() == 9010) {
                    Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                } else if (e.getErrorCode() == 9016) {
                    Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void saveTolocal(User user) {
        try {
            if (TextUtils.isEmpty(Preferences.getUserId())) {
                Preferences.saveUserId(user.getObjectId());
                Log.e(TAG, "uid:" + Preferences.getUserId());
            }
            if (TextUtils.isEmpty(Preferences.getCoupleId())) {
                Preferences.saveCoupleId(user.getCoupleid());
                Log.e(TAG, "cid:" + Preferences.getCoupleId());
            }
            if (TextUtils.isEmpty(Preferences.getUserSex()))
                Preferences.saveUserSex(user.getSex());
            if (TextUtils.isEmpty(Preferences.getUserBirth()))
                Preferences.saveUserBirth(user.getBirth().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchFriend() {
        if (TextUtils.isEmpty(Preferences.getOtherAccount())) {
            List<String> friendAccounts = NIMClient.getService(FriendService.class).getFriendAccounts();
            Log.e(TAG+"：frend", friendAccounts.toString());
            if (friendAccounts.size()>1){
                Toast.makeText(mContext, "预设情侣关系人数出错", Toast.LENGTH_SHORT).show();
            }
            if (friendAccounts.size() > 0) {
                for (String othername : friendAccounts) {
                    if (TextUtils.isEmpty(Preferences.getOtherAccount())){
                        Preferences.saveOtherAccount(othername);
                    }
                    break;
                }
            }
        }
    }
}
