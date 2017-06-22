package com.example.lovespace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.home.HomeFragment;
import com.example.lovespace.login.LoginActivity;
import com.example.lovespace.login.LogoutHelper;
import com.example.lovespace.me.MeFragment;
import com.example.lovespace.session.SessionHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends UI {

    @BindView(R.id.tab_home)
    Button tab_home;
    @BindView(R.id.tab_chat)
    Button tab_chat;
    @BindView(R.id.tab_me)
    Button tab_me;

    @BindView(R.id.tab_home_tv)
    TextView tvTabHome;

    @BindView(R.id.tab_me_tv)
    TextView tvTabMe;

    FragmentManager fm;
    FragmentTransaction ft;
    HomeFragment homeFragment;
    MeFragment meFragment;

    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private String TAG = "MainActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mContext = this;
        fm = getSupportFragmentManager();

        if (homeFragment == null) homeFragment = new HomeFragment();
        if (meFragment == null) meFragment = new MeFragment();
        tab_home.performClick();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(Preferences.getOtherAccount())) {
            searchFriend();
        }
    }

    @OnClick(R.id.tab_home)
    public void selectHomeFragment(){
        FragmentTransaction f = fm.beginTransaction();
        f.replace(R.id.hold_fragment,homeFragment);
        f.show(homeFragment);
        f.commit();
        selectHomeBackground();
    }

    @OnClick(R.id.tab_chat)
    public void selectChatFragment(){
        /*FragmentTransaction f = fm.beginTransaction();
        f.hide(homeFragment);
        f.hide(meFragment);
        f.show(chatFragment);
        f.commit();*/
        String otherAccount = Preferences.getOtherAccount();
        if (TextUtils.isEmpty(otherAccount)) {
            searchFriend();
        }
        if (TextUtils.isEmpty(Preferences.getOtherAccount())){
            Toast.makeText(this, "您还没添加另一半，不能聊天！", Toast.LENGTH_SHORT).show();
        }else {
            SessionHelper.startP2PSession(this, otherAccount);
        }
    }

    @OnClick(R.id.tab_me)
    public void selectMeFragment(){
        FragmentTransaction f = fm.beginTransaction();
        f.replace(R.id.hold_fragment,meFragment);
        f.show(meFragment);
        f.commit();
        selectMeBackground();
    }

    public static void startMine(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        onParseIntent();
    }

    private void searchFriend(){
        List<String> friendAccounts = null;
        try {

            friendAccounts = NIMClient.getService(FriendService.class).getFriendAccounts();
            Log.e(TAG+"：frend", friendAccounts.toString());
            if (friendAccounts.size()>1){
                Toast.makeText(mContext, "预设情侣关系人数出错", Toast.LENGTH_SHORT).show();
            }
            if (friendAccounts.size() > 0) {
                for (String othername : friendAccounts) {
                    Preferences.saveOtherAccount(othername);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void selectHomeBackground(){
        tab_home.setBackgroundResource(R.drawable.home_tab_home_pressed);
        tab_me.setBackgroundResource(R.drawable.home_tab_mine);

        tvTabHome.setTextColor(getColors(android.R.color.holo_blue_light));
        tvTabMe.setTextColor(getColors(android.R.color.black));

    }

    private void selectMeBackground(){
        tab_home.setBackgroundResource(R.drawable.home_tab_home);
        tab_me.setBackgroundResource(R.drawable.home_tab_mine_pressed);

        tvTabHome.setTextColor(getColors(android.R.color.black));
        tvTabMe.setTextColor(getColors(android.R.color.holo_blue_light));

    }

    public void onParseIntent(){
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_APP_QUIT)){
            onLogout();
        }
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听
        LogoutHelper.logout();

        // 启动登录
        LoginActivity.startMine(this);
        finish();
    }

    public int getColors(int id){
        return getResources().getColor(id);
    }


}
