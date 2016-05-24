package com.kalendria.kalendria.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {

    Button singup_btn,login_btn,password, google_Login;
    EditText mybrands_totalPoints_text_textview,password_et;
    TextView forgot_password_txt,singup_txt;
    LoginButton login;
    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    public static String Tag = Login.class.getSimpleName();
    private SharedPreferences sharedPref;

    //Fb Login
    LoginButton login_facebook_button;
    CallbackManager callbackManager;
    Button fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fb Login start
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //Fb Login end
        setContentView(R.layout.main);



        init();
        onClick_button();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPref = getApplicationContext().getSharedPreferences(Constant.MyPREFERENCES,0);



        //Fb login Methed start==

        callbackManager = CallbackManager.Factory.create();
        fb = (Button) findViewById(R.id.fb);
        // Social Buttons
        login_facebook_button = (LoginButton) findViewById(R.id.login_facebook_button);


        login_facebook_button.setReadPermissions(Arrays.asList("public_profile","user_friends","email","user_birthday"));
        login_facebook_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());

                Log.e("OnGraph", "------------------------");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                Log.e("GraphResponse", "-------------" + object.toString());

                                try {

                                    if (object != null) {
                                        String id = object.getString("id");
                                       /* try {
                                            URL profile_pic = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
                                            Log.i("profile_pic",profile_pic + "");

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }*/
                                        String   name=(object.getString("name"));
                                        String   email=(object.getString("email"));
                                        //System.out.println("name"+"  " +name +" "  + email +" " + id);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("fbid", object.getString("id"));
                                        editor.putString("fbfirst_name", object.getString("first_name"));
                                        editor.putString("fblast_name", object.getString("last_name"));
                                        editor.putString("fbemail", object.getString("email"));
                                        editor.putString("fbgender", object.getString("gender"));
                                        editor.commit();
                                        if(email!=null){
                                            String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_EMAIL+email;
                                            checkBySocialMedia(url);
                                        }else{
                                            String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_FB+id;
                                            checkBySocialMedia(url);
                                        }



                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                //=====above method is used to get the fb values ==//

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,location,locale,timezone");
                request.setParameters(parameters);
                request.executeAsync();



                Log.e("Total Friend in List", "----------------------");
                new GraphRequest(loginResult.getAccessToken(),"/me/friends", null, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                Log.e("Friend in List", "-------------" + response.toString());
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("FacebookException", "-------------" + exception.toString());
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int responseCode,Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    public void onClick(View v) {
        if (v == fb) {
            LoginManager.getInstance().logOut();
            login_facebook_button.performClick();
        }
    }
    private void init(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        singup_btn= (Button) findViewById(R.id.singup_btn);
        login_btn= (Button) findViewById(R.id.login_btn);
        mybrands_totalPoints_text_textview=(EditText)findViewById(R.id.mybrands_totalPoints_text_textview);
        password_et=(EditText)findViewById(R.id.password_ed);

        forgot_password_txt=(TextView)findViewById(R.id.forgot_password_txt);

        google_Login = (Button)findViewById(R.id.google_pluse);
    }


    private void onClick_button(){
        singup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this,  Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        google_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Login.this, "google pluse login", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,  GooglePluseLogin.class);
                startActivity(intent);

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String   email = mybrands_totalPoints_text_textview.getText().toString().trim();
                String  passcode = password_et.getText().toString().trim();

                if(TextUtils.isEmpty(email) )
                {
                    mybrands_totalPoints_text_textview.setError("Please enter a valid email id");
                    mybrands_totalPoints_text_textview.requestFocus();
                }else if(!Validator.isEmailValid(email)){
                    mybrands_totalPoints_text_textview.setError("Please enter a valid email id");
                    mybrands_totalPoints_text_textview.requestFocus();
                }else if(passcode.isEmpty()){
                    password_et.setError("Please enter Your Password!");
                    password_et.requestFocus();
                }
                else {

                    if (KalendriaAppController.getInstance().isNetworkConnected(Login.this)) {
                        makeJsonObjectRequest(email,passcode);
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Check your internet connection",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        forgot_password_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });



    }



    private void makeJsonObjectRequest(String email,String password) {



        try {
            gcm_device_id = new JSONObject();
            gcm_device_id.put("identifier", email);
            gcm_device_id.put("password", password);
            System.out.println("dfddf" + gcm_device_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.LOGIN_URL, gcm_device_id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String  code = response.getString("isVerified");
                        if(!code.equalsIgnoreCase("false")){

                            String  id = response.getString("id");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userId", response.getString("id"));
                            editor.commit();
                            if(Constant.getUserId(getApplicationContext())!=null){
                                Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this, "Error. userID", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            // Toast.makeText(getApplicationContext(), "Please check your mail to verify your mail", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);
                            dlgAlert.setMessage("Please verify your account to login ");
                            dlgAlert.setTitle("Kalendria ");
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
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
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
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Login.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Kalendria ");
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


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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


    private void checkBySocialMedia(String url) {
        showpDialog();


        System.out.println("check social media by email and fb_id-->"+url);
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(Tag, response.toString());
                hidepDialog();

                    try {

                        if(response.length()>0){
                            JSONObject object=response.getJSONObject(0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userId", object.getString("id"));
                            editor.commit();
                            String  url=Constant.LOGIN_URL_UPDATE_BY_FB+Constant.getUserId(getApplicationContext());
                            updateFB_ID(Constant.getfbID(getApplicationContext()),url);

                        }else {
                            Intent intent = new Intent(Login.this,  Register.class);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateFB_ID(String id,String url) {
        JSONObject  fbid = new JSONObject();
        try {
            fbid.put("facebook", id);
            System.out.println("dfddf" + fbid);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, fbid, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());
                if (response != null) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("fbid", "");
                    editor.putString("fbfirst_name", "");
                    editor.putString("fblast_name", "");
                    editor.putString("fbemail", "");
                    editor.putString("fbgender", "");
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }

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



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
        super.onBackPressed();
    }



}
