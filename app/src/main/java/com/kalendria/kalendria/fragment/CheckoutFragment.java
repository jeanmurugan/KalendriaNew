package com.kalendria.kalendria.fragment;

/**
 * Created by murugan on 2/05/2016
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.adapter.CheckOutAdapter;
import com.kalendria.kalendria.adapter.CustomAdapter;
import com.kalendria.kalendria.adapter.StaffListAdapter;
import com.kalendria.kalendria.adapter.WeekAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.KADate;
import com.kalendria.kalendria.model.KAStaff;
import com.kalendria.kalendria.model.TimeBean;
import com.kalendria.kalendria.model.VenueDay;
import com.kalendria.kalendria.model.WeekView;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KAJsonArrayRequest;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CheckoutFragment extends Fragment implements CustomAdapter.OnItemClickListener,CheckOutAdapter.CheckOutAdapterDelegate {
    TextView week;
    Button per,next;
    RecyclerView daysList;
    View view;
    ArrayList<WeekView>weekViews;
    Context ctx;
    int  count=0;
    final ArrayList<KADate> days=new ArrayList<>();
    final ArrayList<String> days_name=new ArrayList<>();
    List<AddToCardVenueModel> addToCardSingletone;
    List<AddToCardVenueModel> addToCardArray;
    ArrayList<AddToCardServiceModel> service=new ArrayList<AddToCardServiceModel>();
    ArrayList<AddToCardServiceModel> service_dboup=new ArrayList<AddToCardServiceModel>();
    CheckOutAdapter checkOutAdapter;
    CustomAdapter customAdapter; //added by Magesh
    ListView list,listView;
    double value,value1;
    int y,zd,y1,zd1;
    ArrayList<Double> date_add;
    ArrayList<TimeBean> myList = new ArrayList<TimeBean>();
    String[] arrTime;
    List<String> time_value;
    WeekAdapter weekAdapter;
    boolean checkCurrentDay;
    KADate selectedDay;
    String selectedTime;
    private ProgressDialog pDialog;
    private LinkedHashMap dictBusiness;
    private LinkedHashMap dictWorkingHours;
    StaffListAdapter staffListAdapter =null;

    Button checkOutButton;
    TextView txtTotalPrice;

    private AlertDialog staffDialog;
    private View alertView;
    private ListView staffListView;

    int selectedItemIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        view = inflater.inflate(R.layout.week_days, container, false);
        daysList = (RecyclerView) view.findViewById(R.id.days_list);
        week = (TextView) view.findViewById(R.id.week);
        per =(Button) view.findViewById(R.id.pre);
        next =(Button) view.findViewById(R.id.next);
        list =(ListView) view.findViewById(R.id.list);
        listView = (ListView) view.findViewById(R.id.add_time_list);
        checkOutButton=(Button) view.findViewById(R.id.checkout_btn);
        txtTotalPrice =(TextView)view.findViewById(R.id.price);

        dictBusiness = new LinkedHashMap();
        dictWorkingHours = new LinkedHashMap();
        getWorkingHours();
        loadTimes();
        cartList();
        initStafPicker();
        setCustomWeekCalender();
        onClickMethod();

        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onItemClick(TimeBean currentListData, int position)
    {
        //int index = ArrayUtils.indexOf(currentListData.getCal_time());
        selectedTime = currentListData.getCal_time();

    }
    public void  loadTimes()
    {
        arrTime = new String[] {"07:00","07:15","07:30","07:45","08:00","08:15","08:30","08:45",
            "09:00","09:15","09:30","09:45","10:00","10:15","10:30","10:45",
            "11:00","11:15","11:30","11:45","12:00","12:15","12:30","12:45",
            "13:00","13:15","13:30","13:45","14:00","14:15","14:30","14:45",
            "15:00","15:15","15:30","15:45","16:00","16:15","16:30","16:45",
            "17:00","17:15","17:30","17:45","18:00","18:15","18:30","18:45",
            "19:00","19:15","19:30","19:45","20:00","20:15","20:30","20:45",
            "21:00","21:15","21:30","21:45","22:00","22:15","22:30","22:45",
            "23:00","23:15","23:30","23:45"};

    }
    public void resetAppointmentDate()
    {

        int totalAmount =0;
        int index = ArrayUtils.indexOf(arrTime,selectedTime);

        int hour =0;
        int min  =0;
        Calendar calander = Calendar.getInstance();
        if(selectedTime!=null && selectedTime.length()>2)
        {
            String dateStr = selectedDay.year+"-"+selectedDay.month+"-"+selectedDay.day+" ";
            dateStr +=selectedTime==null?"9:00":selectedTime;
            checkAvailability(dateStr);
        }


        for (int i=0;i<service_dboup.size();i++)
        {
            AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
            addToCardServiceModel.selectedDate= selectedDay.day+"-"+selectedDay.month+"-"+selectedDay.year;
            totalAmount+=addToCardServiceModel.remainAmount;

           /*
            calander.set(Calendar.HOUR_OF_DAY, hr);
            calander.set(Calendar.MINUTE, min);
            calander.add(Calendar.MINUTE, toadd);
            SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
            String formattedDate3 = df3.format(calander.getTime());
*/
            VenueDay dayObject = (VenueDay) dictWorkingHours.get(selectedDay.day);

            if(dayObject!=null)
            addToCardServiceModel.isOpen=dayObject.isOpen();

            if(index<arrTime.length && index>-1)
            addToCardServiceModel.selectedTime=arrTime[index++];
            else {

                if(dayObject!=null)
                addToCardServiceModel.selectedTime = dayObject.getStart_time()+"-"+dayObject.getEnd_time();
            }
        }
        txtTotalPrice.setText(""+totalAmount+" AED");
        checkOutAdapter.notifyDataSetChanged();
    }

    public void cartList(){
        try {
            addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */
            addToCardArray=new ArrayList<>();
            for(int i=0;i<addToCardSingletone.size();i++) {
                if(Constant.getVenueId(getActivity()).equalsIgnoreCase(addToCardSingletone.get(i).getVenueID())){

                    AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
                    addToCardVenueModel.setVenueID(addToCardSingletone.get(i).getVenueID());
                    addToCardVenueModel.setVenueName(addToCardSingletone.get(i).getVenueName());
                    addToCardVenueModel.setVenuImage(addToCardSingletone.get(i).getVenuImage());
                    addToCardVenueModel.setCity(addToCardSingletone.get(i).getCity());
                    addToCardVenueModel.setRegion(addToCardSingletone.get(i).getRegion());

                    service=addToCardSingletone.get(i).getItems();

                    for(int j=0;j<service.size();j++) {
                        AddToCardServiceModel addToCardServiceModel=new AddToCardServiceModel();

                        addToCardServiceModel.setServiceId(service.get(j).getServiceId());
                        addToCardServiceModel.setServiceId2(service.get(j).getServiceId2());
                        addToCardServiceModel.setServiceName(service.get(j).getServiceName());
                        addToCardServiceModel.setServicePrice(service.get(j).getServicePrice());
                        addToCardServiceModel.setServiceDiscount(service.get(j).getServiceDiscount());
                        addToCardServiceModel.setServiceDuration(service.get(j).getServiceDuration());

                        /*get the venue details along with service */
                        addToCardServiceModel.setVenueID(service.get(j).getVenueID());
                        addToCardServiceModel.setVenueName(service.get(j).getVenueName());
                        addToCardServiceModel.setVenuImage(service.get(j).getVenuImage());
                        addToCardServiceModel.setCity(service.get(j).getCity());
                        addToCardServiceModel.setRegion(service.get(j).getRegion());
                        addToCardServiceModel.calculateDiscountAmount();

                        service_dboup.add(addToCardServiceModel);

                    }
                }
            }

            checkOutAdapter = new CheckOutAdapter( service_dboup,getActivity(),this);
            list.setAdapter(checkOutAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCustomWeekCalender(){
        /*set the custom calender view start */
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance(Locale.US);

        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMin = c.get(Calendar.MINUTE);
        int rem = nowMin%15;
        nowMin += (15-rem);



        weekViews = new ArrayList<WeekView>();
        c.add(Calendar.DATE, 0);
        for(int i=0;i<7;i++) {
            String currentDate = (String)(formattedDate.format(c.getTime()));
            WeekView weekView = new WeekView();
            weekView.setCurrentDate(currentDate);
            weekView.setCurrentWeek(c.get(Calendar.WEEK_OF_MONTH));
            weekView.setCurrentDay(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
            weekView.setCurrentMonth(Integer.toString(c.get(Calendar.MONTH )+1));
            weekView.setCurrentYear(Integer.toString(c.get(Calendar.YEAR)));
            weekView.setCurrentWeekDays(getDaysOfWeek(c.getTime(), c.getFirstDayOfWeek()));
            weekViews.add(weekView);
            c.add(Calendar.DATE, 7);
        }

        daysList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        daysList.setLayoutManager(llm);



          /*get the days of week start */
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] dayNames = symbols.getShortWeekdays();

        for (String s : dayNames) {
            if(!s.equals("")) {
                days_name.add(s.toUpperCase()); /*days in name */
            }
        }


        weekAdapter = new WeekAdapter(getActivity(),days, days_name, 0); /*days and days_name for display and index for round current date */
        daysList.setAdapter(weekAdapter);

        ArrayList<KADate> daysT=weekViews.get(0).getCurrentWeekDays();
        days.addAll(daysT);

        int idx=0;
        for (KADate date : daysT)
        {
            // Get Today KADate Object
            if(date.unique==weekAdapter.todayUniqueID)
            {
                selectedDay=date;
                weekAdapter.selectedIndex=date.unique;
                break;
            }
            idx++;
        }

        // View created, so show current date time
        loadAvailableTimes(true,selectedDay.dayLongName);
    }


    public void setCustomAdapterListner()
    {

        customAdapter.SetOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TimeBean currentListData, int position) {
                customAdapter.selectedIndex = position;
                customAdapter.notifyDataSetChanged();
                selectedTime = currentListData.getCal_time();
                resetAppointmentDate();

            }
        });

    }

    /**
     *  discussion, reloading Month Horiztal listview
     * @param index, week index
     */
    public void reloadDaysWheel(int index)
    {
        int monthName = Integer.parseInt(weekViews.get(count).getCurrentMonth());
        week.setText(getMonth(monthName) + " " + weekViews.get(count).getCurrentYear());

        days.clear();
        ArrayList<KADate> daysT=weekViews.get(count).getCurrentWeekDays();
        days.addAll(daysT);

        selectedTime=arrTime[0];
        weekAdapter.refreshDates(getActivity(), days, days_name, 0);
        weekAdapter.notifyDataSetChanged();

        resetAppointmentDate();


    }

    /**
     * Load bottom time listview based on selected date
     */
    public void loadAvailableTimes(boolean isToday, String dayLongname)
    {

        int intStartIndex=0;
        int intTodayIndex=0;
        int endIndex=0;
        if(isToday) { // if Today then we have to show time from now to 22:55
            Calendar c = Calendar.getInstance(Locale.US);

            int nowHour = c.get(Calendar.HOUR_OF_DAY);
            int nowMin = c.get(Calendar.MINUTE);
            int rem = nowMin % 15;
            nowMin += (15 - rem);
            String strTime = String.format("%02d", nowHour) + ":" + String.format("%02d", nowMin);
            intStartIndex = ArrayUtils.indexOf(arrTime, strTime);
        }

        {
            if(dictWorkingHours!=null && dictWorkingHours.size()>0)
            {

                VenueDay dayObject = (VenueDay) dictWorkingHours.get(dayLongname.toLowerCase());
                intTodayIndex = ArrayUtils.indexOf(arrTime, dayObject.getStart_time());
                endIndex = ArrayUtils.indexOf(arrTime, dayObject.getEnd_time());

            }
        }
        if(intTodayIndex>intStartIndex)
            intStartIndex=intTodayIndex;

        if(intStartIndex<0)intStartIndex=0;
        if(endIndex<0)endIndex=0;
        if(endIndex>arrTime.length)endIndex=arrTime.length;
        myList.clear();

        // Load all time value into myList array
        for (int i= intStartIndex; i <=endIndex; i++)
        {
            TimeBean ld = new TimeBean();
            ld.setCal_time(arrTime[i]);
            myList.add(ld);
        }
        if(myList.size()>0) {
            TimeBean tb = myList.get(0);
            selectedTime = tb.getCal_time();
        }
        if(customAdapter==null) {
            customAdapter = new CustomAdapter(getActivity(), myList, this);
            listView.setAdapter(customAdapter);
            setCustomAdapterListner();
        }
        customAdapter.notifyDataSetChanged();

        int monthName = Integer.parseInt(weekViews.get(0).getCurrentMonth());
        week.setText(getMonth(monthName) + " " + weekViews.get(0).getCurrentYear());
        resetAppointmentDate();
    }


    public void onClickMethod() {

        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        list.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });






        weekAdapter.SetOnItemClickListener(new WeekAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                // count=position;
                ArrayList<KADate> daysT = weekViews.get(count).getCurrentWeekDays();

                // code modified by Magesh
                selectedDay = daysT.get(position);
                if (selectedDay.unique == weekAdapter.todayUniqueID) {
                    checkCurrentDay = true;
                } else
                    checkCurrentDay = false;
                weekAdapter.selectedIndex = selectedDay.unique;
                customAdapter.selectedIndex = 0;
                loadAvailableTimes(checkCurrentDay, selectedDay.dayLongName);
                weekAdapter.notifyDataSetChanged();
            }

        });
