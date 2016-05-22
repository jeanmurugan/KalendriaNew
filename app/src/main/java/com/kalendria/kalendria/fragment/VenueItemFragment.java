package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.PostReview;
import com.kalendria.kalendria.adapter.ReviewAdapter;
import com.kalendria.kalendria.adapter.VeneuItemsServiceAdapter;
import com.kalendria.kalendria.adapter.VenueAdapter;
import com.kalendria.kalendria.adapter.VenueItemAdapter;
import com.kalendria.kalendria.adapter.ViewPagerAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.ReviewModel;
import com.kalendria.kalendria.model.VeneuItemServiceHeader;
import com.kalendria.kalendria.model.Venue;
import com.kalendria.kalendria.model.VenueDay;
import com.kalendria.kalendria.model.VenueItemServiceChild;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueItemFragment extends Fragment {


    public static String TAG = VenueItemFragment.class.getSimpleName();
    private ProgressDialog pDialog;

    VenueAdapter adapter1;
    ListView list_day;


    ViewPager viewPager;
    PagerAdapter adapter;
    VenueItemAdapter selectedVenuAdapter;
    CirclePageIndicator mIndicator;
    TextView venu_name_txt, service_title_txt, venue_text,favate;
    ArrayList<String> imageLinksLists = new ArrayList<>();
    ArrayList<VenueDay> custum_list = new ArrayList<VenueDay>();
    TextView parking, city_name, relign_name, number, email_txt, website_txt, venue_service_buttton,venue_location_txt,venue_review_txt;

    TextView venue_header, services_header, review_header, location_header;

    RatingBar ratingbar;
    ExpandableListView expListView;
    private ArrayList<VeneuItemServiceHeader> ExpListItems;
    ArrayList<VeneuItemServiceHeader> list = new ArrayList<VeneuItemServiceHeader>();
    ArrayList<VenueItemServiceChild> ch_list;
    VeneuItemsServiceAdapter listAdapter;
    LinearLayout venue_ll, service_ll,location_lv;

    List<String> mjsonResonceSingletone;
    int currentPage, NUM_PAGES;

    // geting the mapview start ==//
    double latitude, longitude;
    private GoogleMap mMap; // Might be null if Google Play services APK is not
    // available.
    private Marker marker;
    private Hashtable<String, String> markers;

    private SupportMapFragment fragment;
    private GoogleMap map;
    private boolean favoriate=true;

    private ListView reate_us;
    private List<ReviewModel> cafeList=new ArrayList<ReviewModel>();;
    ReviewAdapter reviewAdapter;
    Button review_rateus_btn,register_back_btn;

    private SharedPreferences sharedPref;
    View gridview;

    // geting the mapview end ==//

    public VenueItemFragment() {
        mjsonResonceSingletone = JsonResponce.getInstance().getParamList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         gridview = inflater.inflate(R.layout.selectedvenuactivity, container, false);
        init();


        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.venue_item_servie_footer, null, false);
        reate_us.addFooterView(footerView);

        Button forward = (Button) footerView.findViewById(R.id.review_rateus_btn1);
        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                getReviewStaus();
            }
        });
        /*footerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

            }
        });*/



        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setFillColor(getResources().getColor(R.color.bidgreenpressed));
        mIndicator.setStrokeColor(0xFFFFFFFF);
        mIndicator.setStrokeWidth(1);
        mIndicator.setRadius(3 * density);


        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String phone_no= number.getText().toString().replaceAll("-", "");
                if(phone_no!=null){
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phone_no));
                    startActivity(callIntent);
                }

            }
        });

      /*  venu_name_txt.setText(Constant.subCategeryHeaderName(getActivity()));
        service_title_txt.setText(Constant.subCategeryServiceName(getActivity()));

        float numstars = Float.parseFloat(Constant.getRatting(getActivity()));
        ratingbar.setRating(numstars);

        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();

        if(ratingbar.getRating() > 0)
        {
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        }*/

        getfavorite();
        makeJsonObjectRequest();
        onClick();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


       markers = new Hashtable<String, String>();


        setUpMapIfNeeded();
        if(mMap!=null) //coded by Magesh to avoid unwanted crash
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        return gridview;
    }

    private void init(){
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        ratingbar = (RatingBar) gridview.findViewById(R.id.ratingbar_venue_items);
        parking = (TextView) gridview.findViewById(R.id.parking);
        city_name = (TextView) gridview.findViewById(R.id.city_name);
        relign_name = (TextView) gridview.findViewById(R.id.relign_name);
        number = (TextView) gridview.findViewById(R.id.number);
        email_txt = (TextView) gridview.findViewById(R.id.email_txt);
        venue_text = (TextView) gridview.findViewById(R.id.venue_text);
        website_txt = (TextView) gridview.findViewById(R.id.website_txt);
        list_day = (ListView) gridview.findViewById(R.id.list_day);
        venue_service_buttton = (TextView) gridview.findViewById(R.id.venue_service_buttton);
        venue_review_txt = (TextView) gridview.findViewById(R.id.venue_review_txt);
        venue_location_txt = (TextView) gridview.findViewById(R.id.venue_location_txt);
        //review_rateus_btn = (Button) gridview.findViewById(R.id.review_rateus_btn);
        register_back_btn = (Button) gridview.findViewById(R.id.register_back_btn);

        venue_header = (TextView)gridview.findViewById(R.id.title_venue);
        services_header = (TextView)gridview.findViewById(R.id.title_services);
        review_header = (TextView)gridview.findViewById(R.id.title_review);
        location_header = (TextView)gridview.findViewById(R.id.title_location);


        venu_name_txt = (TextView) gridview.findViewById(R.id.venu_name_txt);
        service_title_txt = (TextView) gridview.findViewById(R.id.service_title_txt);
        favate = (TextView) gridview.findViewById(R.id.favate);


        viewPager = (ViewPager) gridview.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) gridview.findViewById(R.id.indicator);

        expListView = (ExpandableListView) gridview.findViewById(R.id.lvExp);
        venue_ll = (LinearLayout) gridview.findViewById(R.id.venue_ll);
        service_ll = (LinearLayout) gridview.findViewById(R.id.service_ll);
        location_lv = (LinearLayout) gridview.findViewById(R.id.location_ll);
        reate_us = (ListView) gridview.findViewById(R.id.reate_us);
    }



    private void setUpMapIfNeeded() {
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

        if (map == null) {
            mMap = fragment.getMap();
            //map = fragment.getMap();
            // mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
            if (mMap != null) {
                //zooming method will be here
            }
        }

    }

    private void onClick() {

        /*for ward fb link */
        website_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String url = "http://www.facebook.com";
                String url = "http://"+website_txt.getText().toString().trim();
                if(!url.isEmpty()&&url!=null){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });

        register_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        venue_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                venu_name_txt.setVisibility(View.VISIBLE);
                service_title_txt.setVisibility(View.VISIBLE);
                ratingbar.setVisibility(View.VISIBLE);
                favate.setVisibility(View.VISIBLE);

                venue_header.setVisibility(View.VISIBLE);
                services_header.setVisibility(View.GONE);
                review_header.setVisibility(View.GONE);
                location_header.setVisibility(View.GONE);

                venue_text.setBackgroundColor(Color.parseColor("#FFFFFF"));
                venue_text.setTextColor(Color.parseColor("#000000"));

                venue_service_buttton.setBackgroundColor(Color.parseColor("#0097db"));
                venue_service_buttton.setTextColor(Color.parseColor("#FFFFFF"));

                venue_location_txt.setTextColor(Color.parseColor("#FFFFFF"));
                venue_location_txt.setBackgroundColor(Color.parseColor("#0097db"));

                venue_review_txt.setTextColor(Color.parseColor("#FFFFFF"));
                venue_review_txt.setBackgroundColor(Color.parseColor("#0097db"));

                service_ll.setVisibility(View.GONE);
                location_lv.setVisibility(View.GONE);
                reate_us.setVisibility(View.GONE);
               // review_rateus_btn.setVisibility(View.GONE);
                venue_ll.setVisibility(View.VISIBLE);

            }
        });
        favate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoriate){
                    //Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
                    String venueId=Constant.getVenueId(getActivity());
                    final  String userID=Constant.getUserId(getActivity());
                    sendFavorite(venueId,userID);
                }

            }
        });

       /* review_rateus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReviewStaus();

            }
        });*/

        venue_service_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venue_service_buttton.setTextColor(Color.parseColor("#000000"));
                venue_service_buttton.setBackgroundColor(Color.parseColor("#FFFFFF"));

                venue_text.setBackgroundColor(Color.parseColor("#0097db"));
                venue_text.setTextColor(Color.parseColor("#FFFFFF"));

                venue_location_txt.setTextColor(Color.parseColor("#FFFFFF"));
                venue_location_txt.setBackgroundColor(Color.parseColor("#0097db"));

                venue_review_txt.setTextColor(Color.parseColor("#FFFFFF"));
                venue_review_txt.setBackgroundColor(Color.parseColor("#0097db"));

                venu_name_txt.setVisibility(View.GONE);
                service_title_txt.setVisibility(View.GONE);
                ratingbar.setVisibility(View.GONE);
                favate.setVisibility(View.GONE);
                venue_ll.setVisibility(View.GONE);

                venue_header.setVisibility(View.GONE);
                services_header.setVisibility(View.VISIBLE);
                review_header.setVisibility(View.GONE);
                location_header.setVisibility(View.GONE);


                location_lv.setVisibility(View.GONE);
                venue_ll.setVisibility(View.GONE);
                reate_us.setVisibility(View.GONE);
               // review_rateus_btn.setVisibility(View.GONE);
                service_ll.setVisibility(View.VISIBLE);

                getservice();
            }
        });
        venue_review_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venue_review_txt.setTextColor(Color.parseColor("#000000"));
                venue_review_txt.setBackgroundColor(Color.parseColor("#FFFFFF"));

                venue_service_buttton.setTextColor(Color.parseColor("#FFFFFF"));
                venue_service_buttton.setBackgroundColor(Color.parseColor("#0097db"));

                venue_text.setBackgroundColor(Color.parseColor("#0097db"));
                venue_text.setTextColor(Color.parseColor("#FFFFFF"));

                venue_location_txt.setTextColor(Color.parseColor("#FFFFFF"));
                venue_location_txt.setBackgroundColor(Color.parseColor("#0097db"));

                venu_name_txt.setVisibility(View.GONE);
                service_title_txt.setVisibility(View.GONE);
                ratingbar.setVisibility(View.GONE);
                favate.setVisibility(View.GONE);

                venue_header.setVisibility(View.GONE);
                services_header.setVisibility(View.GONE);
                review_header.setVisibility(View.VISIBLE);
                location_header.setVisibility(View.GONE);

                location_lv.setVisibility(View.GONE);
                venue_ll.setVisibility(View.GONE);
                service_ll.setVisibility(View.GONE);
                reate_us.setVisibility(View.VISIBLE);

                //review_rateus_btn.setVisibility(View.VISIBLE);

                getReview();

            }
        });

        venue_location_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                venu_name_txt.setVisibility(View.GONE);
                service_title_txt.setVisibility(View.GONE);
                ratingbar.setVisibility(View.GONE);
                favate.setVisibility(View.GONE);
                //review_rateus_btn.setVisibility(View.GONE);

                venue_text.setBackgroundColor(Color.parseColor("#0097db"));
                venue_text.setTextColor(Color.parseColor("#FFFFFF"));

                venue_service_buttton.setTextColor(Color.parseColor("#FFFFFF"));
                venue_service_buttton.setBackgroundColor(Color.parseColor("#0097db"));

                venue_location_txt.setTextColor(Color.parseColor("#000000"));
                venue_location_txt.setBackgroundColor(Color.parseColor("#FFFFFF"));

                venue_review_txt.setTextColor(Color.parseColor("#FFFFFF"));
                venue_review_txt.setBackgroundColor(Color.parseColor("#0097db"));

                reate_us.setVisibility(View.GONE);
                venue_ll.setVisibility(View.GONE);
                service_ll.setVisibility(View.GONE);
                location_lv.setVisibility(View.VISIBLE);

                venue_header.setVisibility(View.GONE);
                services_header.setVisibility(View.GONE);
                review_header.setVisibility(View.GONE);
                location_header.setVisibility(View.VISIBLE);

                setmMap();

            }
        });
        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*
				 * Toast.makeText(getApplicationContext(),
				 * listDataHeader.get(groupPosition) + " Expanded",
				 * Toast.LENGTH_SHORT).show();
				 */
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * listDataHeader.get(groupPosition) + " Collapsed",
				 * Toast.LENGTH_SHORT).show();
				 */

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition)+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    public LatLng getLatLng() {
        latitude = Double.parseDouble(Constant.getVenueLat(getActivity()));
        longitude = Double.parseDouble(Constant.getVenulont(getActivity()));

        if (this.latitude == 0 || this.longitude == 0) {
            return null;
        }
        return new LatLng(this.latitude, this.longitude);
    }

    private void setmMap() {

        latitude = Double.parseDouble(Constant.getVenueLat(getActivity()));
        longitude = Double.parseDouble(Constant.getVenulont(getActivity()));
        final Marker kiel = mMap.addMarker(new MarkerOptions()
                .position(getLatLng())
                .title(Constant.subCategeryHeaderName(getActivity()))
                .snippet(Constant.subCategeryServiceName(getActivity()))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        markers.put(kiel.getId(), Constant.getVenuSelecedImageUrl(getActivity()));


    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (VenueItemFragment.this.marker != null
                    && VenueItemFragment.this.marker.isInfoWindowShown()) {
                VenueItemFragment.this.marker.hideInfoWindow();
                VenueItemFragment.this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            VenueItemFragment.this.marker = marker;
            String url = null;

            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null
                        && markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final ImageView image = ((ImageView) view.findViewById(R.id.badge));

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {

                Picasso.with(getActivity()).load(url).into(image);
                getInfoContents(marker);
               /* imageLoader.displayImage(url, image, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view,loadedImage);
                                getInfoContents(marker);
                            }
                        });*/
            } else {
                image.setImageResource(R.drawable.ic_cast_disabled_light);
            }

            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }

            final String snippet = marker.getSnippet();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }

            return view;
        }
    }


    private void makeJsonObjectRequest() {

        showpDialog();
        String venueID = Constant.getVenueId(getActivity());
        String url =Constant.HOST+"api/v1/business/"+venueID+"?populate=medias,working_hours,region,city";
        //System.out.println("VeneItemFragement-->"+url);
        Log.d(TAG,url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {



                        venu_name_txt.setText(response.getString("name")); // get the Venue Name
                        float numstars = Float.parseFloat(response.getString("overall_rating")); // get the overall ratting for particular venue
                        /* set ratting particular venue page start */
                        ratingbar.setRating(numstars);
                        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
                        if(ratingbar.getRating() > 0)
                        {
                            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                            stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                            stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

                        }
                        /* set ratting particular venue page end  */

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lat", response.getString("lat"));
                        editor.putString("long", response.getString("long"));
                        editor.putString("venueName", response.getString("name"));
                        String logation_image_url=response.getString("media");
                        if(!logation_image_url.equalsIgnoreCase("null")){
                            JSONObject logation_image_object = response.getJSONObject("media");
                            editor.putString("ImageUrl",logation_image_object.getString("thumb"));
                            System.out.println("location image urr----->"+Constant.getVenuSelecedImageUrl(getActivity()));
                        }
                        editor.commit();

                        JSONObject cityObject = response.getJSONObject("city");
                        relign_name.setText(cityObject.getString("name"));
                        JSONObject region = response.getJSONObject("region");
                        city_name.setText(region.getString("name"));
                        number.setText(response.getString("phone"));
                        email_txt.setText(response.getString("email"));
                        if(!response.getString("website").equalsIgnoreCase("null")){
                            website_txt.setText(response.getString("website"));
                        }


//Code altered by Magesh
                        JSONArray jsonArray = response.getJSONArray("working_hours");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VenueDay selectedVenuDay = new VenueDay();

                                String dayStr = jsonObject.getString("day");
                                if(dayStr.length()==1)
                                    dayStr = dayStr.toUpperCase() ;
                                else if(dayStr.length()>2)
                                    dayStr = dayStr.substring(0,1).toUpperCase() + dayStr.substring(1).toLowerCase();
                                selectedVenuDay.setDay(dayStr);

                                selectedVenuDay.setOpen(SafeParser.getBoolen(jsonObject, "isOpen", false));

                                selectedVenuDay.setStart_time(jsonObject.getString("start_time"));
                                selectedVenuDay.setEnd_time(jsonObject.getString("end_time"));
                                String orderStr = jsonObject.getString("order");
                                if (orderStr != null)
                                    selectedVenuDay.order = Integer.parseInt(orderStr);
                                else
                                    selectedVenuDay.order = 0;
                                custum_list.add(selectedVenuDay);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                        }

                        Collections.sort(custum_list, new Comparator<VenueDay>() {
                            @Override
                            public int compare(VenueDay o1, VenueDay o2) {
                                return o1.order - o2.order;
                            }
                        });


                        System.out.println("custum_list -->" + custum_list.size());
                        selectedVenuAdapter = new VenueItemAdapter(custum_list, getActivity());
                        list_day.setAdapter(selectedVenuAdapter);

                        JSONArray jsonmedia = response.getJSONArray("medias");
                        for (int j = 0; j < jsonmedia.length(); j++) {
                            JSONObject jsonObject = jsonmedia.getJSONObject(j);
                            imageLinksLists.add(jsonObject.getString("thumb"));
                        }
                            if(response.getJSONObject("parking")!=null){
                                JSONObject jsonObject = response.getJSONObject("parking");
                                parking.setText(jsonObject.getString("info"));
                            }

                        adapter = new ViewPagerAdapter(getActivity(), imageLinksLists);
                        viewPager.setAdapter(adapter);
                        currentPage = viewPager.getCurrentItem();
                        NUM_PAGES = imageLinksLists.size();
                        mIndicator.setViewPager(viewPager);


                    } else {
                        // if responce is null write your commants here
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getservice() {

        venue_ll.setVisibility(View.GONE);
        service_ll.setVisibility(View.VISIBLE);

        if(list.size()==0) {

            showpDialog();
            String venueID = Constant.getVenueId(getActivity());
            String url =Constant.HOST+"api/v1/service?business="+venueID;
            System.out.println("service-->" + url);

            StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        list.clear();
                        String s = mjsonResonceSingletone.get(0);
                        if (s != null) {

                            JSONObject jsonObject_category = new JSONObject(s);
                            JSONArray jsonArray_category = jsonObject_category.getJSONArray("categorys");
                            list = new ArrayList<VeneuItemServiceHeader>();
                            for (int j = 0; j < jsonArray_category.length(); j++) {

                                JSONObject jsonObject = jsonArray_category.getJSONObject(j);
                                String type = jsonObject.getString("id");
                                String name = jsonObject.getString("name");
                                VeneuItemServiceHeader gru = new VeneuItemServiceHeader();
                                gru.setName(jsonObject.getString("name"));
                                //System.out.println("Header name-->"+jsonObject.getString("name"));

                                ch_list = new ArrayList<VenueItemServiceChild>();

                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    if (type.equalsIgnoreCase(obj.getString("category")))/*in this line categorry is child's id*/ {
                                        VenueItemServiceChild ch = new VenueItemServiceChild();
                                        //ch.setId(obj.getString("id"));
                                        ch.setId(obj.getString("category_service"));
                                        ch.setName(obj.getString("name"));
                                        ch.setDuration(obj.getString("duration"));
                                        ch.setDiscount(obj.getString("discount"));
                                        ch.setPrice(obj.getString("price"));

                                        // set venue detail with every child vealus

                                        ch.setVenueid(obj.getString("business"));
                                        ch.setVenuename(obj.getString("business_name"));
                                        ch.setVenueImage(Constant.getVenuSelecedImageUrl(getActivity()));
                                        ch.setVenuecity(city_name.getText().toString());
                                        ch.setVeneregiion(relign_name.getText().toString());
                                        ch_list.add(ch);

                                    }

                                }
                                gru.setItems(ch_list);
                                if (ch_list.size() > 0)
                                    list.add(gru);


                            }
                            hidepDialog();
                            ExpListItems = list;
                            listAdapter = new VeneuItemsServiceAdapter(getActivity(), ExpListItems);
                            expListView.setAdapter(listAdapter);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    hidepDialog();
                    VolleyLog.d(TAG, "Error : " + error.getMessage());

                }
            }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("content-type", "application/json");
                    return params;
                }

            };

            //queue.add(jsonObjReq);
            KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
        }


    }





    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getfavorite() {


        String venueId=Constant.getVenueId(getActivity());
        final  String userID=Constant.getUserId(getActivity());
        String url=Constant.HOST+"api/v1/like?business="+venueId;
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("" + "-->"+response);
                    JSONArray jsonArray = new JSONArray(response);
                    String favoriteCount=String.valueOf(jsonArray.length());
                    favate.setText(favoriteCount);
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        if(userID.equalsIgnoreCase(jsonObject.getString("user"))){
                            favate.setBackgroundResource(R.drawable.likef);
                            favoriate=false;
                        }else{

                        }

                    }

                } catch (JSONException e) {
                    System.out.println(TAG+e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                final int statusCode = error.networkResponse.statusCode;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                return params;
            }

        };

        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void sendFavorite(String business,String user) {
        JSONObject gcm_device_id = null;
        try {

            gcm_device_id = new JSONObject();
            gcm_device_id.put("business", business);
            gcm_device_id.put("user", user);
            System.out.println("getFavorite post json-->" + gcm_device_id  +Constant.POST_FAVORITE);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.POST_FAVORITE, gcm_device_id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        getfavorite();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);


                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);

                                String message = jsonObject.getString("message");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
                hidepDialog();
            }

        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };


        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    private void getReview() {



        if(cafeList.size()==0){
            showpDialog();
            String venueId=Constant.getVenueId(getActivity());
            String url=Constant.REVIEW+"business="+venueId+"&limit=10&populate=user,ratings,replyBy&profanity=0&skip=0";
            System.out.println("REVIEW-->"+url);
            JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    hidepDialog();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            ReviewModel favorateModel=new ReviewModel();
                            favorateModel.setReviewCommants(object.getString("message"));
                            favorateModel.setReviewRatting(object.getString("overall"));

                            String media=object.getString("reply");

                            JSONObject jo=object.getJSONObject("user");

                            favorateModel.setReviewUserName(jo.getString("first_name")+jo.getString("last_name"));
                            JSONObject image=jo.getJSONObject("profile_image");
                            favorateModel.setReviewUserTampImage_url(image.getString("thumb"));

                            cafeList.add(favorateModel);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    hidepDialog();
                    reviewAdapter = new ReviewAdapter(getActivity(),cafeList);
                    reate_us.setAdapter(reviewAdapter);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                    hidepDialog();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");

                    return params;
                }
            };
            KalendriaAppController.getInstance().addToRequestQueue(jreq);
        }

    }


    private void getReviewStaus() {


        showpDialog();
        String venueID = Constant.getVenueId(getActivity());
          String userID=Constant.getUserId(getActivity());
        String url =Constant.REVIEW_STATUS+"business="+venueID+"&profanity=1&user="+userID;
        System.out.println("REVIEW_STATUS-->" + url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if(response!=null){
                            JSONArray jsonArray=new JSONArray(response);

                        if(jsonArray.length()==0){
                           Intent intent = new Intent(getActivity(), PostReview.class);
                            startActivity(intent);
                        }else{
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());
                            dlgAlert.setMessage("You have already reviewed this venue. To leave another review you need to pay another visit here first. In the meantime please feel free to review other venues you've visited.");
                            dlgAlert.setTitle("Register ");
                            dlgAlert.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //dismiss the dialog
                                            dialog.dismiss();
                                        }
                                    });
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                final int statusCode = error.networkResponse.statusCode;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    /*vasanth you add this for Map data*/


    private void makeJsonObjectRequestmap() {

        String url;
        url = Constant.VENUE_FILTER;
        System.out.println("VENUE_FILTER------>>" + url);
        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (response != null) {

                        String track = response.getString("Tracks");

                        if (!track.equalsIgnoreCase("null")) {

                            JSONArray array = response.getJSONArray("Tracks");
                            System.out.println("test tracks array size-->" + array.length());
                            Toast.makeText(getContext(), "response method"+array.length(), Toast.LENGTH_SHORT).show();

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        }) {

        };

    }
}
