package com.dabeloper.android.activity;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dabeloper.android.fragment.DetailFragment;
import com.dabeloper.android.fragment.DummyFragment;
import com.dabeloper.android.fragment.ListFragment;
import com.dabeloper.android.fragment.ScrollingFlexibleSpaceFragment;
import com.dabeloper.android.fragment.ScrollingHideTopToolbarFragment;
import com.dabeloper.android.fragment.ScrollingImageCollapseFragment;
import com.dabeloper.android.fragment.ScrollingOverlappingContentFragment;
import com.dabeloper.android.fragment.SettingFragment;
import com.dabeloper.android.fragment.TemplateFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.dabeloper.android.BuildConfig;
import com.dabeloper.android.R;

public class TabActivity   extends AppCompatActivity
        implements  TemplateFragment.OnFragmentInteractionListener,
                    DummyFragment.OnFragmentInteractionListener {

    /*************************  START STAGE :: INITIALIZATION              *************************/

    private static TabActivity instance;

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SearchView mSearchViewBar;
    private static final String TAG = "TabActivity";

    //Page Position
    private static final int TAB_LIST       = 0;
    private static final int TAB_DETAIL     = 1;
    private static final int TAB_SETTING    = 2;


    /* Singleton class, valid uses in classes of "other" or "model" package */
    public static TabActivity getInstance(){
        if( instance==null ){
            instance = new TabActivity();
        }
        return instance;
    }

//    public static void resetLanguage(){ instance = null;    }

    /*************************  START STAGE :: MAIN METHODS AND EVENTS                  *************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab);
        instance = this;

        try {
            mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager() );
        adapter.addFragment( new ScrollingOverlappingContentFragment(),   ScrollingOverlappingContentFragment.TAG );
        adapter.addFragment( new ScrollingHideTopToolbarFragment(),     ScrollingHideTopToolbarFragment.TAG );
        adapter.addFragment( new ScrollingFlexibleSpaceFragment(),   ScrollingFlexibleSpaceFragment.TAG );
        adapter.addFragment( new ScrollingImageCollapseFragment(),   ScrollingImageCollapseFragment.TAG );
        adapter.addFragment( new SettingFragment(),  getString( R.string.setting ) );
        viewPager.setAdapter(adapter);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem( tab.getPosition() );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }//END OnCreated Method

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println( "OnFragmentInteractionListener > onFragmentInteraction : "+ uri.toString() );
    }//END onFragmentInteraction

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_menu, menu);

        //SetUp the search bar
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchViewBar = (SearchView) searchItem.getActionView();

        mSearchViewBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //some operation
                //System.out.println("mSearchViewBar onClose");
                return false;
            }
        });
        mSearchViewBar.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //some operation
                //System.out.println("mSearchViewBar onClick");
            }
        });

        EditText searchPlate = (EditText) mSearchViewBar.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchPlate.setHint( getResources().getString(R.string.key_word) );
        View searchPlateView = mSearchViewBar.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlateView.setBackgroundColor(getResources().getColor(R.color.transparent));

        mSearchViewBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // use this method when query submitted
                 Toast.makeText(getApplication(), query, Toast.LENGTH_SHORT).show();
//                MyPriviasFragment.getInstance().search( query );
//                showPriviasTab();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // use this method for auto complete search process
                //exampleListAdapter.getFilter().filter(newText);
                mSearchViewBar.setIconified( false );
                return false;
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchViewBar.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        mSearchViewBar.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );
        mSearchViewBar.setSubmitButtonEnabled(true);
        //mSearchViewBar.setOnQueryTextListener(this);

        return true;
    }//END onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBack();
                return true;

            case R.id.action_run:
                return true;
            case R.id.action_help:
//                AlertDialog.Builder alert = new AlertDialog.Builder(this);
//                alert.setTitle( "Privia v" + BuildConfig.VERSION_NAME );
//                alert.setMessage( R.string.dabeloper );
//                alert.setPositiveButton(R.string.repeat_introduction, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        (new SessionManager(getApplicationContext())).resetViewed();
//                        startActivity(new Intent(MainActivity.this, SplashActivity.class));
//                        finish();
//                    }
//                });
//                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alert.show();
                return true;
            case R.id.action_language_english:
//                if( !UserPreferences.getLanguage(getApplicationContext()).equals("en") ) {
//                    AlertDialog.Builder lang_en_alert = new AlertDialog.Builder(this);
//                    lang_en_alert.setTitle( R.string.languages );
//                    lang_en_alert.setMessage( R.string.languages_hint );
//                    lang_en_alert.setPositiveButton(R.string.continues, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            UserPreferences.saveLanguage(getApplicationContext(), "en");
//                            startActivity(new Intent(MainActivity.this, SplashActivity.class));
//                            finish();
//                        }
//                    });
//                    lang_en_alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    lang_en_alert.show();
//                }
                return true;
            case R.id.action_language_portuguese:
//                if( !UserPreferences.getLanguage(getApplicationContext()).equals("pt") ) {
//                    AlertDialog.Builder lang_pt_alert = new AlertDialog.Builder(this);
//                    lang_pt_alert.setTitle( R.string.languages );
//                    lang_pt_alert.setMessage( R.string.languages_hint );
//                    lang_pt_alert.setPositiveButton(R.string.continues, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            UserPreferences.saveLanguage(getApplicationContext(), "pt");
//                            startActivity(new Intent(MainActivity.this, SplashActivity.class));
//                            finish();
//                        }
//                    });
//                    lang_pt_alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    lang_pt_alert.show();
//                }
                return true;
            case R.id.action_language_spanish:
//                if( !UserPreferences.getLanguage(getApplicationContext()).equals("es") ) {
//                    AlertDialog.Builder lang_es_alert = new AlertDialog.Builder(this);
//                    lang_es_alert.setTitle( R.string.languages );
//                    lang_es_alert.setMessage(R.string.languages_hint);
//                    lang_es_alert.setPositiveButton(R.string.continues, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            UserPreferences.saveLanguage(getApplicationContext(), "es");
//                            finish();
////                            startActivity(new Intent(MainActivity.this, SplashActivity.class));
//                            Intent i = new Intent(MainActivity.this, SplashActivity.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(i);
////                            Intent i = getBaseContext().getPackageManager()
////                                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
////                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(i);
//                        }
//                    });
//                    lang_es_alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    lang_es_alert.show();
//                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }//END onOptionsItemSelected

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    /*************************  END STAGE :: MAIN METHODS AND EVENTS                  *************************/





    /*************************  START STAGE :: METHODS AND FUNCTIONS       *************************/


    /*** Enable the screen rotation */
    public void enableRotate(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }//end enableRotate method


    /**
     * Return Back to MainActivity "PriviaFragment"
     * */
    private void onBack(){

        enableRotate();
        Intent intent_main = new Intent(TabActivity.this, MainActivity.class);
        startActivity(intent_main);
        finish();

    }//END onBack

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //https://stackoverflow.com/questions/13910826/viewpager-fragmentstatepageradapter-orientation-change#14578709
        //For a ViewPagerAdapter type FragmentStatePagerAdapter use this way to invalidate UI changes when trotate device
        //super.onSaveInstanceState(outState);
    }


    /*************************  END STAGE :: METHODS AND FUNCTIONS       *************************/



    /*************************  START STAGE :: CLASSES       *************************/

    /** Adapter to setup the tabs
     *
     *
     * FragmentPagerAdapter
     *      This is best when navigating between sibling screens representing a fixed,
     *      small number of pages.
     *
     * FragmentStatePagerAdapter
     *
     *     This is best for paging across a collection of objects for which the number of pages
     *     is undetermined. It destroys fragments as the user navigates to other pages,
     *     minimizing memory usage.
     *
     * http://stackoverflow.com/questions/29503818/sliding-tabs-doesnt-load-correctly-on-second-time
     *
     * */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentList.get(position);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
            if( fragment.getClass().getSimpleName().equals(ScrollingHideTopToolbarFragment.class.getSimpleName()) ){
                //app:layout_scrollFlags="scroll|enterAlways|snap"
                //Set-Up the Main TopBar to Hide when Scroll to bottom and appear only when scroll up ALL the content
                params.setScrollFlags( AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED);

                //Set-up Main TopBar to Hide when Scroll to botton and Appear when scroll up
//                params.setScrollFlags(
//                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL /* will be scrolled along with the content.  */
//                        |   AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS  /*when content is pulled down, immediately app bar will appear. */
//                        |   AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP /* when the AppBar is half scrolled and content scrolling stopped, this will allow the AppBar to settle either hidden or appear based on the scrolled size of Toolbar. */
//                );
            }else{
                //Show the Main TopBar in each change of tab(Fragment)
                mAppBarLayout.setExpanded(true, true);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }//END ViewPagerAdapter Class

    /*************************  END STAGE :: CLASSES       *************************/

}