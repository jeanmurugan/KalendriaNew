package com.kalendria.kalendria.utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.RegisterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Magesh on 5/22/16.
 */
public class CommonSingleton {

    static CommonSingleton myInstance = null;
    String Tag = "CommonSingleton";
    List<String> cityTextArray;
    ArrayList<RegisterSpinner> cityModelArray =new ArrayList<RegisterSpinner>();


    public static CommonSingleton getInstance()
    {
        if(myInstance==null)
            myInstance= new CommonSingleton();

        return myInstance;
    }

    public List<String> getCityList()
    {
      return cityTextArray;
    }

    public ArrayList<RegisterSpinner> getCityMode()
    {
        return cityModelArray;
    }



    public void fetchCityList() {


        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.REGISTER_SPINNER, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("CommonSingleTon", response.toString());

                    try {
                        // Parsing json object response response will be a json object
                        if (response != null) {

                            try {
                                String JsonString = response.toString();
                                Constant.savedData(JsonString, "kCityJSONKey");

                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                            JSONArray jsonArray = response.getJSONArray("locations");
                            System.out.println("size of json array"+jsonArray.length());

                            cityTextArray = new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++) {

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                com.kalendria.kalendria.model.RegisterSpinner homePage_model=new com.kalendria.kalendria.model.RegisterSpinner();
                                homePage_model.setType(jsonObject.getString("type"));
                                String type=jsonObject.getString("type");

                                if(type.equalsIgnoreCase("city")  )
                                {
                                    homePage_model.setParent(jsonObject.getString("parent"));
                                    homePage_model.setId(jsonObject.getString("id"));
                                    homePage_model.setName(jsonObject.getString("name"));
                                    cityTextArray.add(jsonObject.getString("name"));
                                    cityModelArray.add(homePage_model);
                                }
                            }
                        }else{
                            // if responce is null write your commants here
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(Tag, "Error: " + error.getMessage());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
