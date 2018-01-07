package com.dabeloper.android.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dabeloper.android.R;
import com.dabeloper.android.activity.RESTVolleyBNAVActivity;
import com.dabeloper.android.adapter.RESTVolleyBNAVRecyclerAdapter;
import com.dabeloper.android.model.IOSApp;
import com.dabeloper.android.network.VolleyAppController;
import com.dabeloper.android.other.OnLoadMoreListener;
import com.dabeloper.android.other.animation.SwipeableTouchHelperCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolleyFragment extends Fragment {

    public String TAG;

    public static final String ARG_FROM = "FROM";
    public static final String ARG_URL = "URL";
    public static final String ARG_TYPE = "TYPE";
    public static final String ARG_DATA_LIST = "DATA_LIST";
    public static final String ARG_LAYOUT_MANAGER = "LAYOUT_MANAGER";

    public static final int TYPE_FREE = 1;
    public static final int TYPE_PAID = 2;
    public static final int TYPE_NEW = 3;

    private static final int MAX_RESULTS = 10;
    private static final String URL_FREE = "https://itunes.apple.com/us/rss/topfreeapplications/limit=%1$d/json";
    private static final String URL_PAID = "https://itunes.apple.com/us/rss/toppaidapplications/limit=%1$d/json";
    private static final String URL_NEW = "https://itunes.apple.com/us/rss/newapplications/limit=%1$d/json";
    //https://affiliate.itunes.apple.com/resources/blog/introduction---rss-feed-generator/
    //https://affiliate.itunes.apple.com/resources/documentation/itunes-enterprise-partner-feed/
    //https://itunes.apple.com/us/rss/customerreviews/id=749133301/sortBy=mostRecent/json
    //https://itunes.apple.com/us/rss/newapplications/limit=10/genre=6014/json

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    private int mFrom = 0;
    private String mUrl;
    private ArrayList<IOSApp> IOSAppList;
    private boolean isBuilding;

    private RecyclerView mRecyclerView;
    private OnLoadMoreListener callbackPopulate;
    private RESTVolleyBNAVRecyclerAdapter mAdapter;

    public VolleyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        //default
        TAG = this.getClass().getSimpleName() + "#FREE";
        mUrl = URL_FREE;

        if (getArguments() != null) {
            switch (getArguments().getInt(ARG_TYPE)) {
                case TYPE_FREE:
                    TAG = this.getClass().getSimpleName() + "#FREE";
                    mUrl = URL_FREE;
                    break;
                case TYPE_PAID:
                    TAG = this.getClass().getSimpleName() + "#PAID";
                    mUrl = URL_PAID;
                    break;
                case TYPE_NEW:
                    TAG = this.getClass().getSimpleName() + "#NEW";
                    mUrl = URL_NEW;
                    break;
            }
        }


        //Device rotating
        if (savedInstanceState != null) {
            System.out.println(TAG + " onCreate savedInstanceState found , Filling... ");
                mFrom = savedInstanceState.getInt(ARG_FROM);
                mUrl = savedInstanceState.getString(ARG_URL);
                IOSAppList = savedInstanceState.getParcelableArrayList(ARG_DATA_LIST);
                mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(ARG_LAYOUT_MANAGER);
            System.out.println(TAG + " savedInstanceState found, mFrom : "+mFrom+", mUrl: "+mUrl+", IOSAppList: "+IOSAppList.size() );
        }

        if (IOSAppList == null) {
            IOSAppList = new ArrayList<IOSApp>();
        }

        callbackPopulate = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                System.out.println(TAG + " callbackPopulate, filling... ");
                if (isBuilding) return;

                //Finding a null object means a ProgressItem Component
                if (IOSAppList.indexOf(null) == -1) {
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
                                String.format(Locale.ENGLISH, mUrl, (mFrom + MAX_RESULTS)),
                                /*"https://itunes.apple.com/us/rss/topfreeapplications/limit="+(IOSAppList.size()+MAX_RESULTS)+"/json",*/
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        if (response.has("feed")) {
                                            try {
                                                JSONArray entries = response.getJSONObject("feed").getJSONArray("entry");
                                                int i = mFrom;
                                                while (i < entries.length() &&
                                                        entries.getJSONObject(i) != null) {
                                                    IOSAppList.add(new IOSApp(entries.getJSONObject(i)));
                                                    i++;
                                                }
                                                mFrom = entries.length();
                                                Toast.makeText(RESTVolleyBNAVActivity.getActualContext(), TAG + " Volley Request Get " + i + "/" + IOSAppList.size() + " new Apps!", Toast.LENGTH_SHORT).show();
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

                                        System.out.println(TAG + error.toString());
                                        Toast.makeText(RESTVolleyBNAVActivity.getActualContext(), TAG + " Volley Request Fails!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        // Adding the request to the queue along with a unique string tag
                        VolleyAppController.getInstance(RESTVolleyBNAVActivity.getActualContext()).addToRequestQueue(jsonVolleyReq, "getRequest");

                    }
                }, 5000);

            }
        };


        super.onCreate(savedInstanceState);
    }//END OnCreate


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_volley, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_volley);

        if (savedInstanceState != null) {
            System.out.println(TAG + " onCreateView savedInstanceState found, ... ");
        }

        System.out.println(TAG + " savedInstanceState? found, mFrom : "+mFrom+", mUrl: "+mUrl+", IOSAppList: "+IOSAppList.size() );


        System.out.println(TAG + "  SetUp - mRecyclerView ");
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        System.out.println(TAG + "  SetUp - mAdapter ");
            mAdapter = new RESTVolleyBNAVRecyclerAdapter(IOSAppList, mRecyclerView);
            mAdapter.addScrollLoading();
        System.out.println(TAG + "  SetUp - mAdapter with mRecyclerView ");
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setCallback(callbackPopulate);
        System.out.println(TAG + "  SetUp - SwipeableTouchHelperCallback with mAdapter for mRecyclerView ");
            //https://stackoverflow.com/questions/27293960/swipe-to-dismiss-for-recyclerview/30601554#30601554
            ItemTouchHelper.Callback callback = new SwipeableTouchHelperCallback(mAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(mRecyclerView);
            if (IOSAppList.isEmpty()){
                IOSAppList.add(null);
                mAdapter.notifyDataSetChanged();
                System.out.println(TAG + "  First call to load data ");
                callbackPopulate.onLoadMore();
            }

        // Inflate the layout for this fragment
        return rootView;
    }//END onCreateView


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_FROM, mFrom );
        outState.putString(ARG_URL, mUrl );
        outState.putParcelableArrayList(ARG_DATA_LIST, IOSAppList );
        super.onSaveInstanceState(outState);
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

}
