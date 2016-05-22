package com.kalendria.kalendria.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.adapter.FavorateAdapter;
import com.kalendria.kalendria.adapter.MyOderAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.FavorateModel;
import com.kalendria.kalendria.model.MyorderModel;
import com.kalendria.kalendria.utility.KalendriaAppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**

 */
public class MyOrdersFragment extends Fragment {

    private ProgressDialog pDialog;
    ListView list;
    private List<MyorderModel> cafeList;
    public static String Tag = MyOrdersFragment.class.getSimpleName();
    MyOderAdapter adapter1;
    TextView current_txt,previous_txt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invites, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        list=(ListView)rootView.findViewById(R.id.myorder);
        current_txt=(TextView)rootView.findViewById(R.id.current_txt);
        previous_txt=(TextView)rootView.findViewById(R.id.previous_txt);
        onclickButton();

        if(KalendriaAppController.isNetworkConnected(getActivity())){
            String url= Constant.MYODER;
            MakeJsonArrayReq(url);
        }else{
            Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public void onclickButton(){
        current_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_txt.setBackgroundColor(Color.parseColor("#FFFFFF"));
                current_txt.setTextColor(Color.parseColor("#000000"));

                previous_txt.setBackgroundColor(Color.parseColor("#000000"));
                previous_txt.setTextColor(Color.parseColor("#FFFFFF"));

                if(KalendriaAppController.isNetworkConnected(getActivity())){
                    String url= Constant.MYODER;
                    MakeJsonArrayReq(url);
                }else{
                    Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        previous_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                previous_txt.setBackgroundColor(Color.parseColor("#FFFFFF"));
                previous_txt.setTextColor(Color.parseColor("#000000"));

                current_txt.setBackgroundColor(Color.parseColor("#000000"));
                current_txt.setTextColor(Color.parseColor("#FFFFFF"));
                if(KalendriaAppController.isNetworkConnected(getActivity())){
                    String url= Constant.MYODER_PAST;
                    MakeJsonArrayReq(url);
                }else{
                    Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void MakeJsonArrayReq(String url) {
        showpDialog();
        cafeList=new ArrayList<>();

        System.out.println("MYODER-->"+url);
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(Tag, response.toString());
                hidepDialog();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        MyorderModel myorderModel=new MyorderModel();
                        JSONObject jo=object.getJSONObject("business");
                        myorderModel.setMorderVenuName(jo.getString("name"));
                        myorderModel.setMorderServiceName(object.getString("service_name"));
                        myorderModel.setMorderPoints(object.getString("service_name"));
                        myorderModel.setMorderPrice(object.getString("price"));
                        myorderModel.setMorderPoints(object.getString("points"));
                        myorderModel.setMorderTime(object.getString("time"));

                        cafeList.add(myorderModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                hidepDialog();
                adapter1 = new MyOderAdapter(getActivity(),cafeList);
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
