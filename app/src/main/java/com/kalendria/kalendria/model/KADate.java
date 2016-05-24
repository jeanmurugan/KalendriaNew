package com.kalendria.kalendria.model;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Superman on 5/23/16.
 */
public class KADate {

    public int day;
    public int month;
    public int year;
    public int unique;
    public  String dayLongName;

    public KADate(Calendar calender)
    {
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH)+1;// because jan start from 0
        year = calender.get(Calendar.YEAR);

        unique = day*1000000+month*10000+year;

        dayLongName = calender.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

    }
}
