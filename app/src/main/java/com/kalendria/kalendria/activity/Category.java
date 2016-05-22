package com.kalendria.kalendria.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.fragment.CategoryFragment;
import com.kalendria.kalendria.fragment.DashBoardFragment;
import com.kalendria.kalendria.fragment.ProfileFragments;
import com.kalendria.kalendria.fragment.FavoriteFragment;
import com.kalendria.kalendria.fragment.MyOrdersFragment;
import com.kalendria.kalendria.fragment.LogoutFragment;
import com.kalendria.kalendria.fragment.BookWithLoyaltyFragment;
import com.kalendria.kalendria.fragment.AddToCartFragment;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.FragmentDrawer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 18/02/16.
 */
public class Category extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    TextView tv;
    List<AddToCardVenueModel> addToCardSingletone;

    public Category() {
        // Required empty public constructor

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv=(TextView) findViewById(R.id.tv);

        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
        ArrayList<AddToCardServiceModel> items;
        int sum = 0;
        for (int i = 0; i < addToCardSingletone.size(); i++) {
            items = (addToCardSingletone.get(i).getItems());
            sum = sum+items.size();
        }
        String countv=String.valueOf(sum);
        tv.setText(countv);


        ImageView cart_image=(ImageView)findViewById(R.id.cart_image);
        cart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment cart = null;
                cart = new AddToCartFragment();

                if (cart != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, cart);
                    fragmentTransaction.commit();

                }
            }
        });
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);
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

        Fragment pro = null;
        pro = new CategoryFragment();

        if (pro != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, pro);
            fragmentTransaction.commit();

        }
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
            if (drawerFragment.mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
                drawerFragment.mDrawerLayout.closeDrawer(Gravity.RIGHT);
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


}
