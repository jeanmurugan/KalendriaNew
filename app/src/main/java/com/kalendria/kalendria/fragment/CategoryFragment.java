package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.adapter.CategoryAdapter;
import com.kalendria.kalendria.model.Category;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.SubCategory;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.singleton.DeshBoardTypeSingleton;
import com.kalendria.kalendria.api.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 02/03/16.
 */
public class CategoryFragment extends Fragment {

    List<String> mjsonResonceSingletone;
    List<DeshBoard> mdeshboard_type_singletone;
    GridView gridView;

    CategoryAdapter mcategoryAdapter;
    private SharedPreferences msharedPref;
    TextView tvcatagory_header;
    Button btnsettings,btnnotification;
    ArrayList<Category> categoryPage_models_listlist =new ArrayList<Category>();

    AutoCompleteTextView textView;
    ImageButton homepage_search_img_btn; // Image Button for search view
    ArrayList<String> COUNTRIES = new ArrayList<>();   // This array list is used to save all the Names for filter
    private SharedPreferences sharedPref;
    public CategoryFragment() {
        // Required empty public constructor
        mjsonResonceSingletone = JsonResponce.getInstance().getParamList();
        mdeshboard_type_singletone = DeshBoardTypeSingleton.getInstance().getParamList();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gridview = inflater.inflate(R.layout.category_activity, container, false);
        msharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
         gridView = (GridView) gridview.findViewById(R.id.gridview);
        tvcatagory_header=(TextView)gridview.findViewById(R.id.tvcatagory_header);
        tvcatagory_header.setText(Constant.getTypeName(getActivity()));
        btnsettings=(Button)gridview.findViewById(R.id.btnsettings);
        btnnotification=(Button)gridview.findViewById(R.id.btnnotification);

        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        textView = (AutoCompleteTextView) gridview.findViewById(R.id.category_autocompleted_txt);
        homepage_search_img_btn = (ImageButton) gridview.findViewById(R.id.category_autocompleted_btn);

        btnnotification.setVisibility(View.GONE);
        Jsonparsing() ;

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent intent = new Intent(getActivity(), SubCategory.class);
                SharedPreferences.Editor editor = msharedPref.edit();
                editor.putString("category_id", categoryPage_models_listlist.get(position).getCategoryId());
                editor.putString("categoryName", categoryPage_models_listlist.get(position).getCategoryName());
                editor.commit();
                startActivity(intent);
            }
        });

        homepage_search_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id= Constant.getTypeId(getActivity());
                String mfilterText = textView.getText().toString().trim();
                if (!mfilterText.isEmpty() || mfilterText != null) {
                    SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putString("HdeaderName", mfilterText);
                    editor1.commit();
                    for (int i = 0; i < categoryPage_models_listlist.size(); i++) {
                        if (mfilterText.equalsIgnoreCase(categoryPage_models_listlist.get(i).getCategoryName())) {
                                Intent intent = new Intent(getActivity(), Venue.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("type_id",id );
                                editor.putString("category_id", categoryPage_models_listlist.get(i).getCategoryId());
                                editor.putString("HeaderId", "");
                                editor.putString("ChildId", "");
                               // editor.putString("HdeaderName", "");
                                editor.commit();
                                startActivity(intent);

                        }
                    }
                }


            }
        });

        btnsettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Jsonparsing_right_arrow_clink();

            }
        });

        btnnotification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Jsonparsing_lift_arrow_clink();

            }
        });
        return gridview;
    }

    private void Jsonparsing(){

        String id= Constant.getTypeId(getActivity());
        System.out.println("Category  postion id-->"+id);

        try {
            // Parsing json object response response will be a json object
            String s= null;
            try {
                s = mjsonResonceSingletone.get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("categories list-->"+s);
            if (s != null) {

                JSONObject jsonObject_category =new JSONObject(s);
                JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                for(int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String type=jsonObject.getString("parent");
                    //System.out.println("parent"+jsonObject.getString("parent"));
                    if(type.equalsIgnoreCase(id))
                    {
                        Category categoryPage_model=new Category();
                        categoryPage_model.setCategoryId(jsonObject.getString("id"));
                        categoryPage_model.setCategoryName(jsonObject.getString("name"));
                        System.out.println("Category parants name lsit-->"+" "+ jsonObject.getString("name"));
                        String nameValue = jsonObject.getString("media");
                        System.out.println("Category media lsit-->"+"  "+ nameValue);

                        if(nameValue !="null" ){
                            System.out.println("i am in side");
                            JSONObject jsonObject1=new JSONObject(nameValue);
                            categoryPage_model.setCategoryImages(jsonObject1.getString("medium"));
                            System.out.println("Category media url list"+ jsonObject1.getString("medium"));

                        }

                        COUNTRIES.add(jsonObject.getString("name"));
                        categoryPage_models_listlist.add(categoryPage_model);

                    }

                }
                System.out.println("size of type array"+ categoryPage_models_listlist.size());
               /* for(Category homePage_model: categoryPage_models_listlist) {
                    System.out.println("iiiiiiii"+homePage_model.getCategoryName());
                }*/

                //==== Filter Adapter start
                ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                textView.setAdapter(adapter);
                //==== Filter Adapter start end
               mcategoryAdapter = new CategoryAdapter(categoryPage_models_listlist,getActivity());
                gridView.setAdapter(mcategoryAdapter);



            }else{
                // if responce is null write your commants here
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void Jsonparsing_right_arrow_clink(){
        int counter = 0;
        counter = Integer.parseInt(Constant.homepage_list_postion(getActivity()));
        counter ++;
        System.out.println("count -->"+counter);


        int  key= mdeshboard_type_singletone.size();
        //coded by Magesh
        if(key<=counter+1  ) {
            btnsettings.setVisibility(View.GONE);
        }
        //code end
        System.out.println("array size ==>"+key);
        btnnotification.setVisibility(View.VISIBLE);
        if(key>counter && counter>0 )
        {

            categoryPage_models_listlist.clear();
            COUNTRIES.clear();
            SharedPreferences.Editor editor = msharedPref.edit();
            editor.putString("position_id", String.valueOf(counter));

            DeshBoard deshBoard = mdeshboard_type_singletone.get(counter);
            tvcatagory_header.setText(deshBoard.getTypeName());

            String idnew= deshBoard.getTypeId();
            editor.putString("type_id", idnew);
            editor.commit();

            try {

                // Parsing json object response response will be a json object
                String s= mjsonResonceSingletone.get(0);
                System.out.println("categories list-->"+s);
                if (s != null) {

                    JSONObject jsonObject_category =new JSONObject(s);
                    JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String type=jsonObject.getString("parent");
                        //System.out.println("parent"+jsonObject.getString("parent"));
                        if(type.equalsIgnoreCase(idnew))
                        {
                            Category categoryPage_model=new Category();
                            categoryPage_model.setCategoryId(jsonObject.getString("id"));
                            categoryPage_model.setCategoryName(jsonObject.getString("name"));
                            System.out.println("Category parants name lsit-->"+" "+ jsonObject.getString("name"));
                            String nameValue = jsonObject.getString("media");
                            System.out.println("Category media lsit-->"+"  "+ nameValue);

                            if(nameValue !="null" ){
                                System.out.println("i am in side");
                                JSONObject jsonObject1=new JSONObject(nameValue);
                                categoryPage_model.setCategoryImages(jsonObject1.getString("medium"));
                                System.out.println("Category media url list"+ jsonObject1.getString("medium"));

                            }
                            COUNTRIES.add(jsonObject.getString("name"));
                            categoryPage_models_listlist.add(categoryPage_model);

                        }

                    }
                    System.out.println("size of type array"+ categoryPage_models_listlist.size());
                   /* for(Category homePage_model1: categoryPage_models_listlist) {
                        System.out.println("iiiiiiii"+homePage_model1.getCategoryName());
                    }*/
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                    textView.setAdapter(adapter);
                    mcategoryAdapter = new CategoryAdapter(categoryPage_models_listlist,getActivity());
                    gridView.setAdapter(mcategoryAdapter);



                }else{
                    // if responce is null write your commants here
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else{
            btnsettings.setVisibility(View.GONE);
        }

    }
    
    private void Jsonparsing_lift_arrow_clink(){

        int counter = 0;
        counter = Integer.parseInt(Constant.homepage_list_postion(getActivity()));
        counter --;
        System.out.println("count -->"+counter);

        //coded by Magesh, we have set visibility based on current Index
        if((counter-1)<0)
        {
            btnnotification.setVisibility(View.GONE);
        }
// end
        btnsettings.setVisibility(View.VISIBLE);
        int  key= mdeshboard_type_singletone.size();
        System.out.println("array size ==>"+key);

        if(key>counter && counter>=0 )
        {
            categoryPage_models_listlist.clear();
            COUNTRIES.clear();
            SharedPreferences.Editor editor = msharedPref.edit();
            editor.putString("position_id", String.valueOf(counter));

            DeshBoard deshBoard = mdeshboard_type_singletone.get(counter);
            tvcatagory_header.setText(deshBoard.getTypeName());
            String idnew= deshBoard.getTypeId();
            editor.putString("type_id", idnew);
            editor.commit();


            try {

                // Parsing json object response response will be a json object
                String s= mjsonResonceSingletone.get(0);
                System.out.println("categories list-->"+s);
                if (s != null) {

                    JSONObject jsonObject_category =new JSONObject(s);
                    JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String type=jsonObject.getString("parent");
                        //System.out.println("parent"+jsonObject.getString("parent"));
                        if(type.equalsIgnoreCase(idnew))
                        {
                            Category categoryPage_model=new Category();
                            categoryPage_model.setCategoryId(jsonObject.getString("id"));
                            categoryPage_model.setCategoryName(jsonObject.getString("name"));
                            System.out.println("Category parants name lsit-->"+" "+ jsonObject.getString("name"));
                            String nameValue = jsonObject.getString("media");
                            System.out.println("Category media lsit-->"+"  "+ nameValue);

                            if(nameValue !="null" ){
                                System.out.println("i am in side");
                                JSONObject jsonObject1=new JSONObject(nameValue);
                                categoryPage_model.setCategoryImages(jsonObject1.getString("medium"));
                                System.out.println("Category media url list"+ jsonObject1.getString("medium"));

                            }
                            COUNTRIES.add(jsonObject.getString("name"));
                            categoryPage_models_listlist.add(categoryPage_model);

                        }

                    }
                   /* System.out.println("size of type array"+ categoryPage_models_listlist.size());
                    for(Category homePage_model1: categoryPage_models_listlist) {
                        System.out.println("iiiiiiii"+homePage_model1.getCategoryName());
                    }*/
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
                    textView.setAdapter(adapter);
                    mcategoryAdapter = new CategoryAdapter(categoryPage_models_listlist,getActivity());
                    gridView.setAdapter(mcategoryAdapter);



                }else{
                    // if responce is null write your commants here
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }else{
            btnnotification.setVisibility(View.GONE);
        }
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
