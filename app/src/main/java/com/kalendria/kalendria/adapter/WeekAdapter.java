package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kalendria.kalendria.R;

import java.util.ArrayList;

/**
 * Created by mansoor on 02/05/16.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {
    Context ctx;
    ArrayList<String> dataValue;
    ArrayList<String> dataname;

    int indexvalue = 0;

    OnItemClickListener mItemClickListener;

    public WeekAdapter(Context context, ArrayList<String> data, ArrayList<String> data1,int indexvalue1) {
        ctx = context;
        dataValue = data;
        dataname = data1;
        indexvalue = indexvalue1;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_row, parent, false);

        return new WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {

      /*  int index = days.indexOf(weekViews.get(0).getCurrentDay());
        System.out.println("Current position :  " + index);*/

        holder.days.setText(dataValue.get(position));
        holder.daysname.setText(dataname.get(position));

        if(position == indexvalue){
            holder.days.setBackgroundResource(R.drawable.button_circle);
            holder.days.setTextColor(Color.WHITE);
        }/*else {
            holder.days.setBackgroundResource(R.drawable.button_circle);
            holder.days.setTextColor(Color.WHITE);
        }*/
    }

    @Override
    public int getItemCount() {
        return dataValue.size();
    }

    public class WeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView days;
        TextView daysname;

        public WeekViewHolder(View itemView) {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.daysvalue);
            daysname = (TextView) itemView.findViewById(R.id.daysname);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
