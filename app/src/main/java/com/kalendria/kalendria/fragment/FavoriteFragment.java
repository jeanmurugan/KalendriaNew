package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.FavorateAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.FavorateModel;
import com.kalendria.kalendria.utility.KalendriaAppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by rajaganapathi on 1/14/2016.
 */
public class FavoriteFragment extends Fragment {

    ListView list;
    private List<FavorateModel> cafeList;
    public static String Tag = FavoriteFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    FavorateAdapter adapter1;
    private SharedPreferences sharedPref;
    TextView faverote_back_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        faverote_back_btn=(TextView)rootView.findViewById(R.id.faverote_back_btn);
        list=(ListView)rootView.findViewById(R.id.list);
        MakeJsonArrayReq();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent intent = new Intent(getActivity(), VenueItem.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("venueID", cafeList.get(position).getFavorateVenue_ID());
                editor.commit();
                startActivity(intent);
            }
        });
        faverote_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DashBoard.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void MakeJsonArrayReq() {
        showpDialog();
        cafeList=new ArrayList<>();
        String url=Constant.FAVORATE+Constant.getUserId(getActivity());
        System.out.println("favorare-->"+url);
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Tag, response.toString());
                        hidepDialog();

                       for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                JSONObject jo=object.getJSONObject("business");
                                FavorateModel favorateModel=new FavorateModel();

                                favorateModel.setFavorateVenue_ID(jo.getString("id"));
                                favorateModel.setFavorateVenuName(jo.getString("name"));
                                favorateModel.setFavorateRatting(jo.getString("overall_rating"));
                                favorateModel.setFavorateReview(jo.getString("review_count"));
                                favorateModel.setFavorateVenue_lat(jo.getString("lat"));
                                favorateModel.setFavorateVenue_long(jo.getString("long"));

                                 String media=jo.getString("media");
                                if(!media.equalsIgnoreCase("null")){
                                    JSONObject mediaObject=new JSONObject(media);
                                    favorateModel.setFavorateImage_url(mediaObject.getString("url"));
                                    favorateModel.setFavorateImage_thumb(mediaObject.getString("medium"));
                                }

                                cafeList.add(favorateModel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        hidepDialog();
                        adapter1 = new FavorateAdapter(getActivity(),cafeList);
                        list.setAdapter(adapter1);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
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
