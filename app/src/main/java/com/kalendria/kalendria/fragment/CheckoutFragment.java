package com.kalendria.kalendria.fragment;

/**
 * Created by murugan on 2/05/2016
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.adapter.CheckOutAdapter;
import com.kalendria.kalendria.adapter.CustomAdapter;
import com.kalendria.kalendria.adapter.WeekAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.TimeBean;
import com.kalendria.kalendria.model.WeekView;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CheckoutFragment extends Fragment  {
    TextView week;
    Button per,next;
    RecyclerView daysList;
    View view;
    ArrayList<WeekView>weekViews;
    Context ctx;
    int  count=0;
    final ArrayList<String> days=new ArrayList<>();
    final ArrayList<String> days_name=new ArrayList<>();
    List<AddToCardVenueModel> addToCardSingletone;
    List<AddToCardVenueModel> addToCardArray;
    ArrayList<AddToCardServiceModel> service=new ArrayList<AddToCardServiceModel>();
    ArrayList<AddToCardServiceModel> service_dboup=new ArrayList<AddToCardServiceModel>();
    CheckOutAdapter checkOutAdapter;
    ListView list,listView;
    double value,value1;
    int y,zd,y1,zd1;
    ArrayList<Double> date_add;
    ArrayList<TimeBean> myList = new ArrayList<TimeBean>();
    List<String> time_value;
    WeekAdapter weekAdapter;
    boolean checkCurrentDay;
    private double addMinute(int hr, int min, int toadd){
        Calendar calander = Calendar.getInstance();
        calander.set(Calendar.HOUR_OF_DAY, hr);
        calander.set(Calendar.MINUTE, min);
        calander.add(Calendar.MINUTE, toadd);

        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        String formattedDate3 = df3.format(calander.getTime());

        value = Double.parseDouble(formattedDate3.replace(":",".") );
        // System.out.println("mmmmmmmm"+value);
        return value;
    }
    private double addMinute1(int hr, int min, int toadd){
        Calendar calander = Calendar.getInstance();
        calander.set(Calendar.HOUR_OF_DAY, hr);
        calander.set(Calendar.MINUTE, min);
        calander.add(Calendar.MINUTE, toadd);
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        String formattedDate3 = df3.format(calander.getTime());
        value1 = Double.parseDouble(formattedDate3.replace(":",".") );
        return value1;
    }

    /*To obtain the current data or not */

    String currenDayForCheckTime;
    public CheckoutFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.week_days, container, false);
        daysList = (RecyclerView) view.findViewById(R.id.days_list);
        week = (TextView) view.findViewById(R.id.week);
        per =(Button) view.findViewById(R.id.pre);
        next =(Button) view.findViewById(R.id.next);
        list =(ListView) view.findViewById(R.id.list);
        listView = (ListView) view.findViewById(R.id.add_time_list);

        cartList();
        setCustomWeekCalender();
        onClickMethod();

        return view;
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

                        service_dboup.add(addToCardServiceModel);

                    }
                }
            }

            checkOutAdapter = new CheckOutAdapter( service_dboup,getActivity());
            list.setAdapter(checkOutAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCustomWeekCalender(){
        /*set the custom calender view start */
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance(Locale.US);
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



        String[] daysT=weekViews.get(0).getCurrentWeekDays();
        for(String da:daysT){
            days.add(da);  //days in numbers
        }
        //System.out.println("Date : " + days);

          /*get the days of week start */
        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] dayNames = symbols.getShortWeekdays();

        for (String s : dayNames) {
            if(!s.equals("")) {
                days_name.add(s.toUpperCase()); /*days in name */
            }
        }

      /*get the days of week end */
        currenDayForCheckTime=weekViews.get(0).getCurrentDay();
        final int index = days.indexOf(weekViews.get(0).getCurrentDay());
       /* System.out.println("Current position :  " + index);
        System.out.println("Current Date :  " + weekViews.get(0).getCurrentDay());
        System.out.println("Current Date 1 :  " + days.get(index));*/

        weekAdapter = new WeekAdapter(getActivity(),days, days_name, index); /*days and days_name for display and index for round current date */
        daysList.setAdapter(weekAdapter);

        /*set the custom calender view end*/

        /*set the custom time ie current day on time with add 15m upto end of the shop start */

        if(days.get(index).equals(weekViews.get(0).getCurrentDay()))  {


            SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
            String formattedDate3 = df3.format(c.getTime());
            value = Double.parseDouble(formattedDate3.replace(":", "."));
            date_add = new ArrayList<>();
            for (double i= value; i <=22.45; i=+addMinute(y,zd,15))
            {
                try {
                    String aString = Double.toString(value);
                    String first = aString.substring(0, aString.indexOf("."));
                    String second = aString.substring(aString.indexOf(".") + 1, aString.length());
                    y = Integer.parseInt(first);
                    zd = Integer.parseInt(second);
                    value = +addMinute(y, zd, 15);
                    date_add.add(value);

                }catch (Exception e)
                {
                    System.out.println("Exception : " +e.getMessage());
                }
            }

            time_value = new ArrayList<String>();

            for (Double d : date_add) {
                // Apply formatting to the string if necessary
                time_value.add(d.toString());
            }

            String[] stockArr = new String[time_value.size()];
            stockArr = time_value.toArray(stockArr);

            for(String s : stockArr) {
                TimeBean ld = new TimeBean();
                ld.setCal_time(s);
                myList.add(ld);
            }

            listView.setAdapter(new CustomAdapter(getActivity(), myList));

        }

        /*set the month name and year on the top of the calender view start */
        int monthName = Integer.parseInt(weekViews.get(0).getCurrentMonth());
        week.setText(getMonth(monthName) + " " + weekViews.get(0).getCurrentYear());

          /*set the custom time ie current day on time with add 15m upto end of the shop end */
    }

    public void onClickMethod() {


        weekAdapter.SetOnItemClickListener(new WeekAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v , int position) {

                String[] daysT=weekViews.get(count).getCurrentWeekDays();
               for(int i=0;i<daysT.length;i++){

                   if(currenDayForCheckTime.equalsIgnoreCase(daysT[i]));{
                       checkCurrentDay=true;
                   }
               }

                if(checkCurrentDay)
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
                    String formattedDate3 = df3.format(c.getTime());
                    value = Double.parseDouble(formattedDate3.replace(":", "."));
                    date_add = new ArrayList<>();
                    for (double i= value; i <=22.45; i=+addMinute(y,zd,15))
                    {
                        try {

                            String aString = Double.toString(value);
                            String first = aString.substring(0, aString.indexOf("."));
                            String second = aString.substring(aString.indexOf(".") + 1, aString.length());
                            y = Integer.parseInt(first);
                            zd = Integer.parseInt(second);
                            value = +addMinute(y, zd, 15);
                            date_add.add(value);

                        }catch (Exception e)
                        {
                            System.out.println("Exception : " +e.getMessage());
                        }
                    }

                    time_value = new ArrayList<String>();

                    for (Double d : date_add) {
                        time_value.add(d.toString());
                    }

                    String[] stockArr = new String[time_value.size()];
                    stockArr = time_value.toArray(stockArr);

                    myList.clear();

                    for(String s : stockArr) {
                        TimeBean ld = new TimeBean();
                        ld.setCal_time(s);
                        myList.add(ld);
                    }

                    listView.setAdapter(new CustomAdapter(getActivity(), myList));

                } else {

                    date_add.clear();
                    value1=09.00;
                    for (double i= value1; i <=22.45; i=+addMinute1(y1, zd1, 15))
                    {

                        try {

                            String aString = Double.toString(value1);
                            String first = aString.substring(0, aString.indexOf("."));
                            String second = aString.substring(aString.indexOf(".") + 1, aString.length());
                            y1 = Integer.parseInt(first);
                            zd1 = Integer.parseInt(second);
                            value1 = +addMinute1(y1, zd1, 15);
                            date_add.add(value1);

                        }catch (Exception e)
                        {
                            System.out.println("Exception : " +e.getMessage());
                        }
                    }

                    time_value = new ArrayList<String>();

                    for (Double d : date_add) {
                        time_value.add(d.toString());
                    }

                    String[] stockArr = new String[time_value.size()];
                    stockArr = time_value.toArray(stockArr);
                    myList.clear();
                    for(String s : stockArr) {

                        TimeBean ld = new TimeBean();
                        ld.setCal_time(s);
                        myList.add(ld);
                    }

                    listView.setAdapter(new CustomAdapter(getActivity(), myList));
                }

            }
        });
