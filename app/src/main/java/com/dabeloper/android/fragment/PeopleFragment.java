package com.dabeloper.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabeloper.android.R;
import com.dabeloper.android.activity.PeopleActivity;
import com.dabeloper.android.adapter.PeopleRecyclerAdapter;
import com.dabeloper.android.model.Person;
import com.dabeloper.android.sqlite_model.DbHandler;
import com.dabeloper.android.sqlite_model.PersonContract;
import com.dabeloper.android.sqlite_model.PersonSQLite;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PeopleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected ArrayList<Person> PersonList; //List of Privias
    private RecyclerView mRecyclerView;
    private PeopleRecyclerAdapter mAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PeopleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PeopleFragment newInstance() {
        PeopleFragment fragment = new PeopleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        PersonList      = new ArrayList<>();

        System.out.println( "PEOPLE/ rootView post " );

        DbHandler db = new DbHandler(getContext());
        Cursor cursor = PersonSQLite.getAllPersons(db.getReadableDatabase());

        if( cursor!=null && cursor.getCount()>0 ) {
            int i=0;
            System.out.println( "PEOPLE/ Filling with cursor " );
            cursor.moveToFirst();
            int privia_id = cursor.getColumnIndexOrThrow(PersonContract.FeedEntry._ID);
            int column_name = cursor.getColumnIndexOrThrow(PersonContract.FeedEntry.COLUMN_NAME_NAME);
            int column_profession = cursor.getColumnIndexOrThrow(PersonContract.FeedEntry.COLUMN_NAME_PROFESSION);
            int column_curriculum = cursor.getColumnIndexOrThrow(PersonContract.FeedEntry.COLUMN_NAME_CURRICULUM);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    PersonList.add(
                            new Person(
                                    cursor.getLong(privia_id),
                                    cursor.getString(column_name),
                                    cursor.getString(column_profession),
                                    cursor.getString(column_curriculum)
                            )
                    );
                    System.out.println( "PEOPLE/ "+i++ );
                    cursor.moveToNext();
                }
            }//END Cursor getItem Condition
        }//END cursor condition

        System.out.println( "PEOPLE/ PersonList size: " + PersonList.size() );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people, container, false);
        mRecyclerView       = (RecyclerView) rootView.findViewById(R.id.people_list);

        mAdapter        = new PeopleRecyclerAdapter( PersonList, mRecyclerView, (PeopleActivity)getActivity() );
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if( !PersonList.isEmpty() ){
            mAdapter.notifyDataSetChanged();
        }
        System.out.println( "PEOPLE/ mAdapter getItemCount: " + mAdapter.getItemCount() );

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
