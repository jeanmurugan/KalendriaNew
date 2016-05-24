package com.kalendria.kalendria.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kalendria.kalendria.utility.KalendriaAppController;

/**
 * Created by murugan on 26/02/16.
 */
public class Constant {

    public static final String MyPREFERENCES ="KALENDRIA";

 public final static String HOST ="https://dev.api.kalendria.com/";

   //public final static String HOST ="https://api.kalendria.com/";

    public final static String LOGIN_URL = HOST+"api/v1/auth/local";
    public final static String LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_EMAIL=HOST+"api/v1/user?email=";
    public final static String LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_GPLUS=HOST+"api/v1/user?google=";
    public final static String LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_FB=HOST+"api/v1/user?facebook=";
    public final static String LOGIN_URL_UPDATE_BY_FB=HOST+"api/v1/user/";
    public final static String DESHBORAD =HOST+"api/v1/external/items?service=0&venue=1&category=1&location=1";
    public final static String FORGATPASSWORD = HOST+"api/v1/auth/reset";
    public final static String REGISTER_SPINNER = HOST+"api/v1/external/items?location=1";
    public final static String REGISTER = HOST+"api/v1/user";
    public final static String POST_FAVORITE = HOST+"api/v1/like";
    public final static String FAVORATE =HOST+"api/v1/like?populate=business&user=";
    public final static String REVIEW = HOST+"api/v1/review?";
    public final static String REVIEW_STATUS =HOST+"api/v1/review?";
    public final static String POST_REVIEW =HOST+"api/v1/review";
    public final static String VENUE_FILTER =HOST+"api/v1/search/search?";
    public final static String GET_RROFILE =HOST+"api/v1/user/";
    public final static String UPDATE_RROFILE =HOST+"api/v1/user/";
    public final static String RESET_PASSWORD =HOST+"api/v1/user/";
    public final static String CHECK_OLD_PASSWROD =HOST+"api/v1/auth/local";


    public final static String MYODER ="https://dev.api.kalendria.com/api/v1/booking?limit=10&populate=business&skip=0&where={%22customer%22:17,%22scheduledAt%22:{%22%3E%22:%222016-05-06T15:31:12+05:30%22}}";
    public final static String MYODER_PAST ="https://dev.api.kalendria.com/api/v1/booking?limit=10&populate=business&skip=0&where={\"customer\":17,\"scheduledAt\":{\"<=\":\"2016-05-06T15:31:12+05:30\"}}";


    public static String getUserId(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("userId", "");

    }

    public static String getTypeId(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("type_id", "");

    }
    public static String getTypeName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("type_name", "");

    }

    public static String homepage_list_postion(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("position_id", "");

    }

    public static String getCategoryId(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("category_id", "");

    } public static String getCategoryName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("categoryName", "");

    }

    public static String subCategeryHeaderID(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("HeaderId", "");

    }
    public static String subCategeryHeaderName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("HdeaderName", "");

    }

    public static String subCategeryChildId(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("ChildId", "");

    }

    public static String subCategeryServiceName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("ServiceName", "");

    }




    public static String getRatting(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("Retting", "");

    } public static String getVenueId(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("venueID", "");

    }
    public static String getVenueName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("venueName", "");

    }
    public static String getVenueLat(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("lat", "");

    } public static String getVenulont(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("long", "");

    } public static String getVenuSelecedImageUrl(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("ImageUrl", "");

    }


    //Register data by FB start

    public static String getfbID(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("fbid", "");

    } public static String getfbEmail(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("fbemail", "");

    } public static String getfbFirstName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("fbfirst_name", "");

    } public static String getfbLastName(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("fblast_name", "");

    } public static String getfbGender(final Context context)
    {
        final SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString("fbgender", "");

    }
    //Register data by FB start end


    //code by Magesh
    static  SharedPreferences preferences;


    public static  void initPreference()
    {
        Context context= KalendriaAppController.getInstance().getApplicationContext();
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getFirstName()
    {
        return preferences.getString("kFirstNameKey", "");

    }
    public static String getLastName()
    {
         return preferences.getString("kLastNameKey", "");

    }

    public static String getEmail()
    {
        return preferences.getString("kEmailKey", "");

    }
    public static String getCity()
    {
        return preferences.getString("kCityKey", "");

    }
    public static String getProfileImage()
    {
        return preferences.getString("kProfileImageKey", "");

    }

    public static String getWallet()
    {
        return preferences.getString("kWalletsKey", "");

    }

    public static String getLoyality()
    {
        return preferences.getString("kLoyalityKey", "");

    }


    public static void setFirstName(String firstName)
    {
        savedData(firstName,"kFirstNameKey");
    }
    public static void setLastName(String lastName)
    {
        savedData(lastName,"kLastNameKey");
    }
    public static void setEmail(String email)
    {
        savedData(email,"kEmailKey");
    }
    public static void setCity(String city)
    {
        savedData(city,"kCityKey");
    }
    public static void setProfileImage(String ProfileImage)
    {
        savedData(ProfileImage,"kProfileImageKey");
    }


    public static void savedData(String data, String key)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,data); // value to store
        editor.commit();
    }


}
