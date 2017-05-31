package com.example.lovespace.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovespace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/5/29.
 */

public class GalaryAdapter extends RecyclerView.Adapter<GalaryAdapter.GalaryHolder> {

    private Context mContext;

    public GalaryAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public GalaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.galary_item, parent, false);
        GalaryHolder holder = new GalaryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalaryHolder holder, int position) {
        holder.galaryItemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GalaryInfoActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class GalaryHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.galary_cover)
        ImageView galaryCover;
        @BindView(R.id.galary_title_tv)
        TextView galaryTitleTv;
        @BindView(R.id.galary_date_tv)
        TextView galaryDateTv;
        @BindView(R.id.photo_sum_cover_tv)
        TextView photoSumCoverTv;
        @BindView(R.id.galary_itemview)
        CardView galaryItemview;

        GalaryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
