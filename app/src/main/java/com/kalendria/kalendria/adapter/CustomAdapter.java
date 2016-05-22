package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.TimeBean;

import java.util.ArrayList;

/**
 * Created by chandirabalan on 5/11/2016.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<TimeBean> mylist = new ArrayList<TimeBean>();
    LayoutInflater inflater;

    public CustomAdapter(Context context, ArrayList<TimeBean> mylist) {
        this.context = context;
        this.mylist = mylist;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public TimeBean getItem(int i) {
        return mylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MyViewHolder mViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.mytextview, viewGroup, false);
            mViewHolder = new MyViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) view.getTag();
        }

        TimeBean currentListData = getItem(i);

        mViewHolder.tvTitle.setText(currentListData.getCal_time());

        return view;

    }

    private class MyViewHolder {
        TextView tvTitle;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tv);

        }
    }
}
