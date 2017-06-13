package com.example.lovespace.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.BaseApplication;
import com.example.lovespace.DemoCache;
import com.example.lovespace.R;
import com.example.lovespace.common.util.DateUtil;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.home.alarm.AlarmActivity;
import com.example.lovespace.home.annversary.AnnversaryActivity;
import com.example.lovespace.home.dynamics.DynamicsActivity;
import com.example.lovespace.main.model.bean.Couple;
import com.example.lovespace.main.model.dao.CoupleDao;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;


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
    @BindView(R.id.home_title)
    RelativeLayout homeTitleRl;

    private Context mContext;
    private NimUserInfo userInfo;
    Unbinder unbind;
    private String TAG = "HomeFragment";
    DateUtil dateUtil;
    private String otherAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbind = ButterKnife.bind(this, view);
        mContext = getActivity();
        dateUtil = new DateUtil();
        home_grid.setAdapter(new HomeAdapter());
        home_grid.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        otherAccount = Preferences.getOtherAccount();
        getUserInfo(DemoCache.getAccount());
        if (TextUtils.isEmpty(otherAccount)) {
            homeTitleRl.setVisibility(View.INVISIBLE);
            return;
        }
        getUserInfo(otherAccount);
        if (DemoCache.getCoupleDays() != 0) {
            homeDate.setText("我们在一起 " + DemoCache.getCoupleDays() + " 天");
        } else {
            coupledays();
        }
    }

    private void coupledays() {
        CoupleDao.fetchCouple(Preferences.getCoupleId(), new SQLQueryListener<Couple>() {
            @Override
            public void done(BmobQueryResult<Couple> result, BmobException e) {
                if (e == null) {
                    List<Couple> list = result.getResults();
                    if (list.size() > 0) {
                        String start = list.get(0).getCreatedAt();
                        DemoCache.setStartTime(start);
                        Date d = dateUtil.string2date(start);
                        List<Integer> cd = dateUtil.string2int(dateUtil.date2string(d));
                        Date date = new Date();
                        List<Integer> currents = dateUtil.string2int(dateUtil.date2string(date));
                        if (cd.get(0).equals(currents.get(0))) {
                            int anniday = countAnnidays(cd.get(0), cd.get(1), cd.get(2));
                            int currentday = countAnnidays(currents.get(0), currents.get(1), currents.get(2));
                            int dd = currentday - anniday;
                            DemoCache.setCoupleDays(dd);
                            homeDate.setText("我们在一起 " + dd + " 天");
                        } else {
                            int dd = dateUtil.onlyDays(cd, currents);
                            DemoCache.setCoupleDays(dd);
                            homeDate.setText("我们在一起 " + dd + " 天");
                        }
                    }
                } else {
                    Log.e(TAG, "fetchCouple:" + e.getMessage());
                    if (e.getErrorCode() == 9010) {
                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                    } else if (e.getErrorCode() == 9016) {
                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public int countAnnidays(int year, int month, int day) {
        dateUtil.year = year;
        Log.e(TAG, "year:" + dateUtil.year);
        dateUtil.month = month;
        Log.e(TAG, "month:" + dateUtil.month);
        dateUtil.day = day;
        Log.e(TAG, "day:" + dateUtil.day);
        return dateUtil.countDays();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:

                if (!TextUtils.isEmpty(otherAccount)) {
                    startActivity(new Intent(mContext, AnnversaryActivity.class));
                } else {
                    Toast.makeText(mContext, "您还没添加另一半，不能使用纪念日功能！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:

                if (!TextUtils.isEmpty(otherAccount)) {
                    startActivity(new Intent(mContext, GalaryActivity.class));
                } else {
                    Toast.makeText(mContext, "您还没添加另一半，不能使用相册功能！", Toast.LENGTH_SHORT).show();
                }

                break;
            case 2:
                startActivity(new Intent(mContext, AlarmActivity.class));
                break;
            case 3:
                if (!TextUtils.isEmpty(otherAccount)) {
                    startActivity(new Intent(mContext, DynamicsActivity.class));
                } else {
                    Toast.makeText(mContext, "您还没添加另一半，不能使用留言板功能！", Toast.LENGTH_SHORT).show();
                }

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
        if (anchor.equals(DemoCache.getAccount())) {
            homeMeHead.loadBuddyAvatar(anchor);
        } else {
            homeOtherHead.loadBuddyAvatar(anchor);
        }
    }

}
