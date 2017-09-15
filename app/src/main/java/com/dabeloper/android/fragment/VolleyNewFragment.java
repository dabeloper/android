package com.dabeloper.android.fragment;

import android.os.Bundle;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 09/09/2017.
 *
 * Singleton with same logic Volley using VolleyFragment
 *
 * Para hacer uso del RecyclerView persistente a cambios,
 * es necesario que el Fragment que lo contenga se una instancia a ejecutarse 1 sola ves sin importar
 * la rotacion, el ciclo de vida o el tiempo de espera para ello se hace uso de un intermediario
 * que obtiene un objeto tipo Fragment el cual sera unico en este ambito
 */

public class VolleyNewFragment {

    private static VolleyFragment mInstance;

    public static VolleyFragment getInstance(){
        if( mInstance == null ){
            mInstance = new VolleyFragment();
            Bundle argsFree = new Bundle();
            argsFree.putInt(VolleyFragment.ARG_TYPE, VolleyFragment.TYPE_NEW);
            mInstance.setArguments(argsFree);
        }
        return mInstance;
    }


}
