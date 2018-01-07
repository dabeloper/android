package com.dabeloper.android.fragment;

import android.os.Bundle;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 09/09/2017.
 *
 * Singleton with same logic Volley using VolleyFragment
 */

public class VolleyPaidFragment {

    private static VolleyFragment mInstance;

    public static VolleyFragment getInstance(){
        if( mInstance == null ){
            mInstance = new VolleyFragment();
            Bundle argsFree = new Bundle();
            argsFree.putInt(VolleyFragment.ARG_TYPE, VolleyFragment.TYPE_PAID);
            mInstance.setArguments(argsFree);
        }
        return mInstance;
    }


}
