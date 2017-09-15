package com.dabeloper.android.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dabeloper.android.R;
import com.dabeloper.android.adapter.MenuActivityDataAdapter;
import com.dabeloper.android.model.DummyData;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private Toolbar mTopToolbar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTopToolbar.setLogo(R.drawable.ic_action_cancel);
        ImageView tvBack = ((ImageView)mTopToolbar.findViewById(R.id.toolbar_back));
        tvBack.setImageResource( getResIdFromAttribute(this,R.attr.homeAsUpIndicator) );
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MenuActivity.this, MainActivity.class );
                startActivity(intent);
                finish();
            }
        });
        mTopToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Toolbar setOnClickListener", Toast.LENGTH_SHORT).show();
            }
        });
        //mTopToolbar.inflateMenu();
        mTopToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println( "TopToolbar onMenuItemClick "+ item.getItemId() );
                return false;
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //App bar
//        try {
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle(R.string.activity_with_menu);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayUseLogoEnabled(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //RecyclerView
//        RecyclerView lstLista = (RecyclerView)findViewById(R.id.lstLista);
//
//        ArrayList<DummyData> datos = new ArrayList<>();
//        for(int i=0; i<50; i++)
//            datos.add(new DummyData("Título " + i, "Subtítulo item " + i, i+""));
//
//        MenuActivityDataAdapter adaptador = new MenuActivityDataAdapter(datos);
//        lstLista.setAdapter(adaptador);
//
//        lstLista.setLayoutManager(
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        lstLista.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        lstLista.setItemAnimator(new DefaultItemAnimator());
    }//EDN OnCreate



    public static int getResIdFromAttribute(final MenuActivity activity,final int attr)
    {
        if(attr==0)
            return 0;
        final TypedValue typedvalueattr=new TypedValue();
        activity.getTheme().resolveAttribute(attr,typedvalueattr,true);
        return typedvalueattr.resourceId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if( mActionBarDrawerToggle.onOptionsItemSelected( item ) ){
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//END onOptionsItemSelected


}
