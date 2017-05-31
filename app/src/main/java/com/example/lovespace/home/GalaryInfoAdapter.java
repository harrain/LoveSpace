package com.example.lovespace.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lovespace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/5/30.
 */

public class GalaryInfoAdapter extends RecyclerView.Adapter <GalaryInfoAdapter.ImageHolder>{


    private Context mContext;

    public GalaryInfoAdapter(Context context) {
        mContext = context;
    }


    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.galary_info_itemimageview, parent, false);
        ImageHolder holder = new ImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.galaryinfo_iv)
        ImageView galaryinfoIv;

        ImageHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
