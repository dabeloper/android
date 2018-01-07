package com.dabeloper.android.adapter;

import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dabeloper.android.R;
import com.dabeloper.android.activity.RESTVolleyBNAVActivity;
import com.dabeloper.android.model.IOSApp;
import com.dabeloper.android.other.CircleTransform;
import com.dabeloper.android.other.OnLoadMoreListener;
import com.dabeloper.android.other.animation.SwipeableAdapter;
import com.dabeloper.android.sqlite_model.DbHandler;
import com.dabeloper.android.sqlite_model.IOSActionSQLite;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 07/09/2017.
 */

public class RESTVolleyBNAVRecyclerAdapter extends RecyclerView.Adapter implements SwipeableAdapter {

    //Static vars to indicate when is a normal Item or is a loading item
    private static int  VIEW_ITEM = 1,
                        VIEW_PROG = 2;

    //A list to populate the RecyclerView
    private ArrayList<IOSApp> objects;

    //A callback to indicate when is filling the data
    private OnLoadMoreListener callback;

    //A recyclerView as List
    private RecyclerView recyclerView;

    //A ScrollListener to know when scroll and populate when reach the end of List
    private RecyclerView.OnScrollListener scrollListener;

    //Flag to know when is loading data (callback.LoadMore)
    private Boolean isLoading = false;

    //To know when a item is under a Swipeable action
    private Boolean isBusy = false;

    //Handler t SQLite Android Database
    private DbHandler dbHandler;
    //Handler to methods for IOSActionApp in SQLite
    //Not necessary is an static class
    //private IOSActionSQLite iosActionSQLite;

    public RESTVolleyBNAVRecyclerAdapter(ArrayList<IOSApp> objs, RecyclerView rv) {
        this.dbHandler      = new DbHandler(rv.getContext());
        this.objects        = objs;
        this.recyclerView   = rv;
        this.scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                System.out.println( " scrollListener executing: "
//                                    + "\n computeVerticalScrollRange() : " + recyclerView.computeVerticalScrollRange()
//                                    + "\n computeVerticalScrollOffset(): " + recyclerView.computeVerticalScrollOffset()
//                                    + "\n computeVerticalScrollExtent(): " + recyclerView.computeVerticalScrollExtent() );
                //Only execute the action when the user scroll to the End
                    if( recyclerView.computeVerticalScrollRange() ==
                            ( recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent() ) ) {

                        if ( !isBusy &&
                                !isLoading &&
                                    callback != null) {
                            System.out.println(" scrollListener? E/RecyclerView? /YES");
                            isLoading = true;
                            callback.onLoadMore();
                        }else{
                            System.out.println(" scrollListener? E/RecyclerView? /NO ??> isLoading: "+ isLoading
                                                + ", callbackPopulate isNull: "+ (callback==null) );
                        }//END is loadMore call

                    }//END check scrollEnd

                }
        };

    }//END Constructor




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            final View v = LayoutInflater
                                            .from(parent.getContext())
                                            .inflate( R.layout.item_list_volley_object, parent, false);
            vh = new DataViewHolder(v);
        } else {
            View v = LayoutInflater
                                    .from(parent.getContext())
                                    .inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }//END onCreateViewHolder




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            IOSApp appData= objects.get(position);
            final DataViewHolder dataHolder = ((DataViewHolder) holder);
            dataHolder.setIOSApp( appData );
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dataHolder.showMark();
                    return false;
                }
            });
//            ((DataViewHolder) holder).setPosition( position );
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }//END onBindViewHolder




    @Override
    public int getItemViewType(int position) {
        return objects.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }//END getItemViewType




    @Override
    public int getItemCount() {
        return objects.size();
    }

    /*************************  END STAGE :: MAIN METHODS AND EVENTS                  *************************/


    /*************************  START STAGE :: METHODS AND FUNCTIONS       *************************/

    public void resetLoaded() {
        isLoading = false;
    }

    public void setCallback(OnLoadMoreListener callback) {
        this.callback = callback;
    }

    public boolean addScrollLoading(){
        System.out.println(" addScrollLoading ");
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            System.out.println(" Scrolling Listener added ");
            recyclerView.addOnScrollListener( scrollListener );
            return true;
        }else{
            System.out.println(" Scrolling Listener NOT added ");
        }//end verify correct Layout

        return false;
    }//end addScrollLoading method


    public void cancelScrollLoading(){
        recyclerView.removeOnScrollListener( scrollListener );
    }//end cancelScrollLoading method



    /** Remove an item of the Recycler List */
    private void removeAt(int position) {
        objects.remove(position);
        notifyDataSetChanged();//notifyItemRemoved(position);
        notifyItemRangeChanged(position, objects.size());
    }//end removeAt function

    /**
     * From Custom Class :: SwipeableAdapter
     *  TurnOn when the item is move from Down-Up or Up-Down
     *  this needs resort all the affected items
     * */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(objects, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(objects, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * From Custom Class :: SwipeableAdapter
     *  TurnOn when the item is move from Right-Left or Left-Right
     * */
    @Override
    public void onItemDismissLR(final int position) {
        //verificar que el item este sin tipo
        if( objects.get(position).hasType() ){
            notifyItemChanged(position);
            return;
        }
        addType(position);
        //SnackBar para deshacer accion
        final Snackbar snackBar = Snackbar.make(recyclerView, R.string.updated, Snackbar.LENGTH_LONG);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBar.getView().getLayoutParams();
        params.setMargins(0, 0, 0, RESTVolleyBNAVActivity.getInstance().getBottomNavigationViewHeight());
        snackBar.getView().setLayoutParams(params);
        snackBar.setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeType(position);
                        snackBar.dismiss();
                    }
                });
