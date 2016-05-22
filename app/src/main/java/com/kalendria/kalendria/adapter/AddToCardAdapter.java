package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.CheckOut;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.SubCategoryChild;
import com.kalendria.kalendria.model.SubCategoryHeader;
import com.kalendria.kalendria.utility.CircularNetworkImageView;
import com.kalendria.kalendria.utility.KalendriaAppController;

import java.util.ArrayList;

public class AddToCardAdapter extends BaseAdapter {
    private ArrayList<Object> personArray;
    private LayoutInflater inflater;
    private static final int TYPE_PERSON = 0;
    private static final int TYPE_DIVIDER = 1;
    TextView title,name,cityRegion;
    LinearLayout addtocart_child_ll;
    Context mContext;
    RelativeLayout addtocart_header_rl;
    ImageLoader imageLoader = KalendriaAppController.getInstance().getImageLoader();
    private SharedPreferences sharedPref;


    public AddToCardAdapter(Context context, ArrayList<Object> personArray) {
        this.personArray = personArray;
        mContext=context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return personArray.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return personArray.get(position);
    }

    @Override
    public int getViewTypeCount() {
        // TYPE_PERSON and TYPE_DIVIDER
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof AddToCardServiceModel) {
            return TYPE_PERSON;
        }

        return TYPE_DIVIDER;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_PERSON);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_PERSON:
                    convertView = inflater.inflate(R.layout.row_item, parent, false);
                    break;
                case TYPE_DIVIDER:
                    convertView = inflater.inflate(R.layout.add_to_card_row_header, parent, false);
                    break;
            }
        }

        switch (type) {
            case TYPE_PERSON:
                final AddToCardServiceModel person = (AddToCardServiceModel)getItem(position);
                name = (TextView)convertView.findViewById(R.id.nameLabel);
                addtocart_child_ll = (LinearLayout) convertView.findViewById(R.id.addtocart_child_ll);
                name.setText(person.getServiceName());

                addtocart_child_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, CheckOut.class);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("venueID", person.getVenueID());
                        editor.commit();
                        mContext.startActivity(intent);
                        //Toast.makeText(mContext, "Working progress is going on ....", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case TYPE_DIVIDER:

                title = (TextView)convertView.findViewById(R.id.addtocart_header);
                addtocart_header_rl = (RelativeLayout) convertView.findViewById(R.id.addtocart_header_rl);
                cityRegion = (TextView)convertView.findViewById(R.id.addtocard_cityregion);

              //  String titleString = (String)getItem(position);
            final    AddToCardVenueModel header = (AddToCardVenueModel)getItem(position);

                title.setText(header.getVenueName());

                cityRegion.setText(header.getRegion()+","+header.getCity());

                if (imageLoader == null)

                    imageLoader = KalendriaAppController.getInstance().getImageLoader();

                final CircularNetworkImageView thumbNail = (CircularNetworkImageView) convertView.findViewById(R.id.thumbnail);

                if(header.getVenuImage()!=null) {

                    imageLoader.get(header.getVenuImage(), new ImageLoader.ImageListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG_image", "Image Load Error: " + error.getMessage());
                        }

                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                            if (response.getBitmap() != null) {
                                // load image into imageview
                                thumbNail.setImageBitmap(response.getBitmap());

                            }
                        }
                    });
                }
                sharedPref = mContext.getSharedPreferences(Constant.MyPREFERENCES, 0);
                addtocart_header_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, VenueItem.class);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("venueID", header.getVenueID());
                        editor.commit();
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

        return convertView;
    }
}