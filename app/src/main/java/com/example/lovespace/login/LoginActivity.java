
package com.example.lovespace.login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.DemoCache;
import com.example.lovespace.MainActivity;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.config.preference.UserPreferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.User;
import com.example.lovespace.main.model.dao.UserDao;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.model.ToolBarOptions;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.FriendService;

import java.util.List;

import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;


public class LoginActivity extends UI implements View.OnKeyListener{

    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

    private TextView rightTopBtn;  // ActionBar完成按钮
    private TextView switchModeBtn;  // 注册/登录切换按钮

    private ClearableEditTextWithIcon loginAccountEdit;
    private ClearableEditTextWithIcon loginPasswordEdit;

    private ClearableEditTextWithIcon registerAccountEdit;
    private ClearableEditTextWithIcon registerNickNameEdit;
    private ClearableEditTextWithIcon registerPasswordEdit;

    private View loginLayout;
    private View registerLayout;

    private AbortableFuture<LoginInfo> loginRequest;
    private boolean registerMode = false; // 注册模式
    private boolean registerPanelInited = false; // 注册面板是否初始化

    private String TAG = "Login";
    private String account;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//设置第四步中的View
        ToolBarOptions options = new ToolBarOptions();
        options.isNeedNavigate = false;
        options.logoId = R.drawable.actionbar_white_logo_space;
        setToolBar(R.id.toolbar, options);
        mContext = this;
        requestBasicPermission();