//        snackBar.addCallback( new Snackbar.Callback() {
//            @Override
//            public void onDismissed(Snackbar snackbar, int event) {
//                switch(event) {
//                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
//                        break;
//                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
//                        break;
//                }
//                //saved logic to SQLite
//                addType(position);
//            }
//
//            @Override
//            public void onShown(Snackbar snackbar) {
//            }
//        }
//        );
        snackBar.show();
    }
    private void addType( int position ){
        IOSApp iap = objects.get(position);
        if( IOSActionSQLite.newOne( dbHandler.getWritableDatabase(), iap.getTextId(), iap.getOwner() ) > -1 ){
            iap.setHasType(true);
        }
        notifyItemChanged(position);
    }

    /**
     * From Custom Class :: SwipeableAdapter
     *  TurnOn when the item is move from Right-Left or Left-Right
     * */
    @Override
    public void onItemDismissRL(final int position) {
        //verificar que el item tenga tipo
        if( !objects.get(position).hasType() ){
            notifyItemChanged(position);
            return;
        }
        removeType(position);
        //SnackBar para deshacer accion
        final Snackbar snackBar = Snackbar.make(recyclerView, R.string.updated, Snackbar.LENGTH_LONG);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBar.getView().getLayoutParams();
        params.setMargins(0, 0, 0, RESTVolleyBNAVActivity.getInstance().getBottomNavigationViewHeight());
        snackBar.getView().setLayoutParams(params);
        snackBar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addType(position);
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }
    private void removeType( int position ){
        //removed logic to SQLite
        final IOSApp iap = objects.get(position);
        if( IOSActionSQLite.removeOne( dbHandler.getWritableDatabase(), iap.getTextId() ) > -1 ){
            iap.setHasType(false);
        }
        notifyItemChanged(position);
    }

    /*************************  END STAGE :: METHODS AND FUNCTIONS       *************************/


    /*************************  START STAGE :: CLASSES       *************************/

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            try {
                //change the color to the circular ProgressBar
                progressBar.getIndeterminateDrawable().setColorFilter(progressBar.getContext().getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }//end progress viewHolder



    //Needs class to inflate the cardView
    private class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView    tvTitle,
                            tvDesciption,
                            tvCreatedBy,
                            tvPrice;

        private ImageView ivHeader, ivType, ivCheckMark;

        private DataViewHolder(View itemView) {
            super(itemView);

            tvTitle         = (TextView) itemView.findViewById(R.id.tv_body_title);
            tvDesciption    = (TextView) itemView.findViewById(R.id.tv_body_description);
            tvCreatedBy     = (TextView) itemView.findViewById(R.id.tv_body_created_by);
            tvPrice         = (TextView) itemView.findViewById(R.id.tv_body_price);

            ivType   = (ImageView) itemView.findViewById(R.id.iv_type);
            ivCheckMark = (ImageView) itemView.findViewById(R.id.iv_check_mark);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_header);

            ivCheckMark.setVisibility( View.GONE );
            ivType.setVisibility(View.GONE);
        }

        private void setIOSApp( IOSApp app ){
            try {
                tvTitle.setText( app.getTitle() );
                tvDesciption.setText( app.getName() );
                tvCreatedBy.setText( app.getOwner() );
                tvPrice.setText( app.getPrice() );

                //Es necesario ocultar los componentes dinamicos sin importar que en constructor lo hayamos realizado
                //porque el recycler guarda los estado de los anteriores items y tambien los estados de VISIBILIDAD de los componentes
                ivType.setVisibility(View.GONE);
                ivCheckMark.setVisibility( View.GONE );

                Glide
                        .with( ivHeader.getContext() )
                        .load(app.getImages()[1])
                        .transform(new CircleTransform(recyclerView.getContext()))
                        .into(ivHeader);

                if( app.hasType() ){
                    ivType.setVisibility(View.VISIBLE);
                }else{
                    Cursor c = IOSActionSQLite.getOne( dbHandler.getReadableDatabase() , app.getTextId() );
                    System.out.println( "IOSActionSQLite.getOne( dbHandler.getReadableDatabase() , "+app.getTextId()+" ) : " +c.getCount() );
                    if( c.getCount() == 1 ){
                        ivType.setVisibility(View.VISIBLE);
                        app.setHasType(true);
                    }
                }//END type logic

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//END setIOSApp

        public void showMark(){
            ivCheckMark.setVisibility( ivCheckMark.isShown() ? View.GONE : View.VISIBLE);
        }//END showMark


    }//END DataViewHolder Class
}
