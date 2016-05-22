package com.kalendria.kalendria.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kalendria.kalendria.R;


/**
 * Created by rajaganapathi on 1/14/2016.
 */
public class MyOrdersFragment extends Fragment {

    TextView myOrder_Date, myOrder_Time, myOrder_VenueName, myOrder_ServiceName, myOrder_Price_Point, myOrder_Status;

    public MyOrdersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myorder_row, container, false);

        myOrder_Date = (TextView)rootView.findViewById(R.id.myorder_date);
        myOrder_Time = (TextView)rootView.findViewById(R.id.myorder_time);
        myOrder_VenueName = (TextView)rootView.findViewById(R.id.myorder_venu_name);
        myOrder_ServiceName = (TextView)rootView.findViewById(R.id.myorder_service_name);
        myOrder_Price_Point = (TextView)rootView.findViewById(R.id.myorder_price_or_points);
        myOrder_Status = (TextView)rootView.findViewById(R.id.myorder_status);

        // Inflate the layout for this fragment
        return rootView;
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
