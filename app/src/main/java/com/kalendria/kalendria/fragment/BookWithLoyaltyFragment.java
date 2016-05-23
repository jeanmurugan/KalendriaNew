package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.kalendria.kalendria.R;


/**
 * Created by murugan on 1/14/2016.
 */
public class BookWithLoyaltyFragment extends Fragment {

    EditText maximum_et,minimum_et;
    ImageView searchImage;
    ListView lv_book_with_loyalty;
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

        return rootView;
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
