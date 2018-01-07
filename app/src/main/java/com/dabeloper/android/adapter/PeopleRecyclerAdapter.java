package com.dabeloper.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dabeloper.android.R;
import com.dabeloper.android.model.Person;
import com.dabeloper.android.other.OnLoadMoreListener;

import java.util.List;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 12/08/2017.
 */

public class PeopleRecyclerAdapter extends RecyclerView.Adapter {

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;

    private List<Person> PeopleList;
    private RecyclerView actualRecyclerView;
    private RecyclerView.OnScrollListener scrollLoading;

    private boolean isLoading;
    private OnLoadMoreListener callbackPopulate;


    private OnItemClickListener escucha;

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, Person person);
    }

    public PeopleRecyclerAdapter(List<Person> people, RecyclerView recyclerView, OnItemClickListener escucha) {
        this.PeopleList = people;
        this.escucha = escucha;
        this.actualRecyclerView = recyclerView;
        this.scrollLoading = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                try {
                    if( recyclerView.computeVerticalScrollRange() ==
                            ( recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent() ) ){

                        if ( !isLoading && callbackPopulate != null) {
                            isLoading = true;
                            callbackPopulate.onLoadMore();
                        }//end is loadMore call
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };//end OnScrollListener

    }

    @Override
    public int getItemViewType(int position) {
        return PeopleList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }//END getItemViewType

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_list_person, parent, false);
            vh = new PersonHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PersonHolder) {
            Person person= PeopleList.get(position);
            ((PersonHolder) holder).setPerson( person );

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return PeopleList.size();
    }


    /*************************  START STAGE :: CLASSES       *************************/


    //
    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            try {
                //change the color for the circular ProgressBar
                progressBar.getIndeterminateDrawable().setColorFilter(progressBar.getContext().getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }//end progress viewHolder



    //Needs class to inflate the cardView
    private class PersonHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener {

        private Person person;
        private Context mContext;

        private ImageView   pic;
        private TextView    tvName,
                            tvProfession;

        public PersonHolder(final View v) {
            super(v);
            mContext            = v.getContext();
            v.setOnClickListener(this);
            pic             = (ImageView) v.findViewById(R.id.iv_person);
            tvName          = (TextView) v.findViewById(R.id.tv_name);
            tvProfession    = (TextView) v.findViewById(R.id.tv_profession);
        }//END Constructor


        public void setPerson(Person person ){
            if( person==null ) return;
            this.person = person;

            pic.setImageResource(   R.drawable.ic_action_about  );
            //TODO add Settings page to enable show ANSWER in the card
            tvName.setText(          this.person.getName()     );
            tvProfession.setText(    this.person.getProfession()   );
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, person);
        }


    }//END viewHolder


}