/*set the onclick listner for recycle view end  */


        per.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    if (count > -1 && count <= 6) {
                        count--;

                        reloadDaysWheel(count);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (count < 6) {
                    count++;
                    reloadDaysWheel(count);

                }

            }
        });

    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private static ArrayList<KADate> getDaysOfWeek(Date refDate, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
       ArrayList<KADate> daysOfWeek =  new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            KADate date = new KADate(calendar);
            daysOfWeek.add(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void onShowStaffPicker(int position,AddToCardServiceModel data)
    {
        tempEmpolyees.clear();
        selectedItemIndex=position;
        showStaffPicker();

    }
    public void showStaffPicker()
    {

        staffDialog.show();
        if(arrEmployes!=null)
        tempEmpolyees.addAll(arrEmployes);

        staffListAdapter.notifyDataSetChanged();
    }

    ArrayList tempEmpolyees;
    public void initStafPicker()
    {

        /*set the tag for book now button */
        Context context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflateralert = LayoutInflater.from(context);
        alertView = inflateralert.inflate(R.layout.staffpicker, null);
        builder.setView(alertView);

        staffListView = (ListView) alertView.findViewById(R.id.staffList);
        Button closeButton = (Button)alertView.findViewById(R.id.cross_image_addto_card);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffDialog.dismiss();
            }
        });

        builder.setCancelable(true);
        staffDialog = builder.create();

        if(tempEmpolyees==null)
            tempEmpolyees=new ArrayList();


        staffListAdapter = new StaffListAdapter(getActivity(),tempEmpolyees);
        staffListView.setAdapter(staffListAdapter);

        staffListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staffDialog.dismiss();
                KAStaff objStaff = (KAStaff)arrEmployes.get(position);
                AddToCardServiceModel addToCardServiceModel = service_dboup.get(selectedItemIndex);
                addToCardServiceModel.staffname=objStaff.firstName;
                addToCardServiceModel.staffID= objStaff.empid;
                checkOutAdapter.notifyDataSetChanged();
                validateStaff();
            }
        });
    }


    boolean isCorrectTime=true;
    private void validateCheckout(){

        if(isCorrectTime == true){
            //btnCheckout.backgroundColor =  UIColor(hexString: "#0097DB")
            //btnCheckout.enabled = true

        }else{
            //btnCheckout.backgroundColor = UIColor.lightGrayColor()
            //btnCheckout.enabled = false
        }
        validateStaff();

    }
    private void validateStaff(){

        boolean isValid = true;
        for (int i=0;i<service_dboup.size();i++) {
            AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
            if(addToCardServiceModel.staffname==null|| addToCardServiceModel.staffname.length()==0)
            {
                isValid=false;
                break;
            }

        }


        if (isValid){
            checkOutButton.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorCheckout));
           checkOutButton.setEnabled(true);

        }
        else
        {
            //0097DB
            checkOutButton.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorGrey));
            checkOutButton.setEnabled(true);
        }


    }


    ArrayList arrEmployes ;
    private  void getStaffDetails()
    {
        for(int i=0;i<arrEmpIds.size();i++) {
            String empID = (String)arrEmpIds.get(i);
            String url = Constant.HOST + "api/v1/service/"+empID+"/employees";

            RequestFuture<JSONArray> future = RequestFuture.newFuture();
            KAJsonArrayRequest jsonObjReq = new KAJsonArrayRequest(Request.Method.GET, url, null, future, future) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");

                    return params;
                }
            };;
            try {
                KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

                JSONArray arrResponse = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);



                int em=0;
               // for (int em=0;em<arrResponse.length();em++)
                {
                    try {


                        JSONObject dictStaf = arrResponse.getJSONObject(em);
                        KAStaff objStaff = new KAStaff();
                        objStaff.empid = dictStaf.getString("id");
                        objStaff.firstName = SafeParser.getString(dictStaf, "first_name", "");
                        objStaff.lastName = SafeParser.getString(dictStaf, "last_name", "");
                        objStaff.gender = SafeParser.getString(dictStaf, "gender", "");
                        objStaff.email = SafeParser.getString(dictStaf, "email", "");
                        objStaff.businesName = SafeParser.getString(dictStaf, "business_name", "");
                        objStaff.business = SafeParser.getString(dictStaf, "business", "");

                        if (dictStaf.has("profile_image")) {
                            JSONObject dictProfileImg = dictStaf.getJSONObject("profile_image");
                            if (dictProfileImg != null) {
                                objStaff.imgUrlNormal = SafeParser.getString(dictStaf, "thumb", "");
                                objStaff.imgUrlThumb = SafeParser.getString(dictStaf, "medium", "");
                                objStaff.imgUrlMedium = SafeParser.getString(dictStaf, "url", "");
                            }
                        }
                        arrEmployes.add(objStaff);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }

    private void checkAvailability(final String datestr) {
        showpDialog();
        new AsyncTask<Void, Void, String>() {


            @Override
            protected String doInBackground(Void... params) {
                getAvailableEmployIds(datestr);

                if(arrEmployes==null)
                    arrEmployes = new ArrayList();
                else
                    arrEmployes.clear();

                getStaffDetails();

                return  "";
            }

            @Override
            protected void onPostExecute(String msg) {

                hideDialogOnMainThread();


            }
        }.execute(null, null, null);
    }

    private void hideDialogOnMainThread() {
        Handler mainHandler = new Handler(KalendriaAppController.getInstance().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                hidepDialog();
            } // This is your code
        };
        mainHandler.post(myRunnable);
    }

    public int REQUEST_TIMEOUT =30;
    ArrayList arrEmpIds = new ArrayList();
    private  void getAvailableEmployIds(String datestr)
    {
       // showpDialog();
        String url =Constant.HOST+"api/v1/external/available";

        JSONObject parameter = new JSONObject();
        JSONArray arrSevices = new JSONArray();
        for (int i = 0; i < service_dboup.size(); i++) {
            try {


                AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
                String sid =  addToCardServiceModel.getServiceId2();

                arrSevices.put(sid);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        try {

            parameter.put("services", arrSevices);
            parameter.put("date", datestr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        KAJsonArrayRequest jsonObjReq = new KAJsonArrayRequest(Request.Method.POST, url, parameter,future,future)
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        try {

            KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
            JSONArray arrResponse = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            if (arrResponse != null) {

                arrEmpIds.clear();

                for (int i = 0; i < arrResponse.length(); i++) {

                    JSONObject dictEmp = arrResponse.getJSONObject(i);
                    String empID = SafeParser.getString(dictEmp, "employee", "");
                    if (empID != null && empID.length() > 0) {
                        if(arrEmpIds.indexOf(empID)<=0)
                            arrEmpIds.add(empID);
                    }
                }

            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    String TAG = "CheckOutFragment";
    private void getWorkingHours() {

        showpDialog();
        String venueID = Constant.getVenueId(getActivity());
        String url =Constant.HOST+"api/v1/business/"+venueID+"?populate=medias,working_hours,region,city";
        //System.out.println("VeneItemFragement-->"+url);
        Log.d(TAG, url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {

                        if(dictBusiness==null)
                            dictBusiness= new LinkedHashMap();

                        dictBusiness.put(response.getString("name"), "name");// get the Venue Name
                        dictBusiness.put(response.getString("overall_rating"), "overall_rating");


                        /* set ratting particular venue page end  */
                        if (response.has("city") && response.getJSONObject("city") != null) {
                            JSONObject cityObject = response.getJSONObject("city");
                            String city = SafeParser.getString(cityObject, "name", "");
                            dictBusiness.put(city, "city");
                        }

                        if (response.has("region") && response.getJSONObject("region") != null) {
                            JSONObject cityObject = response.getJSONObject("region");
                            String city = SafeParser.getString(cityObject, "name", "");
                            dictBusiness.put(city, "city");
                        }
                        dictBusiness.put(SafeParser.getString(response, "phone", ""), "phone");
                        dictBusiness.put(SafeParser.getString(response, "email", ""), "email");
                        dictBusiness.put(SafeParser.getString(response, "website", ""), "website");


                        Date dNow = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("EEEE");
                        String todayDay= ft.format(dNow);
                        boolean isTodayleave = false;
                        JSONArray jsonArray = response.getJSONArray("working_hours");
                        if(dictWorkingHours==null)
                            dictBusiness=new LinkedHashMap();
                        else
                        dictBusiness.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VenueDay selectedVenuDay = new VenueDay();

                                String dayStr = jsonObject.getString("day");

                                selectedVenuDay.setDay(dayStr);

                                selectedVenuDay.setOpen(SafeParser.getBoolen(jsonObject, "isOpen", false));

                                selectedVenuDay.setStart_time(jsonObject.getString("start_time"));
                                selectedVenuDay.setEnd_time(jsonObject.getString("end_time"));
                                String orderStr = jsonObject.getString("order");
                                if (orderStr != null)
                                    selectedVenuDay.order = Integer.parseInt(orderStr);
                                else
                                    selectedVenuDay.order = 0;

                                if(dayStr.equalsIgnoreCase(todayDay))
                                    isTodayleave=true;

                                dictWorkingHours.put(dayStr,selectedVenuDay);


                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                        }

                        loadAvailableTimes(true,selectedDay.dayLongName);


                    } else {
                        // if responce is null write your commants here
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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
    }
}
