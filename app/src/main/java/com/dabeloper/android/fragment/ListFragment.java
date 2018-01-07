package com.dabeloper.android.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabeloper.android.R;
import com.dabeloper.android.adapter.RecyclerDataListAdapter;
import com.dabeloper.android.model.DummyData;
import com.dabeloper.android.other.OnLoadMoreListener;
import com.dabeloper.android.sqlite_model.DbHandler;
import com.dabeloper.android.sqlite_model.DummyContract;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends TemplateFragment {

    /*************************  START STAGE :: INITIALIZATION              *************************/


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_FROM_NUMBER  = "from";
    public static final String ARG_PRIVIAS_LIST = "list";
    public static final String ARG_LAYOUT_MANAGER = "layout_type";

    // TODO: Rename and change types of parameters
    private OnLoadMoreListener callbackPopulate;
    protected ArrayList<DummyData> DataList; //List of Privias
    private RecyclerView mRecyclerView;
    private RecyclerDataListAdapter mAdapter;
    private DbHandler DbInstance;
    private ProgressDialog pDialog;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;
    private static final int GRID_COLS  = 2;
    private boolean isBuildingPrivias   = false;
    private boolean IS_SEARCHING        = false;
    private int FROM;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DataList    = getArguments().getParcelableArrayList(ARG_PRIVIAS_LIST);
            FROM        = getArguments().getInt(ARG_FROM_NUMBER);
        }

        //Device rotating
        if (savedInstanceState != null) {
            DataList    = savedInstanceState.getParcelableArrayList(ARG_PRIVIAS_LIST);
            FROM        = savedInstanceState.getInt(ARG_FROM_NUMBER);
            mCurrentLayoutManagerType   = (LayoutManagerType) savedInstanceState.getSerializable(ARG_LAYOUT_MANAGER);
        }

        if( DataList==null ){
            DataList = new ArrayList<DummyData>();
        }


        callbackPopulate = new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                try {
                    if( isBuildingPrivias || IS_SEARCHING ) return;

                    if( DataList.indexOf(null) == -1 ){
                        try {
                            DataList.add(null);
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    isBuildingPrivias = true;


                    //Make a thread to load the next items
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                removeLoadIcon();
                                FROM = DataList.size();

                                //Getting from SQLite
                                SQLiteDatabase db = DbInstance.getReadableDatabase();


                                Cursor cursor = db.rawQuery("select * from " + DummyContract.FeedEntry.TABLE_NAME + " WHERE ACTIVE = \"1\" ORDER BY "+ DummyContract.FeedEntry.COLUMN_NAME_CREATED_DT+" DESC LIMIT 10 OFFSET " + FROM , null);

                                if( cursor!=null && cursor.getCount()>0 ){

                                    cursor.moveToFirst();
                                    int privia_id               = cursor.getColumnIndexOrThrow(DummyContract.FeedEntry._ID);
                                    int privia_title            = cursor.getColumnIndexOrThrow(DummyContract.FeedEntry.COLUMN_NAME_TITLE);
                                    int privia_body             = cursor.getColumnIndexOrThrow(DummyContract.FeedEntry.COLUMN_NAME_BODY);
                                    int privia_type             = cursor.getColumnIndexOrThrow(DummyContract.FeedEntry.COLUMN_NAME_TYPE);
                                    int privia_created_dt       = cursor.getColumnIndexOrThrow(DummyContract.FeedEntry.COLUMN_NAME_CREATED_DT);
                                    int privia_last_access_dt   = cursor.getColumnIndexOrThrow(DummyContract.FeedEntry.COLUMN_NAME_LAST_ACCESS_DT);

                                    if (cursor.moveToFirst()) {
                                        while (!cursor.isAfterLast()) {
                                            DataList.add(
                                                    new DummyData(
                                                            cursor.getString(privia_title),
                                                            cursor.getString(privia_body),
                                                            cursor.getString(privia_id),
                                                            cursor.getString(privia_type)
                                                    )
                                            );
                                            mAdapter.notifyDataSetChanged();
                                            cursor.moveToNext();
                                        }
                                    }//END Cursor getItem Condition

                                    FROM = DataList.size();
                                }//END check cursor size


                                //Block Database, Session Close
                                DbInstance.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            isBuildingPrivias = false;
                            mAdapter.resetLoaded();

                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                    removeLoadIcon();
                    isBuildingPrivias = false;
                }//END try/catch SQL

            }
        };//END callBackPopulate Setup

        DbInstance = new DbHandler(getContext());

    }//END onCreate



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView       = (RecyclerView) rootView.findViewById(R.id.rv_items);

        if( mRecyclerView.getTag().equals("0") ){
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            mRecyclerView.setLayoutManager(mLayoutManager);
        }else{
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), GRID_COLS);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
            mRecyclerView.setLayoutManager(mLayoutManager);
        }

        mAdapter        = new RecyclerDataListAdapter( DataList, mRecyclerView);
        mAdapter.addScrollLoading();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setCallbackPopulate( callbackPopulate );

        if( DataList.isEmpty() ){
            DataList.add(null);
            mAdapter.notifyDataSetChanged();
        }

        //Use the ActionBar with the onCreateOptionsMenu
        setHasOptionsMenu(true);

        rootView.post(new Runnable() {
            @Override
            public void run() {
//                new CheckAvailablePrivias().execute();
            }
        });

        // Inflate the layout for this fragment
        return rootView;

    }//END onCreateView



    /**
     * Load Icon is a NULL Object into the List
     * */
    public void removeLoadIcon(){

        if( mAdapter != null ){
            int progressIdx;
            while( (progressIdx = DataList.indexOf(null)) > -1){
                DataList.remove( progressIdx );
                mAdapter.notifyItemRemoved( progressIdx );
            }
        }

    }//END removeLoadIcon function



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(ARG_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        savedInstanceState.putParcelableArrayList(ARG_PRIVIAS_LIST, DataList );
        savedInstanceState.putInt(ARG_FROM_NUMBER , FROM );
        super.onSaveInstanceState(savedInstanceState);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
