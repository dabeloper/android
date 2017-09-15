package com.dabeloper.android.other;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 31/07/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

import com.dabeloper.android.AppApplication;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 13/06/2017.
 */

public class UserPreferences {

    private static String PREFERENCES_FILE  = "app_privia_me";

    private static String TAG_SHOW_RATE     = "show_rate";
    private static String TAG_DT_SHOW_RATE  = "show_rate_dt";
    private static String TAG_FIRST_LAUNCH  = "first_launch";
    private static String TAG_LANGUAGE      = "language";
    private static String TAG_CHECK_VERSION = "next_check";
    private static String TAG_TOKEN         = "authentication_code";
    private static String TAG_DOWNLOADS     = "downloaded_privias";

    private static Locale currentLocale     =  new Locale( "es" , "ES" );
    private static SharedPreferences.Editor editor;

    public static void resetTimeToCheckVersion( Context context ){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Long now = System.currentTimeMillis();
        //EACH 12 HOURS
        editor.putLong(TAG_CHECK_VERSION, now + (12 * 60 * 60 * 1000) );
        editor.apply();
        editor.commit();
    }//END resetTimeToCheckVersion



    public static boolean isTimeToCheckVersion( Context context ){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        Long next_check_dt = settings.getLong(TAG_CHECK_VERSION, 0);
        if( next_check_dt==0 ){
            resetTimeToCheckVersion(context);
        }else {
            if ( System.currentTimeMillis() >= next_check_dt ) {
                return true;
            }
        }

        return false;
    }//END isTimeToCheckVersion


    public static void firstLaunch(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Long date_firstLaunch = System.currentTimeMillis();
        editor.putLong(TAG_FIRST_LAUNCH, date_firstLaunch);
        editor.putLong(TAG_DT_SHOW_RATE, date_firstLaunch);
        //EACH 12 HOURS
        editor.putLong(TAG_CHECK_VERSION, date_firstLaunch+ (12 * 60 * 60 * 1000) );
        editor.putBoolean(TAG_SHOW_RATE, true);
        editor.apply();
        editor.commit();
    }//END firstLaunch


    public static void rateViewed(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(TAG_SHOW_RATE, false);
        editor.apply();
        editor.commit();
    }//END rateViewed



    public static void rateSkiped(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Long now = System.currentTimeMillis();
        editor.putLong(TAG_DT_SHOW_RATE, now);
        editor.apply();
        editor.commit();
    }//END rateSkiped



    public static boolean showRate(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        if( !settings.getBoolean( TAG_SHOW_RATE , false) ) return false;

        Long show_rate_dt = settings.getLong(TAG_DT_SHOW_RATE, 0);
        if( show_rate_dt==0 ){
            rateSkiped(context);
        }else{
            if (System.currentTimeMillis() >= show_rate_dt + (3 * 24 * 60 * 60 * 1000)) {
                return true;
            }
        }

        return false;
    }//END showRate



    public static void saveDownloads( Context context , int count ){
        if( count<0 ) return;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt( TAG_DOWNLOADS , getDownloads(context)+count );
        editor.apply();
        editor.commit();
    }//END saveDownloads



    public static int getDownloads( Context context  ){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return settings.getInt( TAG_DOWNLOADS , 0 );
    }//END getDownloads


    public static void saveLanguage(Context context, String lang ){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString( TAG_LANGUAGE , lang);
        editor.apply();
        editor.commit();

//        MainActivity.resetLanguage();
//        PriviaFragment.resetLanguage();
//        MyPriviasFragment.resetLanguage();
//        MyHistoryFragment.resetLanguage();
//        PriviaApplication.resetLanguage();
    }//END saveLanguage



    public static String getLanguage( Context context /*, String fromTAG */ ){
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        String lang = settings.getString( TAG_LANGUAGE , Locale.getDefault().getLanguage() );
//        System.out.println( "getLanguage from "+ fromTAG + " : " + lang );
//        System.out.println( "Language steps > get All :: "+ settings.getAll() );
        switch ( lang ){
            case "en": currentLocale =  Locale.ENGLISH;
                break;
            case "es": currentLocale =  new Locale( "es" , "ES" );
                break;
            case "pt": currentLocale =  new Locale( "pt" , "PT" );
                break;
        }
        return lang;
    }//END getLanguage

    public static Locale getLanguageLocale(){
        return currentLocale;
    }

}