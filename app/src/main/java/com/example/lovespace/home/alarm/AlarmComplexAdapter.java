package com.example.lovespace.home.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Alarm;
import com.example.lovespace.main.model.dao.AlarmDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 包名：
 * 类名：
 * 类功能：符合alarmadapter，实现左滑删除，条目短按长按点击事件
 * 创建者：
 * 创建日期：
 */

public class AlarmComplexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context mContext;
    private List<Alarm> alarmList;
    private boolean hasMore = true;
    private ItemViewHolder itemViewHolder;
    private List<ItemViewHolder> holders = new ArrayList<>();
    private String TAG = "AlarmComplexAdapter";

    public AlarmComplexAdapter(List<Alarm> list){
        alarmList = list;
    }

    static class ItemViewHolder extends RecyclerViewDragHolder{

        @BindView(R.id.wu_tv)
        TextView wuTv;
        @BindView(R.id.time_tv)
        TextView timeTv;
        @BindView(R.id.switch_compat)
        SwitchCompat switchCompat;
        @BindView(R.id.delete)
        TextView deleteItem;
        @BindView(R.id.cardview)
        CardView cardView;

        ItemViewHolder(Context context, View bgView, View topView) {
            super(context, bgView, topView);

        }
        ItemViewHolder(Context context, View bgView, View topView, int mTrackingEdges) {
        super(context, bgView, topView, mTrackingEdges);
    }

    @Override
    public void initView(View itemView) {
        ButterKnife.bind(this, itemView);

    }
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }

            //获取背景菜单
            View mybg = LayoutInflater.from(parent.getContext()).inflate(R.layout.bg_menu, null);
            mybg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //获取item布局
            View view = LayoutInflater.from(mContext).inflate(R.layout.alarm_item
                    , parent, false);
            //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            //生成返回RecyclerView.ViewHolder
            return new ItemViewHolder(mContext, mybg, view, RecyclerViewDragHolder.EDGE_LEFT).getDragViewHolder();

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof RecyclerViewDragHolder.DragViewHolder) {

            itemViewHolder = (ItemViewHolder) RecyclerViewDragHolder.getHolder(holder);
            holders.add(itemViewHolder);

            bind(itemViewHolder,position);

            if (onItemClickListener != null){
                itemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(view,position);
                    }
                });
                itemViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onItemClickListener.onItemLongClick(view,position);
                        return false;
                    }
                });
                itemViewHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlarmDao.deleteRow(alarmList.get(position).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    alarmList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                }else {
                                    Log.e(TAG,"exception:"+e.getMessage());
                                }
                            }
                        });

                    }
                });
            }
        }
    }

    private void bind(ItemViewHolder holder, final int position){
        Log.e(TAG,alarmList.get(position).toString());
        try {
            holder.wuTv.setText(alarmList.get(position).getAlarmname());
            holder.timeTv.setText(alarmList.get(position).getAlarmtime());
            holder.switchCompat.setChecked(!alarmList.get(position).getClose());
            holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        cancelAlarm(position);
                    }
                    updateRing(isChecked,alarmList.get(position).getObjectId());

                }
            });
            if (alarmList.get(position).getCoupleid() != null){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void cancelAlarm(int position){
        Intent intent1 = new Intent(mContext, AlarmReceiver.class);
        intent1.putExtra("alarm_type", position + "");
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, position, intent1, 0);
        manager.cancel(pi);
    }

    private void updateRing(boolean isChecked,String objectId){
        AlarmDao.updateRow("isClose", !isChecked, objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(mContext, "已上传云端保存您的设置", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList == null ? 0 : alarmList.size();
    }


    public void closeItem(){
        for (ItemViewHolder itemHolder: holders) {
            itemHolder.close();
        }

    }
}
