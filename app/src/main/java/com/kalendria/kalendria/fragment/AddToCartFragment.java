package com.kalendria.kalendria.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.AddToCardAdapter;
import com.kalendria.kalendria.adapter.SubCategroryAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.FilerModel;
import com.kalendria.kalendria.model.SubCategoryChild;
import com.kalendria.kalendria.model.SubCategoryHeader;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.singleton.DeshBoardTypeSingleton;
import com.kalendria.kalendria.singleton.JsonResponce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddToCartFragment extends Fragment {

    View view;
    List<AddToCardVenueModel> addToCardSingletone;
    ListView listView;
    ArrayList<Object> people = new ArrayList<>();


    public AddToCartFragment() {
        // Required empty public constructor
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        listView = (ListView) view.findViewById(R.id.subcatagory_list);
        Jsonparsing();



        return view;
    }


    private void Jsonparsing() {


        ArrayList<AddToCardServiceModel> items;
        if (addToCardSingletone.size() > 0) {

            for (int i = 0; i < addToCardSingletone.size(); i++) {

                AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
                addToCardVenueModel.setVenueID(addToCardSingletone.get(i).getVenueID());
                addToCardVenueModel.setVenueName(addToCardSingletone.get(i).getVenueName());
                addToCardVenueModel.setVenuImage(addToCardSingletone.get(i).getVenuImage());
                addToCardVenueModel.setCity(addToCardSingletone.get(i).getCity());
                addToCardVenueModel.setRegion(addToCardSingletone.get(i).getRegion());

                people.add(addToCardVenueModel);

                items = (addToCardSingletone.get(i).getItems());

                for (int j = 0; j < items.size(); j++) {
                            /*get the child details */
                    AddToCardServiceModel addToCardServiceModel = new AddToCardServiceModel();
                    addToCardServiceModel.setServiceId(items.get(j).getServiceId());
                    addToCardServiceModel.setServiceName(items.get(j).getServiceName());
                    addToCardServiceModel.setServicePrice(items.get(j).getServicePrice());
                    addToCardServiceModel.setServiceDiscount(items.get(j).getServiceDiscount());
                    addToCardServiceModel.setServiceDuration(items.get(j).getServiceDuration());
                    // get the parant detials
                    addToCardServiceModel.setVenueID(addToCardSingletone.get(i).getVenueID());
                    addToCardServiceModel.setVenueName(addToCardSingletone.get(i).getVenueName());
                    addToCardServiceModel.setVenuImage(addToCardSingletone.get(i).getVenuImage());
                    addToCardServiceModel.setCity(addToCardSingletone.get(i).getCity());
                    addToCardServiceModel.setRegion(items.get(j).getServiceDuration());

                    people.add(addToCardServiceModel);


                }
            }
            listView.setAdapter(new AddToCardAdapter(getActivity(), people));
        }


    }


}



