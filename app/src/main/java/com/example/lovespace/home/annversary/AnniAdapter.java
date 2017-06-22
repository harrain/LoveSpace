package com.example.lovespace.home.annversary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovespace.DemoCache;
import com.example.lovespace.R;
import com.example.lovespace.common.util.DateUtil;
import com.example.lovespace.main.model.bean.Anni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CDLX on 2017/6/1.
 */

public class AnniAdapter extends RecyclerView.Adapter {

    private static final int HEADER = 0;
    private static final int CONTENT = 1;
    private static final int FOOTER = 2;
    List<Anni> annis;
    Context mContext;

    List<FooterBean> footerBeens = new ArrayList<>();
    private DateUtil dateUtil;
    private String TAG = "AnniAdapter";

    public AnniAdapter(Context context, List list) {
        this.mContext = context;

        footerBeens.clear();
        annis= list;
        initFooterBean();
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
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (annis.size() > 0 && position <= annis.size()) {
            return CONTENT;
        } else {
            return FOOTER;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                View headview = LayoutInflater.from(mContext).inflate(R.layout.anni_header, parent, false);
                return new HeadHolder(headview);
            case CONTENT:
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.anni_content, parent, false);
                return new ContentHolder(contentView);
            case FOOTER:
                View footerView = LayoutInflater.from(mContext).inflate(R.layout.anni_footer, parent, false);
                return new FooterHolder(footerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int[] index = new int[1];
        if (holder instanceof FooterHolder) {

            if (annis!= null) {
                 index[0] = position - annis.size() - 1;
            }else {
                index[0] = position - 1;
            }
            ((FooterHolder)holder).bind(index[0]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext,CreateAnniActivity.class);
                    i.putExtra("footname",footerBeens.get(index[0]).getTitle());

                    mContext.startActivity(i);
                }
            });

        }else if (holder instanceof ContentHolder){
            index[0] = position - 1;
            try {
                ((ContentHolder)holder).bind(index[0]);
                if (onItemClickListener != null){
                    ((ContentHolder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemClickListener.onItemClick(view,index[0]);
                        }
                    });
                    ((ContentHolder)holder).cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            onItemClickListener.onItemLongClick(view,index[0]);
                            return false;
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            ((HeadHolder)holder).bind();
        }

    }

    private void initFooterBean() {
        footerBeens.add(new FooterBean(R.drawable.anni_birthday_icon, "生日"));
        footerBeens.add(new FooterBean(R.drawable.anni_hug_icon, "第一次拥抱的日子"));
        footerBeens.add(new FooterBean(R.drawable.anni_kiss_icon, "第一次kiss的日子"));
        footerBeens.add(new FooterBean(R.drawable.anni_travel_icon, "第一次携手旅行的日子"));
        footerBeens.add(new FooterBean(R.drawable.anni_marry_icon, "结婚纪念日"));

    }

    @Override
    public int getItemCount() {

        return annis == null?1+footerBeens.size():1 + annis.size() + footerBeens.size();
    }

    class FooterBean {
        int iconid;
        String title;

        public FooterBean(int id, String text) {
            iconid = id;
            title = text;
        }

        public int getIconid() {
            return iconid;
        }

        public void setIconid(int iconid) {
            this.iconid = iconid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.startday)
        TextView startday;
        @BindView(R.id.daycount_tv)
        TextView daycountTv;

        HeadHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind() {
            startday.setText(DemoCache.getStartTime());
            daycountTv.setText(DemoCache.getCoupleDays()+"天");
        }
    }

    class ContentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_content)
        TextView titleContent;
        @BindView(R.id.day_tv)
        TextView dayTv;
        @BindView(R.id.yearmonth_tv)
        TextView ymTv;
        @BindView(R.id.date_tv)
        TextView dateTv;
        @BindView(R.id.cardview)
        CardView cardView;

        ContentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int index) throws Exception{
            Log.e(TAG,annis.get(index).toString());

            String day = annis.get(index).getAnnidate();

            dateUtil = new DateUtil();
            List<Integer> list = dateUtil.string2int(day);

            ymTv.setText(list.get(0)+" "+list.get(1));
            dateTv.setText(list.get(2)+"");

            Date date = new Date();
            List<Integer> currents = dateUtil.string2int(dateUtil.date2string(date));
            long dl = dateUtil.date2long(day);
            long dal = dateUtil.date2long(date);
            Log.e(TAG,dl+":"+dal);
            if (annis.get(index).getAnniname().contains("生日")){
                list.set(0,currents.get(0));
                Log.e(TAG,"生日"+"list0:"+list.get(0));

                long newa = dateUtil.date2long(list.get(0)+"-"+list.get(1)+"-"+list.get(2));
                Log.e(TAG,"生日"+newa+":"+dal);
                if (newa > dal){
                    sameYear(list,currents,day,date,index,newa,dal);
                }else {
                    list.add(0,currents.get(0)+1);
                    int d = dateUtil.onlyDays(list,currents);
                    titleContent.setText(annis.get(index).getAnniname() +" 还有");
                    dayTv.setText(d+"天");
                    Log.e(TAG,annis.get(index).getAnniname() +" 还有"+d+"天");
                }
                return;
            }

            if (list.get(0).equals(currents.get(0))){

                sameYear(list,currents,day,date,index,dl,dal);
            }else {
                int d = dateUtil.onlyDays(list,currents);
                if ( dl> dal){
                    titleContent.setText(annis.get(index).getAnniname() +" 还有");
                    dayTv.setText(d+"天");
                    Log.e(TAG,annis.get(index).getAnniname() +" 还有"+d+"天");
                }else {
                    titleContent.setText(annis.get(index).getAnniname() +" 已经");
                    dayTv.setText(d+"天");
                    Log.e(TAG,annis.get(index).getAnniname() +" 还有"+d+"天");
                }
            }


        }

        private void sameYear(List<Integer> list,List<Integer> currents,String day,Date date,int index,long dl,long dal){
            int anniday = countAnnidays(list.get(0),list.get(1),list.get(2));
            int currentday = countAnnidays(currents.get(0),currents.get(1),currents.get(2));

            Log.e(TAG,"anniday:"+anniday);
            Log.e(TAG,"currentday:"+currentday);
            Log.e(TAG,"date2long(day):"+dateUtil.date2long(day));
            Log.e(TAG,"date2long(date):"+dateUtil.date2long(date));
            if (dl >dal){
                titleContent.setText(annis.get(index).getAnniname() +" 还有");
                dayTv.setText(anniday-currentday+"天");

            }else {
                titleContent.setText(annis.get(index).getAnniname() +" 已经");
                dayTv.setText(currentday-anniday+"天");
            }
        }

        public int countAnnidays(int year,int month,int day){
            dateUtil.year = year;
            Log.e(TAG,"year:"+dateUtil.year);
            dateUtil.month = month;
            Log.e(TAG,"month:"+dateUtil.month);
            dateUtil.day = day;
            Log.e(TAG,"day:"+dateUtil.day);
            return dateUtil.countDays();

        }

        public int countCurrentdays(int year,int month,int day){
            dateUtil.year = year;
            dateUtil.month = month;
            dateUtil.day = day;
            return dateUtil.countDays();
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title_footer)
        TextView titleFooter;
        @BindView(R.id.anni_cover)
        ImageView anniCover;
        @BindView(R.id.add_anni)
        ImageButton addAnni;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        public void bind(int position){
            anniCover.setImageResource(footerBeens.get(position).getIconid());
            titleFooter.setText(footerBeens.get(position).getTitle());
        }
    }
}
