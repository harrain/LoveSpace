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
import com.example.lovespace.main.model.bean.Galary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/5/29.
 */

public class GalaryAdapter extends RecyclerView.Adapter<GalaryAdapter.GalaryHolder> {

    private Context mContext;
    private List<Galary> galaryList;

    public GalaryAdapter(Context context,List<Galary> list) {
        this.mContext = context;
        galaryList = list;
    }

    @Override
    public GalaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.galary_item, parent, false);
        GalaryHolder holder = new GalaryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GalaryHolder holder, final int position) {
        holder.galaryItemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GalaryInfoActivity.class);
                intent.putExtra("gid",galaryList.get(position).getObjectId());
                intent.putExtra("gname",galaryList.get(position).getGalaryname());
                mContext.startActivity(intent);

            }
        });
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return galaryList == null?0:galaryList.size();
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

        public void bind(int position) {
            try {

                galaryTitleTv.setText(galaryList.get(position).getGalaryname());
                galaryDateTv.setText(galaryList.get(position).getCreatedAt());
                photoSumCoverTv.setText(galaryList.get(position).getImagenum()+"");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