/*set the onclick listner for recycle view end  */


        per.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    if (count > -1 && count <= 6) {
                        count--;
                        days.clear();
                        int monthName = Integer.parseInt(weekViews.get(count).getCurrentMonth());
                        week.setText(getMonth(monthName) + " " + weekViews.get(count).getCurrentYear());
                        String[] daysT = weekViews.get(count).getCurrentWeekDays();
                        for (String da : daysT) {
                            days.add(da);
                        }

                        int index = days.indexOf(weekViews.get(0).getCurrentDay());

                        if(count==0){
                            WeekAdapter weekAdapter = new WeekAdapter(getActivity(), days, days_name,index);
                            daysList.setAdapter(weekAdapter);
                            weekAdapter.notifyDataSetChanged();
                        }else{
                            WeekAdapter weekAdapter = new WeekAdapter(getActivity(), days, days_name,32);
                            daysList.setAdapter(weekAdapter);
                            weekAdapter.notifyDataSetChanged();
                        }

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
                    days.clear();
                    int monthName = Integer.parseInt(weekViews.get(count).getCurrentMonth());
                    week.setText(getMonth(monthName) + " " + weekViews.get(count).getCurrentYear());
                    String[] daysT = weekViews.get(count).getCurrentWeekDays();
                    for (String da : daysT) {
                        days.add(da);
                    }
                    int index = days.indexOf(weekViews.get(0).getCurrentDay());
                    if(count==0){
                        WeekAdapter weekAdapter = new WeekAdapter(getActivity(), days, days_name,index);
                        daysList.setAdapter(weekAdapter);
                        weekAdapter.notifyDataSetChanged();
                    }else{
                        WeekAdapter weekAdapter = new WeekAdapter(getActivity(), days, days_name,32);
                        daysList.setAdapter(weekAdapter);
                        weekAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private static String[] getDaysOfWeek(Date refDate, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        String[] daysOfWeek = new String[7];
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i] = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
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
}
