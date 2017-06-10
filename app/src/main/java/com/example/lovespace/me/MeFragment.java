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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.BaseApplication;
import com.example.lovespace.DemoCache;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeFragment extends Fragment {


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
    @BindView(R.id.disturb_item_toggle)
    SwitchCompat disturb;
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
        updateHeadView();
        getUserInfo(account);
        showOther();
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    private void updateHeadView() {
        account = DemoCache.getAccount();
        String nickname = NimUserInfoCache.getInstance().getUserDisplayName(DemoCache.getAccount());
        tvUsername.setText("账号："+ account);
        tvNick.setText(nickname);

    }

    private void showOther(){
        otherAccount = Preferences.getOtherAccount();
        if (TextUtils.isEmpty(otherAccount) ){

            List<String> friendAccounts = NIMClient.getService(FriendService.class).getFriendAccounts();
            Log.e("frend",friendAccounts.toString());

            if (friendAccounts == null || friendAccounts.size() == 0){
                return;
            }
            for (String othername:friendAccounts) {
                rlAddBuddy.setVisibility(View.GONE);
                rlBuddy.setVisibility(View.VISIBLE);
                tvBuddyName.setText(getString(R.string.other_acount)+" "+othername);
                otherAccount = othername;
                break;
            }
        }
        rlAddBuddy.setVisibility(View.GONE);
        rlBuddy.setVisibility(View.VISIBLE);
        tvBuddyName.setText(getString(R.string.other_acount)+" "+otherAccount);
        getUserInfo(otherAccount);

    }

    @OnClick(R.id.setting_head)
    public void JumpAccountSetting(){
        UserProfileSettingActivity.startMine(getContext(), DemoCache.getAccount());
    }

    @OnClick(R.id.add_buddy_rl)
    public void jumpToAddBuddy(){
        AddFriendActivity.start(getContext());
    }

    @OnClick(R.id.buddy_account_rl)
    public void jumpToBuddyInfo(){
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

    public void updateUI(String anchor){
        if (anchor == DemoCache.getAccount()) {
            headview.loadBuddyAvatar(anchor);
            tvNick.setText(userInfo.getName());
        }else {
            ivBuddyIcon.loadBuddyAvatar(anchor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
