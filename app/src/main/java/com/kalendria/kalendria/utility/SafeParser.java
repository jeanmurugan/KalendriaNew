package com.kalendria.kalendria.utility;

import org.json.JSONObject;import java.lang.Exception;import java.lang.Integer;import java.lang.String;

/**
 * Created by sdinewimac on 5/18/16.
 */
public class SafeParser {

    public static String getString(JSONObject object,String key, String defValue)
    {
        try {


            if (object.has(key)) {
                String stVlaue = object.getString(key);
                return stVlaue==null?defValue:stVlaue;
            }
            else
                return defValue;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return defValue;
        }

    }

    public static boolean getBoolen(JSONObject object,String key, boolean defValue)
    {
        try {


            return  object.getBoolean(key);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            String value = getString(object,key,"0");

            if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("True") || value.equalsIgnoreCase("yes") )
                return true;
            else   if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("False") || value.equalsIgnoreCase("no") )
                return false;

            try {
                return Integer.parseInt(value)>0 ? true:false;
            }
            catch (Exception exx)
            {
                exx.printStackTrace();
                return defValue;
            }




        }
        finally {

        }
    }

    public static int getInt(JSONObject object,String key, int defValue)
    {
        try {


            return  object.getInt(key);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            String value = getString(object,key,"0");


            try {
                return Integer.parseInt(value);
            }
            catch (Exception exx)
            {
                exx.printStackTrace();
                return defValue;
            }

        }

    }

}
