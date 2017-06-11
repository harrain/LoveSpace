package com.example.lovespace.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Image;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/5/30.
 */

public class GalaryInfoAdapter extends RecyclerView.Adapter <GalaryInfoAdapter.ImageHolder>{


    private Context mContext;
    private List<String> imagePaths;
    private List<Image> imageList;
    private static  final String TAG = "GalaryInfoAdapter";

    public GalaryInfoAdapter(Context context, List<String> list,List<Image> images) {
        mContext = context;
        imagePaths = list;
        imageList = images;
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
    public void onBindViewHolder(ImageHolder holder, final int position) {
        Log.e(TAG,"imagePath:"+imagePaths.get(position));
        Glide.with(mContext).load(imagePaths.get(position)).into(holder.galaryinfoIv);
        holder.galaryinfoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ImageActivity.class);
                intent.putExtra("imagepath",imagePaths.get(position));
                intent.putExtra("objectId",imageList.get(position).getObjectId());
                mContext.startActivity(intent);
            }
        });
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
