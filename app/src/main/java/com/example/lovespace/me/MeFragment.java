package com.example.lovespace.me;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.BaseApplication;
import com.example.lovespace.DemoCache;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.config.preference.UserPreferences;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.session.audio.MessageAudioControl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.inform_item_toggle)
    SwitchCompat inform;
    @BindView(R.id.alarm_item_toggle)
    SwitchCompat alarm;
    @BindView(R.id.light_item_toggle)
    SwitchCompat light;
    @BindView(R.id.diy_item_toggle)
    SwitchCompat diy;
    @BindView(R.id.listen_item_toggle)
    SwitchCompat listen;
    @BindView(R.id.headview)
    HeadImageView headview;
    @BindView(R.id.username)
    TextView tvUsername;
    @BindView(R.id.setting_head)
    RelativeLayout settingHead;
    @BindView(R.id.nickname_tv)
    TextView tvNick;
    @BindView(R.id.add_buddy_rl)
    RelativeLayout rlAddBuddy;
    @BindView(R.id.buddy_account_rl)
    RelativeLayout rlBuddy;
    @BindView(R.id.buddy_name_tv)
    TextView tvBuddyName;
    @BindView(R.id.buddy_head_iv)
    HeadImageView ivBuddyIcon;

    Unbinder bind;
    @BindView(R.id.clear_record)
    RelativeLayout clearRecord;
    private String otherAccount;
    private NimUserInfo userInfo;
    private String account;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        bind = ButterKnife.bind(this, view);
        mContext = getActivity();
        toolbar.setTitle("我");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inform.setChecked(UserPreferences.getNotificationToggle());
        alarm.setChecked(UserPreferences.getRingToggle());
        light.setChecked(UserPreferences.getLedToggle());
        diy.setChecked(UserPreferences.getNoticeContentToggle());
        listen.setChecked(com.netease.nim.uikit.UserPreferences.isEarPhoneModeEnable());

        updateHeadView();
        getUserInfo(account);
        showOther();
        inform.setOnCheckedChangeListener(this);
        alarm.setOnCheckedChangeListener(this);
        light.setOnCheckedChangeListener(this);
        diy.setOnCheckedChangeListener(this);
        listen.setOnCheckedChangeListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void updateHeadView() {
        account = DemoCache.getAccount();
        String nickname = NimUserInfoCache.getInstance().getUserDisplayName(DemoCache.getAccount());
        tvUsername.setText("账号：" + account);
        tvNick.setText(nickname);

    }

    private void showOther() {
        otherAccount = Preferences.getOtherAccount();
        if (TextUtils.isEmpty(otherAccount)) {

            List<String> friendAccounts = NIMClient.getService(FriendService.class).getFriendAccounts();
            Log.e("frend", friendAccounts.toString());

            if (friendAccounts == null || friendAccounts.size() == 0) {
                return;
            }
            for (String othername : friendAccounts) {
                rlAddBuddy.setVisibility(View.GONE);
                rlBuddy.setVisibility(View.VISIBLE);
                tvBuddyName.setText(getString(R.string.other_acount) + " " + othername);
                otherAccount = othername;
                break;
            }
        }
        rlAddBuddy.setVisibility(View.GONE);
        rlBuddy.setVisibility(View.VISIBLE);
        tvBuddyName.setText(getString(R.string.other_acount) + " " + otherAccount);
        getUserInfo(otherAccount);

    }

    @OnClick(R.id.setting_head)
    public void JumpAccountSetting() {
        UserProfileSettingActivity.startMine(getContext(), DemoCache.getAccount());
    }

    @OnClick(R.id.add_buddy_rl)
    public void jumpToAddBuddy() {
        AddFriendActivity.start(getContext());
    }

    @OnClick(R.id.buddy_account_rl)
    public void jumpToBuddyInfo() {
        query();
    }

    private void query() {
        DialogMaker.showProgressDialog(getContext(), null, false);
        final String account = otherAccount.toLowerCase();
        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo user) {
                DialogMaker.dismissProgressDialog();
                if (user == null) {
                    EasyAlertDialogHelper.showOneButtonDiolag(getContext(), R.string.user_not_exsit,
                            R.string.user_tips, R.string.ok, false, null);
                } else {
                    UserProfileActivity.start(getContext(), account);
                }
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    Toast.makeText(getContext(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(getContext(), "on exception:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo(final String anchor) {
        userInfo = NimUserInfoCache.getInstance().getUserInfo(anchor);
        if (userInfo == null) {
            NimUserInfoCache.getInstance().getUserInfoFromRemote(anchor, new RequestCallback<NimUserInfo>() {
                @Override
                public void onSuccess(NimUserInfo param) {
                    userInfo = param;
                    updateUI(anchor);
                }

                @Override
                public void onFailed(int code) {
                    Toast.makeText(BaseApplication.getInstance(), "getUserInfoFromRemote failed:" + code, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Throwable exception) {
                    Toast.makeText(BaseApplication.getInstance(), "getUserInfoFromRemote exception:" + exception, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            updateUI(anchor);
        }
    }

    public void updateUI(String anchor) {
        if (anchor.equals(DemoCache.getAccount())) {
            headview.loadBuddyAvatar(anchor);
            tvNick.setText(userInfo.getName());
        } else {
            ivBuddyIcon.loadBuddyAvatar(anchor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.inform_item_toggle:
                setMessageNotify(isChecked);
                break;
            case R.id.alarm_item_toggle:
                UserPreferences.setRingToggle(isChecked);
                StatusBarNotificationConfig config = UserPreferences.getStatusConfig();
                config.ring = isChecked;
                UserPreferences.setStatusConfig(config);
                NIMClient.updateStatusBarNotificationConfig(config);
                break;
            case R.id.light_item_toggle:
                UserPreferences.setLedToggle(isChecked);
                StatusBarNotificationConfig config1 = UserPreferences.getStatusConfig();
                StatusBarNotificationConfig demoConfig = DemoCache.getNotificationConfig();
                if (isChecked && demoConfig != null) {
                    config1.ledARGB = demoConfig.ledARGB;
                    config1.ledOnMs = demoConfig.ledOnMs;
                    config1.ledOffMs = demoConfig.ledOffMs;
                } else {
                    config1.ledARGB = -1;
                    config1.ledOnMs = -1;
                    config1.ledOffMs = -1;
                }
                UserPreferences.setStatusConfig(config1);
                NIMClient.updateStatusBarNotificationConfig(config1);
                break;
            case R.id.diy_item_toggle:
                UserPreferences.setNoticeContentToggle(isChecked);
                StatusBarNotificationConfig config2 = UserPreferences.getStatusConfig();
                config2.titleOnlyShowAppName = isChecked;
                UserPreferences.setStatusConfig(config2);
                NIMClient.updateStatusBarNotificationConfig(config2);
                break;
            case R.id.listen_item_toggle:
                com.netease.nim.uikit.UserPreferences.setEarPhoneModeEnable(isChecked);
                MessageAudioControl.getInstance(mContext).setEarPhoneModeEnable(isChecked);
                break;

        }
    }

    private void setMessageNotify(final boolean checkState) {
        // 如果接入第三方推送（小米），则同样应该设置开、关推送提醒
        // 如果关闭消息提醒，则第三方推送消息提醒也应该关闭。
        // 如果打开消息提醒，则同时打开第三方推送消息提醒。
        NIMClient.getService(MixPushService.class).enable(checkState).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Toast.makeText(mContext, R.string.user_info_update_success, Toast.LENGTH_SHORT).show();

                setToggleNotification(checkState);
            }

            @Override
            public void onFailed(int code) {

                // 这种情况是客户端不支持第三方推送
                if (code == ResponseCode.RES_UNSUPPORT) {

                    setToggleNotification(checkState);
                } else if (code == ResponseCode.RES_EFREQUENTLY) {
                    Toast.makeText(mContext, R.string.operation_too_frequent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    private void setToggleNotification(boolean checkState) {
        try {
            setNotificationToggle(checkState);
            NIMClient.toggleNotification(checkState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNotificationToggle(boolean on) {
        UserPreferences.setNotificationToggle(on);
    }


    @OnClick(R.id.clear_record)
    public void onClearRecord() {
        Toast.makeText(mContext, R.string.clear_msg_history_success, Toast.LENGTH_SHORT).show();
        NIMClient.getService(MsgService.class).clearMsgDatabase(true);

    }
}
