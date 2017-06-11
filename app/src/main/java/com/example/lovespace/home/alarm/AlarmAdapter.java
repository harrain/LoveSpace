package com.example.lovespace.home.alarm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Alarm;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by stephen on 2017/6/6.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {

    private Context mContext;
    private List<Alarm> alarmList;
    private String TAG = "AlarmAdapter";

    public AlarmAdapter(Context context, List<Alarm> list) {
        mContext = context;
        alarmList = list;
    }

    @Override
    public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alarm_item, parent, false);
        AlarmHolder holder = new AlarmHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmHolder holder, int position) {
        holder.bind(position);
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList == null ? 0:alarmList.size();
    }

    class AlarmHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.wu_tv)
        TextView wuTv;
        @BindView(R.id.time_tv)
        TextView timeTv;
        @BindView(R.id.switch_compat)
        SwitchCompat switchCompat;

        AlarmHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            try {
                Log.e(TAG,alarmList.get(position).toString());
                wuTv.setText(alarmList.get(position).getAlarmname());
                timeTv.setText(alarmList.get(position).getAlarmtime());
                switchCompat.setChecked(!alarmList.get(position).getClose());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
