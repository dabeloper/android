package com.dabeloper.android;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 31/07/2017.
 */

import android.content.res.Configuration;
import android.app.Application;
import java.util.Locale;
import com.dabeloper.android.other.UserPreferences;

public class AppApplication extends Application {

    private static final String TAG = "AppApplication";
    private static AppApplication instance;


    public AppApplication() {
        super();
        instance = this;
    }

    public static AppApplication getApplication() {
        if (instance == null) {
            instance = new AppApplication();
        }
        return instance;
    }

//    public static void resetLanguage(){ instance = null;    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    /**
     * Set the Language Programmatically to control the APP UI language
     * */
    private void setLocale() {
        String languageToLoad = UserPreferences.getLanguage(getApplicationContext()); // your language
        if( languageToLoad==null ) return;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

}
