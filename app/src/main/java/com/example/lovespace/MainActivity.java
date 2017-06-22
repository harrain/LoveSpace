package com.example.lovespace;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
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
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

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

        requestBasicPermission();

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

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CHANGE_CONFIGURATION,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("请点击确定前往应用设置打开权限,否则应用无法正常使用");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent detail_intent = new Intent();
                detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
                detail_intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(detail_intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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
