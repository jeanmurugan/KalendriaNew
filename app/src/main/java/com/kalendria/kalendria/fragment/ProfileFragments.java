package com.kalendria.kalendria.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;


import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.R;

import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.activity.ResetPassword;
import com.kalendria.kalendria.api.Constant;

import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.Validator;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by murugan on 25/04/16.
 */
public class ProfileFragments extends Fragment {

    // LogCat tag
    private static final String TAG = ProfileFragments.class.getSimpleName();
    Button filter_btn;
    Bitmap bitmap;
    ImageView btnCapturePicture;
    private ProgressDialog pDialog;
    private ProgressDialog progress;
    AsyncTask<String, String, String> task;
    MultipartEntity entity;
    String picturePath;
    StringBuilder builder;

    private static final int CAMERA_REQUEST = 1888; // field
    String radiogroup_value;
    String spinner_selected_id,spinner_name,spinner_type,spinner_parent;
    List<String> cityTextArray;
    ArrayList<com.kalendria.kalendria.model.RegisterSpinner> cityModelArray =new ArrayList<com.kalendria.kalendria.model.RegisterSpinner>();
    public static String Tag = ProfileFragments.class.getSimpleName();
    RadioGroup radioGroup;
    EditText register_username_et,register_lastname_et,register_phone_et,register_email_et,register_address_et;
    TextView register_spinner;
    Button register_submit_btn,register_reset_password_btn;
    View rootView;
    Button btnsettings;

    boolean meditOption=true;

    String mUrlImage,mMediumImage,mThumbImage,mTypeImage,mIsDeletedImage,
            mCreatedAtImage,mUpdatedAtImage,mIdImage,mFileNameImage,mSizeImage,mProviderImage;

