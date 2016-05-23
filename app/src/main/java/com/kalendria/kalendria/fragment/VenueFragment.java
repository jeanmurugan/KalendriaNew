package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Login;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.VenueAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.Category;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.model.SubCategoryChild;
import com.kalendria.kalendria.model.SubCategoryHeader;
import com.kalendria.kalendria.model.Venue;
import com.kalendria.kalendria.singleton.DeshBoardTypeSingleton;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.utility.KalendriaAppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueFragment extends Fragment {


    public static String TAG = VenueFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    public static String Tag = Login.class.getSimpleName();
    ArrayList<Venue> custum_list =new ArrayList<Venue>();
    VenueAdapter adapter1;
    ListView list;
    TextView selected_category;
    private SharedPreferences sharedPref;
    Button btnnotification,filter_btn;

    //Filter start
    TextView filderDateTxt, filderTimeTxt,filter_close_txt;
    TextView category_close,subcategory_close,subcategory2_close,time_close,date_close,gender_close;
    Button filterClearBtn, filterSearchBtn;
    EditText filderPriceFrom, filderPriceTo, filderKeyWordToSerachTxt;
    AutoCompleteTextView typeAutoCompletedText, categoiresAuctoCompletedText, subCategoreisHeaderAutoCompletedText, subCategoriesChildAutoCompletedText, filderGenderAutoCompletedText;

    List<String> mjsonResonceSingletone;
    List<DeshBoard> mdeshboard_type_singletone;

    ArrayList<String> typeStringArray = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<DeshBoard> typeModelArray = new ArrayList<DeshBoard>(); // This array list is used to save all the Names

    ArrayList<String> categoriesStringArray = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<Category> categoriesModelArray = new ArrayList<Category>(); // This array list is used to save all the Names

    ArrayList<String> subcategoriesHeaderstring = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<SubCategoryHeader> subcategoriesHeadermodel = new ArrayList<SubCategoryHeader>(); // This array list is used to save all the Names

    ArrayList<String> subcategoriesChildStringArray = new ArrayList<>();
    ArrayList<SubCategoryChild> subcategoriesChildmodelArray = new ArrayList<SubCategoryChild>();

    ArrayList<String> gender;

    private AlertDialog myDialog;
    private View alertView;

    private AlertDialog myDialog1;
    private View alertView1;

    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    LinearLayout locaion,how_often_LL, categoreyLL, subCategoryHeaderLL, subCategoriyChildLL,LinearLayout6;
    LinearLayout community;

    FrameLayout need_ironing;
    TextView filter_location_home_and_garden,how_often_txt;

    RadioGroup register_radiogroup;
    int pos;
    String radiogroup_value;

    //Filter sent to server values
    String sendTypeIdToServer,sendTo_subcategories,sentTosubcategories2,sentTo_categories_act,sendToServerGender;

    String dayName;
    Button btnsettings;

    RadioGroup radioGroup;
    Button sortButton;
    String keyForSharting="1";
    TextView sortingCloseTxt;
    // Filter set all categories
    String catergoryName="", subcatergoryName="",subcatergoryName2="";



    //Filter end
    public VenueFragment() {
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


        View gridview = inflater.inflate(R.layout.categoryselect, container, false);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());


        LayoutInflater inflateralert = LayoutInflater.from(getActivity());
        alertView = inflateralert.inflate(R.layout.filter, null);
        builder.setView(alertView);

        LayoutInflater inflateralert1 = LayoutInflater.from(getActivity());
        alertView1 = inflateralert1.inflate(R.layout.sharting, null);
        builder1.setView(alertView1);

        //builder.setTitle("Log in");
        //builder.setIcon(R.drawable.snowflake);

        // AutoCompleteTextView for filter

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        typeAutoCompletedText = (AutoCompleteTextView)alertView.findViewById(R.id.filter_type_act);
        categoiresAuctoCompletedText = (AutoCompleteTextView)alertView.findViewById(R.id.filter_categories_act);
        subCategoreisHeaderAutoCompletedText = (AutoCompleteTextView)alertView.findViewById(R.id.filter_subcategories_act1);
        subCategoriesChildAutoCompletedText = (AutoCompleteTextView)alertView.findViewById(R.id.filter_subcategories_act2);
        filderGenderAutoCompletedText = (AutoCompleteTextView)alertView.findViewById(R.id.filter_gender);

//      Homa and garden start
        locaion = (LinearLayout)alertView.findViewById(R.id.locaion);
        categoreyLL = (LinearLayout)alertView.findViewById(R.id.LinearLayout2);
        subCategoryHeaderLL = (LinearLayout)alertView.findViewById(R.id.LinearLayout3);
        subCategoriyChildLL = (LinearLayout)alertView.findViewById(R.id.LinearLayout4);
        LinearLayout6 = (LinearLayout)alertView.findViewById(R.id.LinearLayout6);

        how_often_LL = (LinearLayout)alertView.findViewById(R.id.how_often_LL);
        need_ironing = (FrameLayout)alertView.findViewById(R.id.need_ironing);
        filter_location_home_and_garden = (TextView)alertView.findViewById(R.id.filter_location_home_and_garden);
        how_often_txt = (TextView)alertView.findViewById(R.id.how_often_txt);
        //      Homa and garden end



        category_close = (TextView)alertView.findViewById(R.id.category_close);
        subcategory_close = (TextView)alertView.findViewById(R.id.subcategory_close);
        subcategory2_close = (TextView)alertView.findViewById(R.id.subcategory2_close);
        date_close = (TextView)alertView.findViewById(R.id.date_close);
        time_close = (TextView)alertView.findViewById(R.id.time_close);
        gender_close = (TextView)alertView.findViewById(R.id.option);
        gender_close.setVisibility(View.INVISIBLE);

        // filter
        filderKeyWordToSerachTxt = (EditText)alertView.findViewById(R.id.filter_keyword_search);
        filter_close_txt = (TextView)alertView.findViewById(R.id.filter_close_txt);
        filderPriceFrom = (EditText)alertView.findViewById(R.id.filter_price_from);
        filderPriceTo = (EditText)alertView.findViewById(R.id.filter_price_to);
        filderDateTxt = (TextView)alertView.findViewById(R.id.filter_date);
        filderTimeTxt = (TextView)alertView.findViewById(R.id.filter_time);
        filterClearBtn = (Button)alertView.findViewById(R.id.filter_clear_btn);
        filterSearchBtn = (Button)alertView.findViewById(R.id.filter_search_btn);

        radioGroup = (RadioGroup) alertView1.findViewById(R.id.rgOpinion);
        sortButton = (Button) alertView1.findViewById(R.id.sorting_save_btn);
        sortingCloseTxt = (TextView) alertView1.findViewById(R.id.sharting_close_txt);




        builder.setCancelable(false);
        builder1.setCancelable(false);
        myDialog = builder.create();
        myDialog1 = builder1.create();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        selected_category=(TextView)gridview.findViewById(R.id.selected_category);
        filter_btn=(Button) gridview.findViewById(R.id.filter_btn);
        list = (ListView)gridview.findViewById(R.id.venulist);
        register_radiogroup=(RadioGroup)gridview.findViewById(R.id.register_radiogroup_filter) ;

        btnsettings = (Button) gridview.findViewById(R.id.btnsettings);
        btnnotification=(Button)gridview.findViewById(R.id.btnnotification);






        makeJsonObjectRequest();
        getFiler();
        getDOB();
        onclick();
        return gridview;
    }

    private  void onclick(){

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent intent = new Intent(getActivity(), VenueItem.class);
                SharedPreferences.Editor editor = sharedPref.edit();
               /* editor.putString("HdeaderName", custum_list.get(position).getName());
                editor.putString("ServiceName", custum_list.get(position).getName_service());
                editor.putString("Retting", custum_list.get(position).getOverallRating());*/

                editor.putString("venueID", custum_list.get(position).getId());
              /*  editor.putString("lat", custum_list.get(position).getLat());
                editor.putString("long", custum_list.get(position).getLongId());
                editor.putString("ImageUrl", custum_list.get(position).getImageUrl());*/
                editor.commit();
                startActivity(intent);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.radioLowest:
                        // switch to fragment 1
                        keyForSharting = "1";
                        break;
                    case R.id.radioHighest:
                        // Fragment 2
                        keyForSharting = "2";
                        break;
                    case R.id.radioReview:
                        // Fragment 3
                        keyForSharting = "3";
                        break;
                    case R.id.radioDiscount:
                        // Fragment 3
                        keyForSharting = "4";

                        break;
                    case R.id.radioNearest:
                        // Fragment 3
                        keyForSharting = "5";
                        break;
                }
            }
        });


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterResponce();
            }
        });

        sortingCloseTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.dismiss();
            }
        });
        filterSearchBtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   getFilterResponce();
                                               }
                                           }
        );

        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.show();
            }
        });

        filterClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoiresAuctoCompletedText.setText("");
                category_close.setVisibility(View.GONE);
                subCategoryHeaderLL.setVisibility(View.GONE);
                subCategoriyChildLL.setVisibility(View.GONE);
                filderGenderAutoCompletedText.setText("");
                filderKeyWordToSerachTxt.setText("");
                filderPriceTo.setText("");
                filderPriceFrom.setText("");
                filderDateTxt.setText("");
                filderTimeTxt.setText("");
                date_close.setVisibility(View.INVISIBLE);
                time_close.setVisibility(View.INVISIBLE);
                gender_close.setVisibility(View.INVISIBLE);
                //subcategory2_close.setVisibility(View.INVISIBLE);

            }
        });

        typeAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, typeStringArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                typeAutoCompletedText.setText(typeStringArray.get(which).toString());
                                // System.out.println("1111"+ typeAutoCompletedText.getText().toString());
                                if(typeAutoCompletedText.getText().toString().equalsIgnoreCase("Home & Garden")){
                                    locaion.setVisibility(View.VISIBLE);
                                    how_often_LL.setVisibility(View.VISIBLE);
                                    need_ironing.setVisibility(View.VISIBLE);
                                    LinearLayout6.setVisibility(View.GONE);

                                }else{

                                    subCategoryHeaderLL.setVisibility(View.GONE);
                                    subCategoriyChildLL.setVisibility(View.GONE);
                                    locaion.setVisibility(View.GONE);
                                    how_often_LL.setVisibility(View.GONE);
                                    need_ironing.setVisibility(View.GONE);
                                    LinearLayout6.setVisibility(View.VISIBLE);
                                }

                                String TypeId= Constant.getTypeId(getActivity());
                                for(int i = 0; i< typeModelArray.size(); i++) {
                                    if(typeAutoCompletedText.getText().toString().equalsIgnoreCase(typeModelArray.get(i).getTypeName())){
                                        sendTypeIdToServer= typeModelArray.get(i).getTypeId();
                                        if(!TypeId.equalsIgnoreCase(typeModelArray.get(i).getTypeId())){

                                            categoreyLL.setVisibility(View.VISIBLE);
                                            subCategoryHeaderLL.setVisibility(View.GONE);
                                            subCategoriyChildLL.setVisibility(View.GONE);

                                            categoiresAuctoCompletedText.setText("");
                                            subCategoreisHeaderAutoCompletedText.setText("");
                                            subCategoriesChildAutoCompletedText.setText("");
                                            category_close.setVisibility(View.INVISIBLE);

                                        }
                                    }
                                }
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });

        categoiresAuctoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                for(int i = 0; i< typeModelArray.size(); i++) {
                    if(typeAutoCompletedText.getText().toString().equalsIgnoreCase(typeModelArray.get(i).getTypeName())){
                        sendTypeIdToServer= typeModelArray.get(i).getTypeId();

                    }
                }
                System.out.println("type id"+sendTypeIdToServer);
                categoriesStringArray.clear();
                for(int i = 0; i< categoriesModelArray.size(); i++) {
                    String categoryParentId = categoriesModelArray.get(i).getCategoryImages(); // categorie parant id
                    if (categoryParentId.equalsIgnoreCase(sendTypeIdToServer)) {
                        categoriesStringArray.add(categoriesModelArray.get(i).getCategoryName());
                    }
                }


                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoriesStringArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                categoiresAuctoCompletedText.setText(categoriesStringArray.get(which).toString());
                                category_close.setVisibility(View.VISIBLE);
                                subCategoreisHeaderAutoCompletedText.setText("");
                                subCategoryHeaderLL.setVisibility(View.VISIBLE);
                                subcategory_close.setVisibility(View.INVISIBLE);

                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });
        subCategoreisHeaderAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                for(int i = 0; i< categoriesModelArray.size(); i++) {
                    if(categoiresAuctoCompletedText.getText().toString().equalsIgnoreCase(categoriesModelArray.get(i).getCategoryName())){
                        sentTo_categories_act= categoriesModelArray.get(i).getCategoryId();
                        subCategoriyChildLL.setVisibility(View.VISIBLE);
                        subcategory2_close.setVisibility(View.VISIBLE);

                    }
                }

                subcategoriesHeaderstring.clear();
                for(int i = 0; i< subcategoriesHeadermodel.size(); i++) {

                    String subcategoryParentId = subcategoriesHeadermodel.get(i).getSubcategoryHeaderParent(); // categorie parant id
                    if (subcategoryParentId.equalsIgnoreCase(sentTo_categories_act)) {
                        subcategoriesHeaderstring.add(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName());

                    }
                }
                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subcategoriesHeaderstring);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                subCategoreisHeaderAutoCompletedText.setText(subcategoriesHeaderstring.get(which).toString());
                                subCategoriesChildAutoCompletedText.setText("");
                                subcategory_close.setVisibility(View.VISIBLE);
                                subcategory2_close.setVisibility(View.INVISIBLE);


                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });

        subCategoriesChildAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {



                for(int i = 0; i< subcategoriesHeadermodel.size(); i++){
                    if(subCategoreisHeaderAutoCompletedText.getText().toString().equalsIgnoreCase(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName())){
                        sendTo_subcategories= subcategoriesHeadermodel.get(i).getSubcategoryHeaderId();

                    }
                }

                subcategoriesChildStringArray.clear();

                for(int i = 0; i< subcategoriesChildmodelArray.size(); i++) {

                    String subcategoryParentId2 = subcategoriesChildmodelArray.get(i).getParentId(); // categorie parant id
                    if (subcategoryParentId2.equalsIgnoreCase(sendTo_subcategories)) {
                        subcategoriesChildStringArray.add(subcategoriesChildmodelArray.get(i).getSubcategoryName());

                    }
                }

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subcategoriesChildStringArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                subCategoriesChildAutoCompletedText.setText(subcategoriesChildStringArray.get(which).toString());

                                subcategory2_close.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });



        filderGenderAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gender);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                filderGenderAutoCompletedText.setText(gender.get(which).toString());
                                sendToServerGender= filderGenderAutoCompletedText.getText().toString();
                                gender_close.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });




        filter_close_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });category_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoiresAuctoCompletedText.setText("");
                category_close.setVisibility(View.INVISIBLE);
                subCategoryHeaderLL.setVisibility(View.GONE);
                subCategoriyChildLL.setVisibility(View.GONE);
            }
        });subcategory_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoreisHeaderAutoCompletedText.setText("");
                subCategoriyChildLL.setVisibility(View.GONE);
                subcategory_close.setVisibility(View.INVISIBLE);
            }
        });subcategory2_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subCategoriesChildAutoCompletedText.setText("");
                subcategory2_close.setVisibility(View.INVISIBLE);
            }
        });date_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderDateTxt.setText("");
            }
        });time_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderTimeTxt.setText("");
            }
        });gender_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderGenderAutoCompletedText.setText("");
                gender_close.setVisibility(View.INVISIBLE);
            }
        });



        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TypeId= Constant.getTypeId(getActivity());
                String categoryId= Constant.getCategoryId(getActivity());
                String Headerid= Constant.subCategeryHeaderID(getActivity());
                String HeaderName= Constant.subCategeryHeaderName(getActivity());
                String ChildId= Constant.subCategeryChildId(getActivity());



                //========== Get the type Name ============//
                typeStringArray.clear();
                String typeName="";
                for(int i=0;i<2;i++){
                    typeStringArray.add(typeModelArray.get(i).getTypeName());
                    if(TypeId.equalsIgnoreCase(typeModelArray.get(i).getTypeId())){

                        typeName= typeModelArray.get(i).getTypeName();

                    }
                }
                typeAutoCompletedText.setText(typeName);
                if(typeAutoCompletedText.getText()!=null) {

                    if(typeAutoCompletedText.getText().toString().equalsIgnoreCase("Home & Garden")){
                        locaion.setVisibility(View.VISIBLE);
                        how_often_LL.setVisibility(View.VISIBLE);
                        need_ironing.setVisibility(View.VISIBLE);

                    }
                }
                //========== Get the type end ============//


                //========== Get the categories Name start  ============//
                categoriesStringArray.clear();
                for(int i = 0; i< categoriesModelArray.size(); i++) {

                    String categoryParentId = categoriesModelArray.get(i).getCategoryImages(); // categorie parant id
                    if (categoryParentId.equalsIgnoreCase(TypeId)) {
                        categoriesStringArray.add(categoriesModelArray.get(i).getCategoryName());
                        if(categoriesModelArray.get(i).getCategoryId().equalsIgnoreCase(categoryId)){
                            catergoryName= categoriesModelArray.get(i).getCategoryName();
                        }
                    }
                }

                categoiresAuctoCompletedText.setText(catergoryName);
                //========== Get the categories Name end  ============//


                //========== Get the subcategoriesHeader Name start  ============//
                subcategoriesHeaderstring.clear();

                for(int i = 0; i< subcategoriesHeadermodel.size(); i++) {

                    String subcategoryParentId = subcategoriesHeadermodel.get(i).getSubcategoryHeaderParent(); // categorie parant id
                    if (subcategoryParentId.equalsIgnoreCase(categoryId)) {
                        subcategoriesHeaderstring.add(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName());
                        if(subcategoriesHeadermodel.get(i).getSubcategoryHeaderId().equalsIgnoreCase(Headerid)){
                            subcatergoryName= subcategoriesHeadermodel.get(i).getSubcategoryHeaderName();
                        }
                    }
                }

                subCategoreisHeaderAutoCompletedText.setText(subcatergoryName);

                //========== Get the subcategoriesHeader Name end  ============//


                //========== Get the subcategorieschild Name start  ============//
                subcategoriesChildStringArray.clear();

                for(int i = 0; i< subcategoriesChildmodelArray.size(); i++) {

                    String subcategoryParentId2 = subcategoriesChildmodelArray.get(i).getParentId(); // categorie parant id
                    if (subcategoryParentId2.equalsIgnoreCase(Headerid)) {
                        subcategoriesChildStringArray.add(subcategoriesChildmodelArray.get(i).getSubcategoryName());
                        if(subcategoriesChildmodelArray.get(i).getSubcategoryId().equalsIgnoreCase(ChildId)){
                            subcatergoryName2= subcategoriesChildmodelArray.get(i).getSubcategoryName();
                        }
                    }
                }


                subCategoriesChildAutoCompletedText.setText(subcatergoryName2);
                //========== Get the subcategorieschild Name start  ============//

                gender = new ArrayList<>();
                gender.add("Male");
                gender.add("Female");
                gender.add("Children");
                // ArrayAdapter adapter4 = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, gender);
                //filderGenderAutoCompletedText.setAdapter(adapter4);
                //filderGenderAutoCompletedText.setText("Choose your gender");
                //==== Filter Adapter start end
                myDialog.show();
            }
        });




        btnnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        filderDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });

        filderTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currenttime = Calendar.getInstance();
                int hour = currenttime.get(Calendar.HOUR_OF_DAY);
                int minutes = currenttime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                // have to work
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        filderTimeTxt.setText(hourOfDay + ":" + minute);
                        time_close.setVisibility(View.VISIBLE);

                    }
                },hour,minutes,true);
                timePickerDialog.show();
            }
        });
    }



    private void getDOB() {
        Calendar newCalendar = Calendar.getInstance();
        //newCalendar.add(Calendar.DATE, -1);
        //long a=System.currentTimeMillis()-1;
        //System.out.println("yesterdays date is:"+a);

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {



            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                String dayOfWeek = simpledateformat.format(date);
                System.out.println("day-->"+dayOfWeek);
                dayName=dayOfWeek;
                filderDateTxt.setText(dateFormatter.format(newDate.getTime()));
                date_close.setVisibility(View.VISIBLE);
            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        // toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 12 * 60 * 60 * 1000);



    }


    private void getFiler(){


        try {

            String s= mjsonResonceSingletone.get(0);
            System.out.println("categories list--> "+s);
            if (s != null) {
                JSONObject jsonObject_category =new JSONObject(s);
                JSONArray jsonArray=jsonObject_category.getJSONArray("categorys");

                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String level = jsonObject.getString("level");
                    if(level.equalsIgnoreCase("type")){
                        DeshBoard deshBoard_filder = new DeshBoard();
                        deshBoard_filder.setTypeId(jsonObject.getString("id"));
                        deshBoard_filder.setTypeName(jsonObject.getString("name"));
                        typeStringArray.add(jsonObject.getString("name"));
                        typeModelArray.add(deshBoard_filder);

                    }else if (level.equalsIgnoreCase("category")){
                        Category categoryPage_model=new Category();
                        categoryPage_model.setCategoryId(jsonObject.getString("id"));
                        categoryPage_model.setCategoryName(jsonObject.getString("name"));
                        categoryPage_model.setCategoryImages(jsonObject.getString("parent"));
                        categoriesStringArray.add(jsonObject.getString("name"));
                        categoriesModelArray.add(categoryPage_model);

                    } else if (level.equalsIgnoreCase("sub_category 1")){
                        SubCategoryHeader subcategoryHeader=new SubCategoryHeader();
                        subcategoryHeader.setSubcategoryHeaderId(jsonObject.getString("id"));
                        subcategoryHeader.setSubcategoryHeaderName(jsonObject.getString("name"));
                        subcategoryHeader.setSubcategoryHeaderParent(jsonObject.getString("parent"));
                        subcategoriesHeaderstring.add(jsonObject.getString("name"));
                        subcategoriesHeadermodel.add(subcategoryHeader);

                    } else if (level.equalsIgnoreCase("sub_category 2")){
                        SubCategoryChild subcategory=new SubCategoryChild();
                        subcategory.setSubcategoryId(jsonObject.getString("id"));
                        subcategory.setSubcategoryName(jsonObject.getString("name"));
                        subcategory.setParentId(jsonObject.getString("parent"));
                        subcategoriesChildStringArray.add(jsonObject.getString("name"));
                        subcategoriesChildmodelArray.add(subcategory);
                    }

                }

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



    private void makeJsonObjectRequest() {
        showpDialog();
        String TypeId= Constant.getTypeId(getActivity());
        String categoryId= Constant.getCategoryId(getActivity());
        String Headerid= Constant.subCategeryHeaderID(getActivity());
        String HeaderName= Constant.subCategeryHeaderName(getActivity());
        String ChildId= Constant.subCategeryChildId(getActivity());
        selected_category.setText(HeaderName);

        String url=Constant.HOST+"api/v1/search/search?category="+categoryId+"&category_service="+ChildId+"&category_type="+TypeId+"&city=3&limit=20&sector=1&skip=0&sub_category="+Headerid;
        Log.d(TAG,"--->"+url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"--->"+response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    //System.out.println("murugankldjkdkd"+jsonArray);
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Venue selectedModel=new Venue();
                        selectedModel.setId(jsonObject.getString("id"));
                        selectedModel.setName(jsonObject.getString("name"));
                        selectedModel.setCity(jsonObject.getString("city"));
                        selectedModel.setRegion(jsonObject.getString("region"));
                        selectedModel.setDescription(jsonObject.getString("description"));
                        selectedModel.setLat(jsonObject.getString("lat"));
                        selectedModel.setLongId(jsonObject.getString("long"));
                        selectedModel.setOverallRating(jsonObject.getString("overall_rating"));


                        String nameValue = jsonObject.getString("media");
                        if(nameValue !="null" ) {
                            System.out.println("i am in side");
                            JSONObject jsonObject1=new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));
                            selectedModel.setImageUrlThumb(jsonObject1.getString("thumb"));

                        }

                        String service = jsonObject.getString("services");
                        if(service !="null" ){
                            JSONArray jsonArray1=new JSONArray(service);
                            JSONObject object=jsonArray1.getJSONObject(0);
                            selectedModel.setName_service_id(object.getString("id"));
                            selectedModel.setName_service(object.getString("name"));
                            selectedModel.setPrice(object.getString("price"));
                            selectedModel.setDiscount(object.getString("discount"));
                            selectedModel.setDiscounted_price(object.getString("discounted_price"));
                            selectedModel.setDuration(object.getString("duration"));


                        }
                        custum_list.add(selectedModel);

                    }
                    adapter1 = new VenueAdapter(custum_list,getActivity());
                    list.setAdapter(adapter1);

                } catch (JSONException e) {
                    System.out.println(TAG+e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                // final int statusCode = error.networkResponse.statusCode;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void getFilterResponce() {
        myDialog.dismiss();
        showpDialog();

        for(int i = 0; i< typeModelArray.size(); i++) {
            if(typeAutoCompletedText.getText().toString().equalsIgnoreCase(typeModelArray.get(i).getTypeName())){
                sendTypeIdToServer= typeModelArray.get(i).getTypeId();

            }
        }

        for(int i = 0; i< categoriesModelArray.size(); i++){
            if(categoiresAuctoCompletedText.getText().toString().equalsIgnoreCase(categoriesModelArray.get(i).getCategoryName())){
                sentTo_categories_act= categoriesModelArray.get(i).getCategoryId();

            }
        }

        for(int i = 0; i< subcategoriesHeadermodel.size(); i++){
            if(subCategoreisHeaderAutoCompletedText.getText().toString().equalsIgnoreCase(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName())){
                sendTo_subcategories= subcategoriesHeadermodel.get(i).getSubcategoryHeaderId();

            }
        }

        for(int i = 0; i< subcategoriesChildmodelArray.size(); i++){
            if(subCategoriesChildAutoCompletedText.getText().toString().equalsIgnoreCase(subcategoriesChildmodelArray.get(i).getSubcategoryName())){
                sentTosubcategories2= subcategoriesChildmodelArray.get(i).getSubcategoryId();

            }
        }


        //StringBuilder s = new StringBuilder();
        //s.append("something");

/*
 categoiresAuctoCompletedText.setText("");
                category_close.setVisibility(View.GONE);
                subCategoryHeaderLL.setVisibility(View.GONE);
                subCategoriyChildLL.setVisibility(View.GONE);
                filderGenderAutoCompletedText.setText("");
                filderKeyWordToSerachTxt.setText("");
                filderPriceTo.setText("");
                filderPriceFrom.setText("");
                filderDateTxt.setText("");
                filderTimeTxt.setText("");
                date_close.setVisibility(View.INVISIBLE);
                time_close.setVisibility(View.INVISIBLE);
 */
        String keyword = filderKeyWordToSerachTxt.getText().toString();
        String filter_price_from1 = filderPriceFrom.getText().toString();
        String filter_price_to1 = filderPriceTo.getText().toString();
        String filter_date1 = filderDateTxt.getText().toString();
        String filter_time1 = filderTimeTxt.getText().toString();
        //String sendToServerGender = filderGenderAutoCompletedText.getText().toString();

        if(keyword==null){
            keyword="";
        }
        if(filter_price_from1==null){
            filter_price_from1="";
        }
        if(filter_price_to1==null){
            filter_price_to1="";
        }
        if(filter_date1==null){
            filter_date1="";
        }
        if(filter_time1==null){
            filter_time1="";
        }
        String filter_weekday = "";
        String param ="category="+sentTo_categories_act;
        param += "&category_service="+sentTosubcategories2;
        param += "&category_type="+sendTypeIdToServer;
        param += "&day="+filter_weekday==null?"":filter_weekday;
        param += "&limit=10";
        param += "&sector=1";
        param += "&skip=0";
        param += "&sub_category="+sendTo_subcategories;
        param += "&city=3";
        param += "&date="+filter_date1==null?"":filter_date1;
        param += "&keyword="+keyword;
        param += "&max_price="+filter_price_to1==null?"":filter_price_to1;
        param += "&min_price="+filter_price_from1==null?"":filter_price_from1;
        param += "&region=";
        param += "&time="+(filter_time1==null?"":filter_time1);
        param += "&audience="+(sendToServerGender==null?"":sendToServerGender);
        param += "&how_often=";
        param += "&user_location=";



        if (keyForSharting != null && keyForSharting.length() > 0) {

            String sortingURL = "";
            if (keyForSharting.equalsIgnoreCase("1")) {
                sortingURL = "&sort=discounted_price+ASC";

            } else if (keyForSharting.equalsIgnoreCase("2")) {
                sortingURL = "&sort=discounted_price+DESC";

            } else if (keyForSharting.equalsIgnoreCase("3")) {
                sortingURL = "&sort=overall_rating+DESC";


            } else if (keyForSharting.equalsIgnoreCase("4")) {

                sortingURL = "&sort=discount+DESC";
            }
            else if (keyForSharting.equalsIgnoreCase("5")) {
                sortingURL = "&sort=";

            }
            param +=sortingURL;
        }


        //String va= https://dev.api.kalendria.com/api/v1/search/search?audience=Male&category=2&category_service=36&category_type=1&city=3&limit=10&sector=1&skip=0
        //String va="audience="+sendToServerGender+"&category="+sentTo_categories_act+"&category_service="+sentTosubcategories2+
        // "&category_type="+sendTypeIdToServer+"&city=3&date="+filter_date1+"+00:00:00.0000&day="+dayName+"&keyword="+keyword+"&limit=10
        // &max_price="+filter_price_from1+"&min_price="+filter_price_to1+"&sector=1&skip=0&sub_category="+sendTo_subcategories+"&time="+filter_time1;



        String url=Constant.VENUE_FILTER+param;
        Log.d(TAG,"--->"+url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"--->"+response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    //System.out.println("murugankldjkdkd"+jsonArray);
                    custum_list.clear();
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Venue selectedModel=new Venue();
                        selectedModel.setId(jsonObject.getString("id"));
                        selectedModel.setName(jsonObject.getString("name"));
                        selectedModel.setCity(jsonObject.getString("city"));
                        selectedModel.setRegion(jsonObject.getString("region"));
                        selectedModel.setDescription(jsonObject.getString("description"));
                        selectedModel.setLat(jsonObject.getString("lat"));
                        selectedModel.setLongId(jsonObject.getString("long"));
                        selectedModel.setOverallRating(jsonObject.getString("overall_rating"));


                        String nameValue = jsonObject.getString("media");
                        if(nameValue !="null" ){
                            System.out.println("i am in side");
                            JSONObject jsonObject1=new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));

                        }

                        String service = jsonObject.getString("services");
                        if(service !="null" ){
                            JSONArray jsonArray1=new JSONArray(service);
                            JSONObject object=jsonArray1.getJSONObject(0);
                            selectedModel.setName_service(object.getString("name"));
                            selectedModel.setPrice(object.getString("price"));
                            selectedModel.setDiscounted_price(object.getString("discounted_price"));


                        }
                        custum_list.add(selectedModel);

                    }

                    adapter1 = new VenueAdapter(custum_list,getActivity());
                    list.setAdapter(adapter1);

                } catch (JSONException e) {
                    System.out.println(TAG+e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                final int statusCode = error.networkResponse.statusCode;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    private void getshartingResponce(String url1) {
        myDialog1.dismiss();
        showpDialog();

        String url=Constant.VENUE_FILTER+url1;
        Log.d(TAG,"--->"+url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"--->"+response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    //System.out.println("murugankldjkdkd"+jsonArray);
                    custum_list.clear();
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Venue selectedModel=new Venue();
                        selectedModel.setId(jsonObject.getString("id"));
                        selectedModel.setName(jsonObject.getString("name"));
                        selectedModel.setCity(jsonObject.getString("city"));
                        selectedModel.setRegion(jsonObject.getString("region"));
                        selectedModel.setDescription(jsonObject.getString("description"));
                        selectedModel.setLat(jsonObject.getString("lat"));
                        selectedModel.setLongId(jsonObject.getString("long"));
                        selectedModel.setOverallRating(jsonObject.getString("overall_rating"));


                        String nameValue = jsonObject.getString("media");
                        if(nameValue !="null" ){
                            System.out.println("i am in side");
                            JSONObject jsonObject1=new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));

                        }

                        String service = jsonObject.getString("services");
                        if(service !="null" ){
                            JSONArray jsonArray1=new JSONArray(service);
                            JSONObject object=jsonArray1.getJSONObject(0);
                            selectedModel.setName_service(object.getString("name"));
                            selectedModel.setPrice(object.getString("price"));
                            selectedModel.setDiscounted_price(object.getString("discounted_price"));


                        }
                        custum_list.add(selectedModel);

                    }

                    adapter1 = new VenueAdapter(custum_list,getActivity());
                    list.setAdapter(adapter1);

                } catch (JSONException e) {
                    System.out.println(TAG+e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                final int statusCode = error.networkResponse.statusCode;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                return params;
            }

        };

        //queue.add(jsonObjReq);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
