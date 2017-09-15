package com.dabeloper.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabeloper.android.R;
import com.dabeloper.android.adapter.PeopleRecyclerAdapter;
import com.dabeloper.android.model.Person;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment{

    Person person;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("PersonFragment onCreate");
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }

        person = getArguments().getParcelable("PERSON");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        View rootView = inflater.inflate(R.layout.fragment_person, container, false);
        System.out.println("PersonFragment onViewCreated");


        ((TextView) rootView.findViewById(R.id.tv_person_id)).setText(person.getId()+"");
        ((TextView) rootView.findViewById(R.id.tv_person_name)).setText(person.getName());
        ((TextView) rootView.findViewById(R.id.tv_person_curriculum)).setText(person.getCurriculum());
        ((TextView) rootView.findViewById(R.id.tv_person_profession)).setText(person.getProfession());

        ImageView imageView = (ImageView) rootView.findViewById(R.id.iv_person);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            imageView.setTransitionName(transitionName);
        }

        return rootView;
    }

}
