package com.example.lovespace.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.BaseApplication;
import com.example.lovespace.DemoCache;
import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.home.alarm.AlarmActivity;
import com.example.lovespace.home.annversary.AnnversaryActivity;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {


    @BindView(R.id.home_gridview)
    GridView home_grid;

    String[] mItems = {"纪念日", "相册", "时钟", "动态"};
    int[] mPics = {R.mipmap.home_anniversary_icon, R.mipmap.home_album_icon, R.mipmap.home_sleep_icon, R.mipmap.home_menses_icon,};
    @BindView(R.id.home_me_head)
    HeadImageView homeMeHead;
    @BindView(R.id.home_other_head)
    HeadImageView homeOtherHead;
    @BindView(R.id.home_date)
    TextView homeDate;

    private Context mContext;
    private NimUserInfo userInfo;
    Unbinder unbind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbind = ButterKnife.bind(this, view);
        mContext = getActivity();
        home_grid.setAdapter(new HomeAdapter());
        home_grid.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUserInfo(DemoCache.getAccount());
        getUserInfo(Preferences.getOtherAccount());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(mContext, AnnversaryActivity.class));
                break;
            case 1:
                startActivity(new Intent(mContext, GalaryActivity.class));
                break;
            case 2:
                startActivity(new Intent(mContext, AlarmActivity.class));
                break;
            case 3:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.e("gridItem", mItems.length + "");
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(),
                        R.layout.home_list_item, null);
                holder = new ViewHolder();
                holder.ivItem = (ImageView) convertView.findViewById(R.id.iv_item);
                holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItem.setText(mItems[position]);
            holder.ivItem.setImageResource(mPics[position]);
            return convertView;
        }

        class ViewHolder {
            ImageView ivItem;
            TextView tvItem;
        }

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

    private void updateUI(String anchor) {
        if (anchor == DemoCache.getAccount()) {
            homeMeHead.loadBuddyAvatar(anchor);
        }else {
            homeOtherHead.loadBuddyAvatar(anchor);
        }
    }

}
