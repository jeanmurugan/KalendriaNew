package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.Venue;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by mansoor on 11/03/16.
 */
public class CheckOutAdapter extends BaseAdapter {

    public List<AddToCardServiceModel> _data;
    private ArrayList<AddToCardServiceModel> arraylist;
    CheckOutAdapterDelegate delegate;
    Context _c;
    ViewHolder v;
    public interface CheckOutAdapterDelegate
    {
        public void onShowStaffPicker(int position,AddToCardServiceModel data);
    }

    public CheckOutAdapter(List<AddToCardServiceModel> selectUsers, Context context,CheckOutAdapterDelegate delegate) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<AddToCardServiceModel>();
        this.arraylist.addAll(_data);
        this.delegate=delegate;
        System.out.println("i am from selected adapter page" + _data.size());
        //Toast.makeText(_c, "hi", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.checkoutinclude, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.imageView = (ImageView) view.findViewById(R.id.imavenu);
        v.service_nameLable = (TextView) view.findViewById(R.id.service_nameLable);
        v.service_duration = (TextView) view.findViewById(R.id.service_duration);
        v.service_price = (TextView) view.findViewById(R.id.service_price);
        v.service_deiscount = (TextView) view.findViewById(R.id.service_deiscount);
        v.service_date = (TextView) view.findViewById(R.id.service_date);
        v.service_time = (TextView) view.findViewById(R.id.service_time);
        v.staffName = (TextView) view.findViewById(R.id.staffName);
        v.staffRow = (TableRow)view.findViewById(R.id.staffrow);
        v.staffImageView = (ImageView) view.findViewById(R.id.thumbnail);


        final AddToCardServiceModel data = (AddToCardServiceModel) _data.get(i);
        v.service_nameLable.setText(data.getServiceName());
        v.service_duration.setText(data.getServiceDuration());

        v.service_date.setText(data.selectedDate);
        v.service_time.setText(data.selectedTime);
        if(data.selectedTime!=null)
            Log.d("selectedTime",data.selectedTime);

        v.service_price.setPaintFlags(v.service_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        v.service_price.setText(data.getStrikeOutAmount());

        v.service_deiscount.setText(data.getOriginalPrices());

        v.staffName.setText(data.getstaffname());

        final int pos = i;
        v.staffRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delegate.onShowStaffPicker(pos,data);
            }
        });

        if(data.getstaffname().length()>0) {
            if (data.staffthumbImage != null && !"".equals(data.staffthumbImage)) {
                try {
                    Picasso.with(_c)
                            .load(data.staffthumbImage)
                            // .memoryPolicy(MemoryPolicy.NO_CACHE )
                            // .networkPolicy(NetworkPolicy.NO_CACHE)
                            //.resize(720, 350)
                            // .error(R.drawable.login)
                            .placeholder(R.drawable.avatar)
                            .noFade()
                            // .fit().centerCrop()
                            .into(v.staffImageView);
                } catch (Exception e) {
                    v.staffImageView.setImageResource(R.drawable.avatar);
                    e.printStackTrace();
                }

            } else
                v.staffImageView.setImageResource(R.drawable.avatar);

            v.staffImageView.setVisibility(View.VISIBLE);
        }
        else
            v.staffImageView.setVisibility(View.INVISIBLE);

        if(!data.isValid || !data.isOpen)
            v.service_time.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorRoseRed));
        else
            v.service_time.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorLightGreen));


        view.setTag(data);
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (AddToCardServiceModel wp : arraylist) {
                if (wp.getServiceId().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {

        TextView service_nameLable, service_duration, service_price, service_deiscount, service_date, service_time, staffName;
        ImageView imageView;
        TableRow staffRow;
        ImageView staffImageView;


    }
}
