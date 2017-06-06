package com.example.lovespace.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lovespace.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/5/30.
 */

public class GalaryInfoAdapter extends RecyclerView.Adapter <GalaryInfoAdapter.ImageHolder>{


    private Context mContext;
    private List<String> imagePaths;
    private static  final String TAG = "GalaryInfoAdapter";

    public GalaryInfoAdapter(Context context, List<String> list) {
        mContext = context;
        imagePaths = list;
        if (imagePaths == null) {
            Log.e(TAG, "image集合空");
        }else {
            Log.e(TAG,"imagepath0:"+imagePaths.size());
        }
    }


    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.galary_info_itemimageview, parent, false);
        ImageHolder holder = new ImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        Log.e(TAG,"imagePath:"+imagePaths.get(position));
        Glide.with(mContext).load(imagePaths.get(position)).into(holder.galaryinfoIv);
    }

    @Override
    public int getItemCount() {
        if (imagePaths == null) {
            Log.e(TAG, "imagecount空");
        }else {
            Log.e(TAG,"imagepath1:"+imagePaths.size());
        }
        return imagePaths == null ? 0 : imagePaths.size();
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
