package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.adapter.BookWityLoyalityAdapter;
import com.kalendria.kalendria.adapter.VenueAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.BookWithLoyalityModel;
import com.kalendria.kalendria.model.Venue;
import com.kalendria.kalendria.utility.KalendriaAppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by murugan on 1/14/2016.
 */
public class BookWithLoyaltyFragment extends Fragment {
    public static String TAG = BookWithLoyaltyFragment.class.getSimpleName();
    EditText maximum_et,minimum_et;
    ImageView searchImage;
    ListView lv_book_with_loyalty;
    private ProgressDialog pDialog;
    ArrayList<BookWithLoyalityModel> custum_list =new ArrayList<BookWithLoyalityModel>();
    ArrayList<BookWithLoyalityModel> maxMin =new ArrayList<BookWithLoyalityModel>();
    BookWityLoyalityAdapter adapter1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_with_loyalty, container, false);
        maximum_et=(EditText)rootView.findViewById(R.id.maximum_et);
        minimum_et=(EditText)rootView.findViewById(R.id.minimum_et);
        searchImage=(ImageView)rootView.findViewById(R.id.searchImage);
        lv_book_with_loyalty=(ListView) rootView.findViewById(R.id.lv_book_with_loyalty);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if(KalendriaAppController.isNetworkConnected(getActivity())){
            makeJsonObjectRequest();
        }else{
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxMin.clear();

                    String max=maximum_et.getText().toString().trim();
                    String min=minimum_et.getText().toString().trim();

                    if(max!=null &&min!=null ){

                        for(int i=0;i<custum_list.size();i++) {
                        int foo = Integer.parseInt(custum_list.get(i).getPoints());
                        int max1 = Integer.parseInt(max);
                        int min1 = Integer.parseInt(min);
                        if(max1>=foo&&min1<=foo){
                            BookWithLoyalityModel selectedModel=new BookWithLoyalityModel();
                            selectedModel.setId(custum_list.get(i).getId());
                            selectedModel.setName(custum_list.get(i).getName());
                            selectedModel.setServiceName(custum_list.get(i).getServiceName());
                            selectedModel.setOverallRating(custum_list.get(i).getOverallRating());
                            selectedModel.setPoints(custum_list.get(i).getPoints());
                            selectedModel.setCity(custum_list.get(i).getCity());
                            selectedModel.setRegion(custum_list.get(i).getRegion());
                            selectedModel.setImageUrl(custum_list.get(i).getImageUrl());
                            selectedModel.setImageUrlThumb(custum_list.get(i).getImageUrlThumb());

                            maxMin.add(selectedModel);
                            System.out.println("result-->"+foo);
                        }
                    }
                        Collections.sort(maxMin, BookWithLoyalityModel.sortPoints);

                        adapter1 = new BookWityLoyalityAdapter(maxMin,getActivity());
                        lv_book_with_loyalty.setAdapter(adapter1);
                }
                    else{
                        adapter1 = new BookWityLoyalityAdapter(custum_list,getActivity());
                        lv_book_with_loyalty.setAdapter(adapter1);

                        //Toast.makeText(getActivity(), "Please choose your points ", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        return rootView;
    }

    private void makeJsonObjectRequest() {
        showpDialog();

       String url="https://dev.api.kalendria.com/api/v1/service?populate=city,region,business&where={\"business\":{\"!\":\"null\"},\"enable_loyalty_points\":1,\"is_search\":1}";
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"--->"+response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        BookWithLoyalityModel selectedModel=new BookWithLoyalityModel();

                        selectedModel.setId(jsonObject.getString("id"));
                        selectedModel.setName(jsonObject.getString("business_name"));
                        selectedModel.setServiceName(jsonObject.getString("name"));
                        selectedModel.setOverallRating(jsonObject.getString("review_count"));
                        selectedModel.setPoints(jsonObject.getString("points"));

                        JSONObject bussiness=jsonObject.getJSONObject("business");
                        JSONObject city=jsonObject.getJSONObject("city");
                        JSONObject region=jsonObject.getJSONObject("region");
                        selectedModel.setCity(city.getString("name"));
                        selectedModel.setRegion(region.getString("name"));
                        String nameValue = bussiness.getString("media");
                        if(nameValue !="null" ) {
                            JSONObject jsonObject1=new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));
                            selectedModel.setImageUrlThumb(jsonObject1.getString("thumb"));
                        }
                        custum_list.add(selectedModel);

                    }
                    adapter1 = new BookWityLoyalityAdapter(custum_list,getActivity());
                    lv_book_with_loyalty.setAdapter(adapter1);

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
                // final int statusCode = error.networkResponse.statusCode;
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
