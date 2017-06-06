package com.example.lovespace.home.annversary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Anni;

import java.util.ArrayList;
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

    public AnniAdapter(Context context, List list) {
        this.mContext = context;

        footerBeens.clear();
        annis= list;
        initFooterBean();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterHolder) {
            int index;
            if (annis!= null) {
                 index = position - annis.size() - 1;
            }else {
                index = position - 1;
            }
            ((FooterHolder)holder).bind(index);
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
    }

    class ContentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_content)
        TextView titleContent;
        @BindView(R.id.day_tv)
        TextView dayTv;

        ContentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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