        initRightTopBtn();
        setupLoginPanel();
        setupRegisterPanel();


    }

    /**
     * 基本权限管理
     */

    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private void requestBasicPermission() {
        MPermission.with(LoginActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * ActionBar 右上角按钮
     */
    private void initRightTopBtn() {
        rightTopBtn = addRegisterRightTopBtn(this, R.string.login);
        rightTopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (registerMode) {
                    register();
                } else {
                    //fakeLoginTest(); // 假登录代码示例
                    login();
                }
            }
        });
    }

    /**
     * 登录面板
     */
    private void setupLoginPanel() {
        loginAccountEdit = findView(R.id.edit_login_account);
        loginPasswordEdit = findView(R.id.edit_login_password);

        loginAccountEdit.setIconResource(R.drawable.user_account_icon);
        loginPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);

        loginAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
        loginAccountEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.addTextChangedListener(textWatcher);
        loginPasswordEdit.setOnKeyListener(this);

        String account1 = Preferences.getUserAccount();
        loginAccountEdit.setText(account1);
    }

    /**
     * 注册面板
     */
    private void setupRegisterPanel() {
        loginLayout = findView(R.id.login_layout);
        registerLayout = findView(R.id.register_layout);
        switchModeBtn = findView(R.id.register_login_tip);

        switchModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 更新右上角按钮状态
            if (!registerMode) {
                // 登录模式
                boolean isEnable = loginAccountEdit.getText().length() > 0
                        && loginPasswordEdit.getText().length() > 0;
                updateRightTopBtn(LoginActivity.this, rightTopBtn, isEnable);
            }
        }
    };

    private void updateRightTopBtn(Context context, TextView rightTopBtn, boolean isEnable) {
        rightTopBtn.setText(R.string.done);
        rightTopBtn.setBackgroundResource(R.drawable.g_white_btn_selector);
        rightTopBtn.setEnabled(isEnable);
        rightTopBtn.setTextColor(context.getResources().getColor(R.color.color_blue_0888ff));
        rightTopBtn.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
    }

    /**
     * ***************************************** 登录 **************************************
     */

    private void login() {
        DialogMaker.showProgressDialog(this, null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (loginRequest != null) {
                    loginRequest.abort();
                    onLoginDone();
                }
            }
        }).setCanceledOnTouchOutside(false);


        account = loginAccountEdit.getEditableText().toString().toLowerCase();
        final String password = loginPasswordEdit.getEditableText().toString();
        UserDao.obtainTokenForLogin(account, password, new SQLQueryListener<User>() {
            @Override
            public void done(BmobQueryResult<User> result, BmobException e) {
                if(e == null){
                    if (result == null || result.getResults() == null || result.getResults().size() == 0){
                        DialogMaker.dismissProgressDialog();
                        Toast.makeText(mContext, "用户名或者密码错误！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<User> list = result.getResults();
                    if (list.size()>0){
                        User user = list.get(0);
                        Log.e(TAG,user.toString());
                        saveTolocal(user);
                        String token = user.getToken();
                        if (token!=null) {
                            loginToNim(account, token);
                        }else {
                            loginToNim(account,password);
                        }
                    }

                }else {
                    if (e.getErrorCode() == 9010){
                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                    }else if (e.getErrorCode() == 9016){
                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG,"obtainTokenForLogin:"+e.getMessage());
                }
            }
        });
        // 登录

    }

    private void loginToNim(final String acc, final String token){
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。

        loginRequest = NimUIKit.doLogin(new LoginInfo(acc, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");

                DemoCache.setAccount(acc);
                saveLoginInfo(acc, token);
                /*load();*/
                searchFriend();

                onLoginDone();
                // 初始化消息提醒配置
                initNotificationConfig();

                // 进入主界面
                MainActivity.start(LoginActivity.this, null);
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void load(){
        UserDao.queryUserInfo(account, new OnCompleteListener<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                if (data.size() == 1){
                    User user = data.get(0);
                    Log.e(TAG,user.toString());
                    saveTolocal(user);
                    searchFriend();

                    onLoginDone();
                    // 初始化消息提醒配置
                    initNotificationConfig();

                    // 进入主界面
                    MainActivity.start(LoginActivity.this, null);
                    finish();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void searchFriend(){
        if (TextUtils.isEmpty(Preferences.getOtherAccount())) {
            List<String> friendAccounts = NIMClient.getService(FriendService.class).getFriendAccounts();
            Log.e(TAG,":frend"+friendAccounts.toString());
            if (friendAccounts.size()>1){
                Toast.makeText(mContext, "预设情侣关系人数出错", Toast.LENGTH_SHORT).show();
            }
            if (friendAccounts.size() > 0) {
                for (String othername : friendAccounts) {
                    if(TextUtils.isEmpty(Preferences.getOtherAccount())) {
                        Preferences.saveOtherAccount(othername);
                    }
                    break;
                }
            }
        }
    }

    /*private void login(){
        final String name = account.getText().toString().toLowerCase();//获取edittext上用户输入的account
        final String pwd = passwd.getText().toString();//获取edittext上用户输入的pwd
        LoginInfo info = new LoginInfo(name,pwd); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        DemoCache.setAccount(name);
                        saveLoginInfo(name,pwd);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailed(int code) {
                        Toast.makeText(LoginActivity.this,"登录失败: "+code,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);//进行登录,实现登录的回调
    }*/


    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    public static void startMine(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }


    /**
     * ***************************************** 注册 **************************************
     */

    private void register() {
        if (!registerMode || !registerPanelInited) {
            return;
        }

        if (!checkRegisterContentValid()) {
            return;
        }

        if (!NetworkUtil.isNetAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        DialogMaker.showProgressDialog(this, getString(R.string.registering), false);

        // 注册流程
        final String account = registerAccountEdit.getText().toString();
        final String nickName = registerNickNameEdit.getText().toString();
        final String password = registerPasswordEdit.getText().toString();
//修改了NimUIKit的默认登录方法，因为其中的接口都是demo的。修改appkey要修改URL和传入的header
        ContactHttpClient.getInstance().register(account, nickName, password, new ContactHttpClient.ContactHttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                UserDao.addToBomb(account, nickName, password, null, Preferences.getUserToken(), new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String objectId) {
                        Toast.makeText(LoginActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                        switchMode();  // 切换回登录
                        loginAccountEdit.setText(account);
                        loginPasswordEdit.setText(password);

                        registerAccountEdit.setText("");
                        registerNickNameEdit.setText("");
                        registerPasswordEdit.setText("");

                        DialogMaker.dismissProgressDialog();
                    }

                    @Override
                    public void onFailure(BmobException e) {
                        if (e.getErrorCode() == 9010){
                            Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                        }else if (e.getErrorCode() == 9016){
                            Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(LoginActivity.this, getString(R.string.register_failed, String.valueOf(code), errorMsg), Toast.LENGTH_SHORT)
                        .show();

                DialogMaker.dismissProgressDialog();
            }
        });
    }

    private boolean checkRegisterContentValid() {
        if (!registerMode || !registerPanelInited) {
            return false;
        }

        // 帐号检查
        String account = registerAccountEdit.getText().toString().trim();
        if (account.length() <= 0 || account.length() > 20) {
            Toast.makeText(this, R.string.register_account_tip, Toast.LENGTH_SHORT).show();

            return false;
        }

        // 昵称检查
        String nick = registerNickNameEdit.getText().toString().trim();
        if (nick.length() <= 0 || nick.length() > 10) {
            Toast.makeText(this, R.string.register_nick_name_tip, Toast.LENGTH_SHORT).show();

            return false;
        }

        // 密码检查
        String password = registerPasswordEdit.getText().toString().trim();
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(this, R.string.register_password_tip, Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    /**
     * ***************************************** 注册/登录切换 **************************************
     */
    private void switchMode() {
        registerMode = !registerMode;

        if (registerMode && !registerPanelInited) {
            registerAccountEdit = findView(R.id.edit_register_account);
            registerNickNameEdit = findView(R.id.edit_register_nickname);
            registerPasswordEdit = findView(R.id.edit_register_password);

            registerAccountEdit.setIconResource(R.drawable.user_account_icon);
            registerNickNameEdit.setIconResource(R.drawable.nick_name_icon);
            registerPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);

            registerAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            registerNickNameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            registerPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

            registerAccountEdit.addTextChangedListener(textWatcher);
            registerNickNameEdit.addTextChangedListener(textWatcher);
            registerPasswordEdit.addTextChangedListener(textWatcher);

            registerPanelInited = true;
        }

        setTitle(registerMode ? R.string.register : R.string.login);
        loginLayout.setVisibility(registerMode ? View.GONE : View.VISIBLE);
        registerLayout.setVisibility(registerMode ? View.VISIBLE : View.GONE);
        switchModeBtn.setText(registerMode ? R.string.login_has_account : R.string.register);
        if (registerMode) {
            rightTopBtn.setEnabled(true);
        } else {
            boolean isEnable = loginAccountEdit.getText().length() > 0
                    && loginPasswordEdit.getText().length() > 0;
            rightTopBtn.setEnabled(isEnable);
        }
    }

    public TextView addRegisterRightTopBtn(UI activity, int strResId) {
        String text = activity.getResources().getString(strResId);
        TextView textView = findView(R.id.action_bar_right_clickable_textview);
        textView.setText(text);
        if (textView != null) {
            textView.setBackgroundResource(R.drawable.register_right_top_btn_selector);
            textView.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
        }
        return textView;
    }
}
