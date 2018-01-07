package com.dabeloper.android.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dabeloper.android.R;
import com.dabeloper.android.model.DummyData;
import com.dabeloper.android.other.OnLoadMoreListener;

import java.util.Date;
import java.util.List;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 31/07/2017.
 */

public class RecyclerDataListAdapter extends RecyclerView.Adapter {

    private int position;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<DummyData> DataList;
    private RecyclerView actualRecyclerView;
    private RecyclerView.OnScrollListener scrollLoading;

    private boolean isLoading;
    private OnLoadMoreListener callbackPopulate;


    /*************************  START STAGE :: MAIN METHODS AND EVENTS                  *************************/


    public RecyclerDataListAdapter(List<DummyData> Data, RecyclerView recyclerView) {
        this.DataList = Data;
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
                            //                        System.out.println(" onScrolled? E/RecyclerView? /YES");
                            isLoading = true;
                            callbackPopulate.onLoadMore();
                        }else{
                            //                        System.out.println(" onScrolled? E/RecyclerView? /NO ??> isLoading: "+ isLoading + ", callbackPopulate isNull: "+ (callbackPopulate==null) );
                        }//end is loadMore call
                    }
                } catch (Exception e) {
                    //TODO:: java.lang.IllegalStateException check and try to solve this mistake
                    //e.printStackTrace();
                }
            }

        };//end OnScrollListener
    }//END Constructor


    public void resetLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return DataList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }//END getItemViewType

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_list_data, parent, false);
            ((CardView) v.findViewById(R.id.cv_data)).setContentPadding(0,0,0,0);
            ((CardView) v.findViewById(R.id.cv_data)).setMaxCardElevation(10);
            ((CardView) v.findViewById(R.id.cv_data)).setCardElevation(10);
            ((CardView) v.findViewById(R.id.cv_data)).setRadius(5);
            vh = new DataViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }//END onCreateViewHolder

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            DummyData dummyData = (DummyData) DataList.get(position);
            ((DataViewHolder) holder).setData( dummyData );
            ((DataViewHolder) holder).setPosition( position );
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }//END onBindViewHolder

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public void setCallbackPopulate(OnLoadMoreListener onloadMoreListener) {
        this.callbackPopulate = onloadMoreListener;
    }//END setCallbackPopulate


    /*************************  END STAGE :: MAIN METHODS AND EVENTS                  *************************/




    /*************************  START STAGE :: METHODS AND FUNCTIONS       *************************/

    public boolean addScrollLoading(){

        if (actualRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            actualRecyclerView.addOnScrollListener( scrollLoading );
            return true;
        }//end verify correct Layout

        return false;
    }//end addScrollLoading method


    public void cancelScrollLoading(){
        actualRecyclerView.removeOnScrollListener( scrollLoading );
    }//end cancelScrollLoading method



    /** Remove an item of the Recycler List */
    private void removeAt(int position) {
        DataList.remove(position);
        notifyDataSetChanged();//notifyItemRemoved(position);
        notifyItemRangeChanged(position, DataList.size());
    }//end removeAt function

    /*************************  END STAGE :: METHODS AND FUNCTIONS       *************************/





    /*************************  START STAGE :: CLASSES       *************************/

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
    private class DataViewHolder extends RecyclerView.ViewHolder {

        private DummyData dummyData;
        private Context mContext;

        private ProgressBar progressStats;
        private TextView tvPriviaQuestion,
                tvPriviaAnswer,
                tvPriviaOptions,
                tvPriviaLevel;

        private Button btnActivate;
        private ImageButton btnBluetooth,
                btnRun,
                btnEdit,
                btnDeactivate;

        public DataViewHolder(final View v) {
            super(v);
            mContext            = v.getContext();

            progressStats       = (ProgressBar) v.findViewById(R.id.progress_run_win);
            tvPriviaQuestion    = (TextView) v.findViewById(R.id.tv_privia_question);
            tvPriviaAnswer      = (TextView) v.findViewById(R.id.tv_privia_answer);
            tvPriviaOptions     = (TextView) v.findViewById(R.id.tv_privia_options);
            tvPriviaLevel       = (TextView) v.findViewById(R.id.tv_privia_level);

            btnActivate     = (Button) v.findViewById(R.id.btn_activate);
            btnBluetooth    = (ImageButton) v.findViewById(R.id.btn_bluetooth);
            btnRun          = (ImageButton) v.findViewById(R.id.btn_run);
            btnEdit         = (ImageButton) v.findViewById(R.id.btn_edit);
            btnDeactivate   = (ImageButton) v.findViewById(R.id.btn_deactivate);

            btnBluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // If the adapter is null, then Bluetooth is not supported
                    System.out.println("OnClick btnBluetooth");

                }
            });

            btnActivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    //Show with Deactivate Logic
                    alert.setTitle( R.string.title );
                    alert.setMessage( R.string.description );
                    alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("OnClick btnActivate");
                        }
                    });

                    alert.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
            });

            btnRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("OnClick btnRun");
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("OnClick btnEdit");
                }
            });

            btnDeactivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("OnClick btnDeactivate");
                }
            });

        }//END Constructor

        public void setPosition( int newPosition ){
            position = newPosition;
        }//End setPosition


        public void setData(DummyData dummyData ){
            if( dummyData==null ) return;
            this.dummyData = dummyData;


            if( !this.dummyData.getId().isEmpty() ){
                btnEdit.setVisibility(View.GONE);
                btnRun.setVisibility(View.GONE);
                btnActivate.setVisibility(View.VISIBLE);
            }

            tvPriviaQuestion.setText(   this.dummyData.getTitle()   );
            //TODO add Settings page to enable show ANSWER in the card
            tvPriviaAnswer.setText(     this.dummyData.getBody()     );
            tvPriviaOptions.setText(    this.dummyData.getType()   );

            Resources res = mContext.getResources();
            String text = String.format(res.getString(R.string.text_formatted), 1 , 2 );
            tvPriviaLevel.setText( text );

            progressStats.setProgress(100);

        }


    }//END viewHolder

}
