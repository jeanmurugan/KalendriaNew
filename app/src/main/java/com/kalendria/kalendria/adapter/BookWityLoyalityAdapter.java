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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.BookWithLoyalityModel;
import com.kalendria.kalendria.model.Venue;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mansoor on 11/03/16.
 */
public class BookWityLoyalityAdapter extends BaseAdapter {

    public List<BookWithLoyalityModel> _data;
    private ArrayList<BookWithLoyalityModel> arraylist;
    Context _c;
    ViewHolder v;

    Button cross_image;
    Button home_service_book_btn;



    public BookWityLoyalityAdapter(List<BookWithLoyalityModel> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<BookWithLoyalityModel>();
        this.arraylist.addAll(_data);

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
            view = li.inflate(R.layout.categoryslect_child, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.imageView = (ImageView) view.findViewById(R.id.imavenu);
        v.ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);

        v.venu_name_txt = (TextView) view.findViewById(R.id.venu_name_txt);
        v.service_title_txt = (TextView) view.findViewById(R.id.service_title_txt);
        v.venulocation_tet = (TextView) view.findViewById(R.id.venulocation_tet);
        v.normal_price_text = (TextView) view.findViewById(R.id.normal_price_text);
        v.discount_price_txt = (TextView) view.findViewById(R.id.discount_price_txt);


        v.normal_price_text.setVisibility(View.GONE);
        final BookWithLoyalityModel data = (BookWithLoyalityModel) _data.get(i);
        v.venu_name_txt.setText(data.getName());
        v.service_title_txt.setText(data.getServiceName());
        v.venulocation_tet.setText(data.getRegion()+","+data.getCity());
        v.discount_price_txt.setText(data.getPoints());





        v.discount_price_txt.setTag(i);
        try {
            System.out.println("getoverallRatting-->"+data.getOverallRating());

            if (data.getOverallRating()!="null") {

                float numstars = Float.parseFloat(data.getOverallRating());
                v.ratingbar.setRating(numstars);
                LayerDrawable stars = (LayerDrawable) v.ratingbar.getProgressDrawable();
                if(v.ratingbar.getRating() > 0)
                {
                    stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean isImageFound = false;
        if( data.getImageUrl() != null && !"".equals(data.getImageUrl()) ){
            try {
                Picasso.with(_c)
                        .load(data.getImageUrl())
                                // .memoryPolicy(MemoryPolicy.NO_CACHE )
                                // .networkPolicy(NetworkPolicy.NO_CACHE)
                                //.resize(720, 350)
                                // .error(R.drawable.login)
                        .placeholder(R.drawable.login_logo)
                        .noFade()
                                // .fit().centerCrop()
                        .into(v.imageView);
                isImageFound=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(isImageFound==false)
        {
            v.imageView.setImageResource(R.drawable.bg);
        }





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
            for (BookWithLoyalityModel wp : arraylist) {
                if (wp.getOverallRating().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {

        RatingBar ratingbar;
        ImageView imageView;
        TextView venu_name_txt,service_title_txt,venulocation_tet,normal_price_text,discount_price_txt;

    }
}
