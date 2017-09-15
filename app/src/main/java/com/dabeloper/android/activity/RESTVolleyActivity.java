package com.dabeloper.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dabeloper.android.R;
import com.dabeloper.android.adapter.RESTVolleyRecyclerAdapter;
import com.dabeloper.android.model.IOSApp;
import com.dabeloper.android.network.VolleyAppController;
import com.dabeloper.android.other.OnLoadMoreListener;
import com.dabeloper.android.other.animation.SwipeableTouchHelperCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class RESTVolleyActivity extends AppCompatActivity {


    private static Context mContext;

    private static final String ARG_DATA_LIST = "IOSAppList",
                                ARG_IS_SEARCHING = "isSearching",
                                ARG_SEARCH_QUERY = "searchQuery",
                                KEY_LAYOUT_MANAGER = "mCurrentLayoutManagerType";

    private static final String URL = "https://itunes.apple.com/us/rss/topfreeapplications/limit=%1$d/json";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    private static final int    GRID_COLS   = 2;
    private static final int    MAX_RESULTS = 10;


    //Number to know how many elements has been downloaded from server
    private int from = 0;

    private String searchQuery  = "";

    private boolean isFABOpen              = false,
                    isBuilding = false,
                    isSearching            = false,
                    isDownloadAvailable    = false;

    private OnLoadMoreListener callbackPopulate;

    protected ArrayList<IOSApp> IOSAppList;
    private RecyclerView mRecyclerView;
    private RESTVolleyRecyclerAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_restvolley);

        mRecyclerView   = (RecyclerView) findViewById(R.id.rv_volley);
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
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(view.getContext(), "Fab toggle", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        //Device rotating
        if (savedInstanceState != null) {
            IOSAppList                  = savedInstanceState.getParcelableArrayList(ARG_DATA_LIST);
            isSearching                 = savedInstanceState.getBoolean(ARG_IS_SEARCHING);
            searchQuery                 = savedInstanceState.getString(ARG_SEARCH_QUERY);
            mCurrentLayoutManagerType   = (LayoutManagerType) savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER);
        }

        if( IOSAppList==null ){
            IOSAppList = new ArrayList<IOSApp>();
        }

        callbackPopulate = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                System.out.println( " callbackPopulate, filling... ");
                if( isBuilding || isSearching ) return;

                //Finding a null object means a ProgressItem Component
                if( IOSAppList.indexOf(null) == -1 ){
                    IOSAppList.add(null);
                    mAdapter.notifyDataSetChanged();
                }

                isBuilding = true;


                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        /**
                         JsonObjectRequest takes in five parameters
                         Request Type - This specifies the type of the request eg: GET,POST
                         URL          - This String param specifies the Request URL
                         JSONObject   - This parameter takes in the POST parameters.null in case of
                         GET request (Request.Method.GET)
                         Listener     -This parameter takes in a implementation of  Response.Listener()
                         interface which is invoked if the request is successful
                         Listener     -This parameter takes in a implementation of Error.Listener()
                         interface which is invoked if any error is encountered while processing
                         the request
                         **/
                        //IMPORTANT! When the URL receive a dynamic value like a LIMIT or FROM number,
                        // is need create the JsonObjectRequest foreach Request
                        JsonObjectRequest jsonVolleyReq = new JsonObjectRequest(Request.Method.GET,
                                String.format( Locale.ENGLISH, URL, (from+MAX_RESULTS) ),
                                /*"https://itunes.apple.com/us/rss/topfreeapplications/limit="+(IOSAppList.size()+MAX_RESULTS)+"/json",*/
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        if(  response.has("feed") ){
                                            try {
                                                JSONArray entries = response.getJSONObject("feed").getJSONArray("entry");
                                                int i = from;
                                                while ( i < entries.length() &&
                                                        entries.getJSONObject(i) != null){
                                                    IOSAppList.add( new IOSApp(entries.getJSONObject(i)) );
                                                    i++;
                                                }
                                                from = entries.length();
                                                Toast.makeText(getActualContext() , "Volley Request Get "+i+"/"+IOSAppList.size()+" new Apps!", Toast.LENGTH_SHORT).show();
                                                mAdapter.notifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }//END feed condition

                                        //Success Request
                                        isBuilding = false;
                                        mAdapter.resetLoaded();
                                        removeProgressIcon();

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Failure Request
                                        removeProgressIcon();
                                        isBuilding = false;
                                        mAdapter.resetLoaded();

                                        System.out.println(error.toString());
                                        Toast.makeText(getActualContext() , "Volley Request Fails!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        // Adding the request to the queue along with a unique string tag
                        VolleyAppController.getInstance(getBaseContext()).addToRequestQueue(jsonVolleyReq,"getRequest");

                    }
                } , 5000);

            }
        };

        System.out.println( " SetUp - mRecyclerView ");
            mRecyclerView.setLayoutManager( new LinearLayoutManager(getActualContext()) );
        System.out.println( " SetUp - mAdapter ");
            mAdapter = new RESTVolleyRecyclerAdapter( IOSAppList , mRecyclerView );
            mAdapter.addScrollLoading();
        System.out.println( " SetUp - mAdapter with mRecyclerView ");
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(callbackPopulate);
            IOSAppList.add(null);
            mAdapter.notifyDataSetChanged();
        System.out.println( " SetUp - SwipeableTouchHelperCallback with mAdapter for mRecyclerView ");
        //https://stackoverflow.com/questions/27293960/swipe-to-dismiss-for-recyclerview/30601554#30601554
            ItemTouchHelper.Callback callback = new SwipeableTouchHelperCallback(mAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
        System.out.println( " First call to load data ");
            callbackPopulate.onLoadMore();


    }//END OnCreated


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(RESTVolleyActivity.this, MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Load Icon is a NULL Object into the List
     * */
    public void removeProgressIcon(){

        if( mAdapter != null ){
            int progressIdx;
            while( (progressIdx = IOSAppList.indexOf(null)) > -1){
                IOSAppList.remove( progressIdx );
                mAdapter.notifyItemRemoved( progressIdx );
            }
        }

    }//END removeLoadIcon function


    public static Context getActualContext(){
        return mContext;
    }


}
