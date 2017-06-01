package com.example.lovespace.home.annversary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.lovespace.R;
import com.example.lovespace.main.model.bean.Anni;

import java.util.List;

/**
 * Created by CDLX on 2017/6/1.
 */

public class AnniAdapter extends RecyclerView.Adapter {

    private static final int HEADER = 0;
    private static final int CONTENT = 0;
    private static final int FOOTER = 0;
    List<Anni> annis;
    Context mContext;

    List<FooterBean> footerBeens;

    public AnniAdapter(Context context,List list){
        this.mContext = context;
        annis = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private void initFooterBean(){
        footerBeens.add(new FooterBean(R.drawable.anni_birthday_icon,"生日"));
        footerBeens.add(new FooterBean(R.drawable.anni_hug_icon,"第一次拥抱的日子"));
        footerBeens.add(new FooterBean(R.drawable.anni_kiss_icon,"第一次kiss的日子"));
        footerBeens.add(new FooterBean(R.drawable.anni_travel_icon,"第一次携手旅行的日子"));
        footerBeens.add(new FooterBean(R.drawable.anni_marry_icon,"结婚纪念日"));

    }

    @Override
    public int getItemCount() {
        return 1 + annis.size();
    }

    class FooterBean{
        int iconid;
        String title;

        public FooterBean(int id,String text){
            iconid = id;
            title = text;
        }
    }
}