    String imageName;
    public ProfileFragments() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.getprofile, container, false);


        btnsettings = (Button) rootView.findViewById(R.id.btnsettings);
        btnCapturePicture = (ImageView) rootView.findViewById(R.id.image_profile);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.register_radiogroup);
        register_spinner = (TextView) rootView.findViewById(R.id.register_spinner);
        register_submit_btn = (Button) rootView.findViewById(R.id.register_submit_btn);
        register_reset_password_btn = (Button) rootView.findViewById(R.id.register_reset_password_btn);

        register_username_et = (EditText) rootView.findViewById(R.id.register_username_et);
        register_lastname_et = (EditText) rootView.findViewById(R.id.register_lastname_et);
        register_email_et = (EditText) rootView.findViewById(R.id.register_email_et);
        register_address_et = (EditText) rootView.findViewById(R.id.register_address_et);

        register_username_et.setFocusable(false);
        register_username_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_username_et.setClickable(false); // user navigates with wheel and selects widget


        register_lastname_et.setFocusable(false);
        register_lastname_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_lastname_et.setClickable(false); // user navigates with wheel and selects widget

        register_phone_et = (EditText) rootView.findViewById(R.id.register_phone_et);
        register_phone_et.setFocusable(false);
        register_phone_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_phone_et.setClickable(false); // user navigates with wheel and selects widget


        register_email_et.setFocusable(false);
        register_email_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_email_et.setClickable(false); // user navigates with wheel and selects widget


        register_address_et.setFocusable(false);
        register_address_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        register_address_et.setClickable(false); // user navigates with wheel and selects widget


        onClickButton();
        get_radio_group_values();

        if (KalendriaAppController.isNetworkConnected(getActivity())){
            getCityList();
            getProfile();
        }else{
            Toast.makeText(getActivity(), "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }


        return rootView;
    }


    public void get_radio_group_values() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                try {
                    switch (checkedId) {
                        case R.id.male_radio_btn:
                            radiogroup_value = "Male";
                            break;

                        case R.id.female_radio_btn:
                            radiogroup_value = "Female";
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
    }


    public  void onClickButton(){
        register_reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPassword.class);
                startActivity(intent);
            }
        });
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(meditOption){
                    meditOption=false;
                    btnsettings.setBackgroundResource(R.drawable.edit_icon);
                    register_submit_btn.setVisibility(View.VISIBLE);
                    register_reset_password_btn.setVisibility(View.GONE);
                    register_username_et.setFocusable(true);
                    register_username_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    register_username_et.setClickable(true); // user navigates with wheel and selects widget


                    register_lastname_et.setFocusable(true);
                    register_lastname_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    register_lastname_et.setClickable(true); // user navigates with wheel and selects widget


                    register_phone_et.setFocusable(true);
                    register_phone_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    register_phone_et.setClickable(true); // user navigates with wheel and selects widget


                    register_email_et.setFocusable(true);
                    register_email_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    register_email_et.setClickable(true); // user navigates with wheel and selects widget


                    register_address_et.setFocusable(true);
                    register_address_et.setFocusableInTouchMode(true); // user touches widget on phone with touch screen
                    register_address_et.setClickable(true); // user navigates with wheel and selects widget
                }else{
                    meditOption=true;
                    register_submit_btn.setVisibility(View.GONE);
                    register_reset_password_btn.setVisibility(View.VISIBLE);
                    btnsettings.setBackgroundResource(R.drawable.delete);
                    register_username_et.setFocusable(false);
                    register_username_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    register_username_et.setClickable(false); // user navigates with wheel and selects widget


                    register_lastname_et.setFocusable(false);
                    register_lastname_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    register_lastname_et.setClickable(false); // user navigates with wheel and selects widget


                    register_phone_et.setFocusable(false);
                    register_phone_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    register_phone_et.setClickable(false); // user navigates with wheel and selects widget


                    register_email_et.setFocusable(false);
                    register_email_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    register_email_et.setClickable(false); // user navigates with wheel and selects widget


                    register_address_et.setFocusable(false);
                    register_address_et.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    register_address_et.setClickable(false); // user navigates with wheel and selects widget
                }


            }
        });
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    selectImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }); register_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(KalendriaAppController.isNetworkConnected(getActivity())){
                    //getCityList();
                    try {
                        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, cityTextArray);

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Kalendria")
                                .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        register_spinner.setText(cityTextArray.get(which).toString());
                                        spinner_selected_id= cityModelArray.get(which).getId();
                                        spinner_name= cityModelArray.get(which).getName();
                                        spinner_type= cityModelArray.get(which).getType();
                                        spinner_parent= cityModelArray.get(which).getParent();
                                        //String imc_met = cityText.getSelectedItem().toString();

                                        dialog.dismiss();
                                    }
                                }).create().show();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getActivity(), "Please check internet... ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        register_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String  username = register_username_et.getText().toString().trim();
                    String  lastName = register_lastname_et.getText().toString().trim();
                    String  city = register_spinner.getText().toString().trim();
                    String  phone = register_phone_et.getText().toString().trim();
                    String   email = register_email_et.getText().toString().trim();
                    String address= register_address_et.getText().toString().trim();


                    if(TextUtils.isEmpty(username)){
                        register_username_et.setError("Please enter Your First Name");
                        register_username_et.requestFocus();
                    }
                    else if(TextUtils.isEmpty(lastName)){
                        register_lastname_et.setError("Please enter Your Last Name");
                        register_lastname_et.requestFocus();
                    }
                    else if(TextUtils.isEmpty(city)){
                        register_spinner.setError("Please enter Your city Name");
                        register_spinner.requestFocus();
                    }
                    else if( TextUtils.isEmpty(phone)){
                        register_phone_et.setError("Please enter Your Number!");
                        register_phone_et.requestFocus();
                    }
                    else if(TextUtils.isEmpty(email) )
                    {
                        register_email_et.setError("Please enter a valid email id");
                        register_email_et.requestFocus();
                    }else if(!Validator.isEmailValid(email)){
                        register_email_et.setError("Please enter a valid email id");
                        register_email_et.requestFocus();
                    }

                   /*  else if(TextUtils.isEmpty(address)){
                         register_address_et.setError("Please enter Your First Name");
                         register_address_et.requestFocus();
                     }*/

                    else {

                        if (KalendriaAppController.getInstance().isNetworkConnected(getActivity())) {
                            makeJsonObjectRequest(username,lastName,city,email,phone,address);
                        }else{
                            Toast.makeText(getActivity(),"Please Check your internet connection",Toast.LENGTH_LONG).show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // =========select image view start===============//

    protected void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Clear Photo"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                   /* Intent cameraIntent = new  Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Clear Photo")) {

                    //profilepicture.setImageResource(R.drawable.profile);

                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // bitmap.recycle();

        return output;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            File f = new File(Environment.getExternalStorageDirectory().toString());

            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    imageName = temp.getName();
                    break;
                }
            }
            try {

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = 8;

                try {
                    // Decode image size
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(new FileInputStream(f), null, o);

                    // The new size we want to scale to
                    final int REQUIRED_SIZE =720;
                    int scale = 1;
                    while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                        scale *= 2;
                    }

                    // Decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                //btnCapturePicture.setImageBitmap(getCircleBitmap(bitmap));
                btnCapturePicture.setImageBitmap(bitmap);

                task = new GET_EXPECT2().execute();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {

            try {
                String sResponse = null;
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);

                File f = new File(picturePath);

                imageName = f.getName();
                bitmap = decodeFile(picturePath);
                btnCapturePicture.setImageBitmap(getCircleBitmap(bitmap));

                c.close();

                task = new GET_EXPECT1().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public Bitmap decodeFile(String filePath) {

        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 300;
            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFile(filePath, o2);

        } catch (Exception e) {
            System.out.println("decodeFile-->" + e.getMessage().toString());
        }
        return bitmap;
    }

    // =======================select imageview
    // end=======================================================//


    private class GET_EXPECT1 extends AsyncTask<String, String, String> {

        String sResponse = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Sign UP!!");
            progress.setMessage("Please wait!");
            progress.setCancelable(false);
            progress.show();

        }

        @SuppressLint("NewApi")
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params1) {

            try {

                builder = new StringBuilder();
                String url = "https://dev.api.kalendria.com/api/v1/media/upload";
                // bitmap = decodeFile(picturePath);

                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);

                entity = new MultipartEntity();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                byte[] data1 = bos.toByteArray();

            /*    entity.addPart("user_id", new StringBody("199"));
                entity.addPart("club_id", new StringBody("10"));*/
                System.out.println("imageName-->"+imageName);
                entity.addPart("file", new ByteArrayBody(data1, imageName));
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                sResponse = EntityUtils.getContentCharSet(response.getEntity());

                StatusLine statusLine = response.getStatusLine();
                System.out.println("statusLine...." + statusLine);
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity1 = response.getEntity();
                    InputStream content = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    System.out.println("sResponse : " + builder);
                } else {
                    Log.e("class", "Failed to download file");
                    HttpEntity entity1 = response.getEntity();
                    InputStream content = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    //System.out.println("sResponse : " + builder.toString());
                }


            } catch (Exception e) {

                Log.e(e.getClass().getName(), e.getMessage(), e);
            }

            return builder.toString();

        }


        @Override
        protected void onPostExecute(String result) {

            if (progress.isShowing()) {
                progress.dismiss();
            }
           // System.out.println("---->result : " + builder.toString());

            try {
                JSONArray jsonArray=new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    mUrlImage=object.getString("url");
                    mMediumImage=object.getString("medium");
                    mThumbImage=object.getString("thumb");
                    mTypeImage=object.getString("type");
                    mIsDeletedImage=object.getString("isDeleted");
                    mCreatedAtImage=object.getString("createdAt");
                    mUpdatedAtImage=object.getString("updatedAt");
                    mIdImage=object.getString("id");
                    mFileNameImage=object.getString("filename");
                    mSizeImage=object.getString("size");
                    mProviderImage=object.getString("provider");

                    System.out.println("url-->"+object.getString("url"));
                    System.out.println("medium-->"+object.getString("medium"));
                    System.out.println("thumb-->"+object.getString("thumb"));
                    System.out.println("type-->"+object.getString("type"));
                    System.out.println("isDeleted-->"+object.getString("isDeleted"));
                    System.out.println("createdAt-->"+object.getString("createdAt"));
                    System.out.println("updatedAt-->"+object.getString("updatedAt"));
                    System.out.println("id-->"+object.getString("id"));
                    System.out.println("filename-->"+object.getString("filename"));
                    System.out.println("size-->"+object.getString("size"));
                    System.out.println("provider-->"+object.getString("provider"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private class GET_EXPECT2 extends AsyncTask<String, String, String> {

        String sResponse = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            progress = new ProgressDialog(getActivity());
            progress.setTitle("Sign UP!!");
            progress.setMessage("Please wait!");
            progress.setCancelable(false);
            progress.show();

        }

        @SuppressLint("NewApi")
        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params1) {


            try {

                builder = new StringBuilder();
                String url ="https://dev.api.kalendria.com/api/v1/media/upload";
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);

                entity = new MultipartEntity();

            /*    entity.addPart("user_id", new StringBody("199"));
                entity.addPart("club_id", new StringBody("10"));*/

                byte[]  photoimageByte = new byte[0];
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                    photoimageByte = baos.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("imageName-->"+imageName);
                entity.addPart("file", new ByteArrayBody(photoimageByte, imageName));
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                sResponse = EntityUtils.getContentCharSet(response.getEntity());

                StatusLine statusLine = response.getStatusLine();
                System.out.println("statusLine...." + statusLine);
                int statusCode = statusLine.getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity1 = response.getEntity();
                    InputStream content = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    System.out.println("sResponse : " + builder);
                } else {
                    Log.e("class", "Failed to download file");
                    HttpEntity entity1 = response.getEntity();
                    InputStream content = entity1.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    //System.out.println("sResponse : " + builder.toString());
                }

            } catch (Exception e) {

                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
            return builder.toString();

        }


        @Override
        protected void onPostExecute(String result) {

            if (progress.isShowing()) {
                progress.dismiss();
            }
            //System.out.println("---->result : " + builder.toString());
            try {
                JSONArray jsonArray=new JSONArray( builder.toString());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);

                    mUrlImage=object.getString("url");
                    mMediumImage=object.getString("medium");
                    mThumbImage=object.getString("thumb");
                    mTypeImage=object.getString("type");
                    mIsDeletedImage=object.getString("isDeleted");
                    mCreatedAtImage=object.getString("createdAt");
                    mUpdatedAtImage=object.getString("updatedAt");
                    mIdImage=object.getString("id");
                    mFileNameImage=object.getString("filename");
                    mSizeImage=object.getString("size");
                    mProviderImage=object.getString("provider");

                    System.out.println("url-->"+object.getString("url"));
                    System.out.println("medium-->"+object.getString("medium"));
                    System.out.println("thumb-->"+object.getString("thumb"));
                    System.out.println("type-->"+object.getString("type"));
                    System.out.println("isDeleted-->"+object.getString("isDeleted"));
                    System.out.println("createdAt-->"+object.getString("createdAt"));
                    System.out.println("updatedAt-->"+object.getString("updatedAt"));
                    System.out.println("id-->"+object.getString("id"));
                    System.out.println("filename-->"+object.getString("filename"));
                    System.out.println("size-->"+object.getString("size"));
                    System.out.println("provider-->"+object.getString("provider"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }


    private void getProfile() {


        showpDialog();
        String url=Constant.GET_RROFILE+Constant.getUserId(getActivity());
        System.out.println("getprofile_url-->"+url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String first_name = response.getString("first_name");
                        String last_name = response.getString("last_name");
                        String email = response.getString("email");
                        String city = response.getString("city");
                        String gender = response.getString("gender");
                        String phone = response.getString("phone");
                        String address = response.getString("address");
                        String profile_image = response.getString("profile_image");
                        System.out.println("imagepfofile_muru"+profile_image);


                        register_username_et.setText(first_name);
                        register_lastname_et.setText(last_name);
                        register_email_et.setText(email);
                        register_address_et.setText(address);
                        register_phone_et.setText(phone);

                        if(!city.equals("")){
                            for(int i=0;i<cityModelArray.size();i++){

                                    if(city.equalsIgnoreCase(cityModelArray.get(i).getId())){
                                        register_spinner.setText(cityModelArray.get(i).getName());
                                    }
                            }
                        }

                            if(!profile_image.equalsIgnoreCase("null")){
                                JSONObject object=new JSONObject(profile_image);
                                String imageUrl=object.getString("thumb");
                                Picasso.with(getActivity())
                                        .load(imageUrl)
                                        // .memoryPolicy(MemoryPolicy.NO_CACHE )
                                        // .networkPolicy(NetworkPolicy.NO_CACHE)
                                        //.resize(720, 350)
                                        // .error(R.drawable.login)
                                        .placeholder(R.drawable.profile)
                                        .noFade()
                                        // .fit().centerCrop()
                                        .into(btnCapturePicture);
                            }
                        }


                } catch (Exception e) {
                    e.printStackTrace();
                   // Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                           // Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void makeJsonObjectRequest( String username,String lastName,String city,String email,String phone,String address) {

        JSONObject parentData = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {


            parentData.put("phone",phone);
            parentData.put("type","customer");
            parentData.put("total_bookings","0");
            parentData.put("city",spinner_selected_id);
            parentData.put("id",Constant.getUserId(getActivity()));
            parentData.put("email", email);
            parentData.put("first_name",username);
            parentData.put("last_name",lastName);
            parentData.put("address",address);
            parentData.put("createdAt","");
            parentData.put("send_marketing_email",true);
            parentData.put("role","3");
            parentData.put("lastBookingAt","");

            parentData.put("points",0);
            parentData.put("credit",0.0);
            parentData.put("total_amount_spent",0.0);
            if(!Constant.getfbID(getActivity()).isEmpty() ||!Constant.getfbEmail(getActivity()).isEmpty()){
                parentData.put("isVerified","true");
            }else{
                parentData.put("isVerified","false");
            }
            parentData.put("is_active",true);
            parentData.put("total_points_spent",0);

            parentData.put("isActivationEmailSent","true");
            parentData.put("lastReminderAt","");
            parentData.put("is_app_installed","");
            parentData.put("isDeleted","");
            parentData.put("isVerifiedEmailSent","");
            parentData.put("last_login","");
            parentData.put("updatedAt","");
            parentData.put("reminderCount",0);
            parentData.put("verifyToken","");
            parentData.put("like_count",0);


            jsonObject.put("url",mUrlImage);
            jsonObject.put("filename",mFileNameImage);
            jsonObject.put("size",mSizeImage);
            jsonObject.put("provider",mProviderImage);
            jsonObject.put("medium",mMediumImage);
            jsonObject.put("thumb",mThumbImage);
            jsonObject.put("type",mTypeImage);
            jsonObject.put("isDeleted",mIsDeletedImage);
            jsonObject.put("createdAt",mCreatedAtImage);
            jsonObject.put("updatedAt",mUpdatedAtImage);
            jsonObject.put("id",mIdImage);

            parentData.put("profile_image", jsonObject);

            System.out.println("Register json object-->"+parentData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,Constant.UPDATE_RROFILE+Constant.getUserId(getActivity()), parentData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

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
                        Log.e("Error 111",json);
                        Toast.makeText(getActivity(), "A record with that `email` already exists ", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111",e.getMessage());
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

    private void getCityList() {

        showpDialog();
        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.REGISTER_SPINNER, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(Tag, response.toString());

                    try {
                        // Parsing json object response response will be a json object
                        if (response != null) {


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

                           /* try {
                                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_dropdown_item, cityTextArray);

                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Kalendria")
                                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int which) {
                                                register_spinner.setText(cityTextArray.get(which).toString());
                                                spinner_selected_id= cityModelArray.get(which).getId();
                                                spinner_name= cityModelArray.get(which).getName();
                                                spinner_type= cityModelArray.get(which).getType();
                                                spinner_parent= cityModelArray.get(which).getParent();
                                                //String imc_met = cityText.getSelectedItem().toString();

                                                dialog.dismiss();
                                            }
                                        }).create().show();



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
*/
                        }else{
                            // if responce is null write your commants here
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    hidepDialog();
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


            // Adding request to request queue
            KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
