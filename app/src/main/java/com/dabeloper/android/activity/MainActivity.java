package com.dabeloper.android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dabeloper.android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMenuActivity = (Button)findViewById(R.id.btn_activity_menu);
        Button btnTabsActivity = (Button)findViewById(R.id.btn_activity_tabs);
        Button btnPeopleActivity = (Button)findViewById(R.id.btn_activity_people);
        Button btnVolleyActivity = (Button)findViewById(R.id.btn_activity_volley);
        Button btnVolleyBNAVActivity = (Button)findViewById(R.id.btn_activity_volley_bottom_nav);

        btnTabsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TabActivity.class));
                finish();
            }
        });

        btnMenuActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
                finish();
            }
        });


        btnPeopleActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PeopleActivity.class));
                finish();
            }
        });

        btnVolleyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RESTVolleyActivity.class));
                finish();
            }
        });

        btnVolleyBNAVActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RESTVolleyBNAVActivity.class));
                finish();
            }
        });

    }
}
