package com.dabeloper.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dabeloper.android.R;
import com.dabeloper.android.adapter.PeopleRecyclerAdapter;
import com.dabeloper.android.fragment.PeopleFragment;
import com.dabeloper.android.fragment.PersonFragment;
import com.dabeloper.android.model.Person;

import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity implements PeopleFragment.OnFragmentInteractionListener,
                                                                    PeopleRecyclerAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.people_container);

        //Avoid Exception
        //This Activity can attach 2 Fragments: People & Person
        if( fragment == null || fragment instanceof PeopleFragment ){
            fragment = PeopleFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.people_container, fragment)
                    .commit();
        }//END PeopleFragment

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent_main = new Intent(PeopleActivity.this, MainActivity.class);
                startActivity(intent_main);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, Person person) {
//        Snackbar.make(findViewById(android.R.id.content), ":id = " + person.getId(),
//                Snackbar.LENGTH_LONG).show();


        PersonFragment fragment = new PersonFragment();
        Bundle b = (new Bundle());
        b.putParcelable("PERSON",person);
        fragment.setArguments( b );
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.people_container, fragment)
//                .commit();
       //It is mandatory to undo transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.people_container, fragment,fragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }
}
