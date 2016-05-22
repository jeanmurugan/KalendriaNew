package com.kalendria.kalendria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.adapter.AddToCardAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.FragmentDrawer;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.fragment.ProfileFragments;
import com.kalendria.kalendria.fragment.DashBoardFragment;
import com.kalendria.kalendria.fragment.FavoriteFragment;
import com.kalendria.kalendria.fragment.MyOrdersFragment;
import com.kalendria.kalendria.fragment.LogoutFragment;
import com.kalendria.kalendria.fragment.BookWithLoyaltyFragment;
import com.kalendria.kalendria.fragment.AddToCartFragment;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashBoard extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener  {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    TextView   tv;
    List<AddToCardVenueModel> addToCardSingletone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        getUserProfileInformation();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv=(TextView) findViewById(R.id.tv);

        ImageView cart_image =(ImageView)findViewById(R.id.cart_image);

        cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayView(5);
            }
        });

        setSupportActionBar(mToolbar);


        //code by Magesh


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


       if( drawerFragment.getAllowEnterTransitionOverlap()){

       }

        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        displayView(0);

    }
    public void dispatchInformations(String mesg){

        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
        ArrayList<AddToCardServiceModel> items;
        int sum = 0;
            for (int i = 0; i < addToCardSingletone.size(); i++) {
                items = (addToCardSingletone.get(i).getItems());
                sum = sum+items.size();
            }
        String countv=String.valueOf(sum);
        tv.setText(countv);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        if (id == R.id.action_search) {
            //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            if (drawerFragment.mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                drawerFragment.mDrawerLayout.closeDrawer(Gravity.RIGHT);

            }

            else
                drawerFragment.mDrawerLayout.openDrawer(Gravity.RIGHT);
            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position) {
            case 0:
                fragment = new DashBoardFragment();
                //title = getString(R.string.nav_item_home);
                break;
            case 1:
                fragment = new ProfileFragments();
                //title = getString(R.string.nav_item_friends);
                break;
            case 2:
                fragment = new MyOrdersFragment();
               // title = getString(R.string.nav_item_notifications);
                break;
            case 3:
                fragment = new BookWithLoyaltyFragment();
                //title = getString(R.string.nav_item_profile);
                break;
            case 4:
                fragment = new FavoriteFragment();
               // title = getString(R.string.nav_item_images);
                break;
            case 5:
                fragment = new AddToCartFragment();
               // title = getString(R.string.nav_item_settings);
                break;
            case 6:
                fragment = new LogoutFragment();
                //title = getString(R.string.nav_item_match_preference);
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
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

    private void getUserProfileInformation() {



        String url= Constant.GET_RROFILE+Constant.getUserId(this);
        System.out.println("getprofile_url-->" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("DashBoard", response.toString());

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
                        String points = response.getString("points");
                        String wallets = response.getString("credit");
                        System.out.println("imagepfofile_muru" + profile_image);

                        Constant.setCity(city);
                        Constant.setFirstName(first_name);
                        Constant.setLastName(last_name);
                        Constant.setEmail(email);
                        Constant.setProfileImage(profile_image);
                        Constant.savedData(gender, "kGenderKey");
                        Constant.savedData(address, "kAddressKey");
                        Constant.savedData(phone, "kphoneKey");
                        Constant.savedData(points,"kLoyalityKey");
                        Constant.savedData(wallets,"kWalletsKey");

                        drawerFragment.setUserDetails();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
               // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("DashBoard", "Error: " + error.getMessage());
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
}
