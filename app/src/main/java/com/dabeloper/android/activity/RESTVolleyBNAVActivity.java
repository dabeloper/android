package com.dabeloper.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dabeloper.android.R;
import com.dabeloper.android.fragment.VolleyFragment;
import com.dabeloper.android.fragment.VolleyFreeFragment;
import com.dabeloper.android.fragment.VolleyNewFragment;
import com.dabeloper.android.fragment.VolleyPaidFragment;

import java.util.ArrayList;
import java.util.List;

public class RESTVolleyBNAVActivity extends AppCompatActivity {

    private static final int BNAV_POS_FREE   = 0;
    private static final int BNAV_POS_PAID   = 1;
    private static final int BNAV_POS_NEW    = 2;

    private static RESTVolleyBNAVActivity mInstance;
    private BottomNavigationView bottomNavigationView;
    private List<Fragment> fragments = new ArrayList<>(3);

    public static RESTVolleyBNAVActivity getInstance(){
        if( mInstance == null ){
            mInstance = new RESTVolleyBNAVActivity();
        }
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restvolley_bnav);
        mInstance = this;

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("SetUp - fragments List");


        fragments.add( BNAV_POS_FREE , VolleyFreeFragment.getInstance() );
        fragments.add( BNAV_POS_PAID , VolleyPaidFragment.getInstance() );
        fragments.add( BNAV_POS_NEW, VolleyNewFragment.getInstance());

//        https://stackoverflow.com/questions/41432902/bottomnavigationview-is-not-full-width/45301351#45301351
//        Para obtener una BottomToolbar de tamaño completo "full_width" es necesario editar "dimens.xml" y agregar los valores
//                <dimen name="design_bottom_navigation_item_max_width" tools:override="true">600dp</dimen>
//                <dimen name="design_bottom_navigation_active_item_max_width" tools:override="true">600dp</dimen>
//
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        //Start with the default Fragment
        switchFragment(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottombaritem_free:
                                switchFragment(0);
                                return true;
                            case R.id.bottombaritem_paid:
                                switchFragment(1);
                                return true;
                            case R.id.bottombaritem_new:
                                switchFragment(2);
                                return true;
                        }
                        return false;
                    }
                });


//        Snackbar snack = Snackbar.make(bottomNavigationView,
//                "Your message", Snackbar.LENGTH_LONG);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
//                snack.getView().getLayoutParams();
//        params.setMargins(0, 0, 0, bottomNavigationView.getHeight());
//        snack.getView().setLayoutParams(params);
//        snack.show();

    }//END OnCreate


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case android.R.id.home:
                startActivity(new Intent(RESTVolleyBNAVActivity.this, MainActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Function used by the "onNavigationItemSelected" event from setOnNavigationItemSelectedListener
     * */
    private void switchFragment( int pos ) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_holder, fragments.get(pos), fragments.get(pos).getClass().getSimpleName())
                .commit();


    }//END switchFragment

    public static Context getActualContext(){
        return mInstance.getApplicationContext();
    }

    public int getBottomNavigationViewHeight(){
        return bottomNavigationView.getHeight();
    }

}
