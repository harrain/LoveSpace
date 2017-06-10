package com.example.lovespace.home.dynamics;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Commit;
import com.example.lovespace.main.model.bean.Dynamics;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/6/10.
 */

public class DyInfoAdapter extends RecyclerView.Adapter {

    Context mContext;
    Dynamics dynamics;
    List<Commit> commits;
    private final int HEADER = 0;
    private final int COMMIT = 1;


    public DyInfoAdapter(Context mContext, Dynamics dynamics, List<Commit> commits) {
        this.mContext = mContext;
        this.dynamics = dynamics;
        this.commits = commits;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return COMMIT;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.dyinfo_header, parent, false);

        } else {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder {
        @BindView(R.id.dyi_head)
        HeadImageView dyiHead;
        @BindView(R.id.dyi_time_tv)
        TextView dyiTimeTv;
        @BindView(R.id.dyi_content_tv)
        TextView dyiContentTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
