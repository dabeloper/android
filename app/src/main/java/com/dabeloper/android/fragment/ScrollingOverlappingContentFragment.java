package com.dabeloper.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabeloper.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollingOverlappingContentFragment extends Fragment {

    public static final String TAG = "Overlapping Content";

    public ScrollingOverlappingContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scrolling_overlapping_content, container, false);
    }

}
