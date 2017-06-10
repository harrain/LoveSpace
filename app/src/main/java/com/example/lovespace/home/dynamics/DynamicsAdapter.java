package com.example.lovespace.home.dynamics;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Dynamics;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/6/10.
 */

public class DynamicsAdapter extends RecyclerView.Adapter<DynamicsAdapter.DYHolder> {

    Context mContext;
    List<Dynamics> dys;

    public DynamicsAdapter(Context context,List<Dynamics> list) {
        mContext = context;
        dys = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public DynamicsAdapter.DYHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dy_item, parent, false);

        return new DYHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicsAdapter.DYHolder holder, final int position) {
        holder.bind(position);
        if (onItemClickListener!=null){
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v,position);
                }
            });
            holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v,position);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dys==null ? 0:dys.size();
    }

    class DYHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.dy_head)
        HeadImageView dyHead;
        @BindView(R.id.dy_time)
        TextView dyTime;
        @BindView(R.id.dy_content)
        TextView dyContent;
        @BindView(R.id.cardview)
        CardView cardview;

        DYHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            dyHead.loadBuddyAvatar(dys.get(position).getUid());
            dyContent.setText(dys.get(position).getContent());
            dyTime.setText(dys.get(position).getCreatedAt());
        }
    }
}
