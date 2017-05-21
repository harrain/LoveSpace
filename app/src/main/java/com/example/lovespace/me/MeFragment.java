package com.example.lovespace.me;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lovespace.DemoCache;
import com.example.lovespace.MainActivity;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.makeramen.roundedimageview.RoundedImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    RoundedImageView headview;
    @BindView(R.id.username)
    TextView tvUsername;
    @BindView(R.id.setting_head)
    RelativeLayout settingHead;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle("我");

        updateHeadView();
        return view;
    }

    private void updateHeadView() {
        tvUsername.setText("账号："+DemoCache.getAccount());
    }

    @OnClick(R.id.setting_head)
    public void JumpAccountSetting(){
        UserProfileSettingActivity.startMine(getContext(), DemoCache.getAccount());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
