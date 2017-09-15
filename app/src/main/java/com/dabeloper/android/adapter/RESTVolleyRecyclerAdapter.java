package com.dabeloper.android.adapter;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dabeloper.android.R;
import com.dabeloper.android.activity.RESTVolleyActivity;
import com.dabeloper.android.model.IOSApp;
import com.dabeloper.android.other.CircleTransform;
import com.dabeloper.android.other.OnLoadMoreListener;
import com.dabeloper.android.other.animation.SwipeableAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 07/09/2017.
 */

public class RESTVolleyRecyclerAdapter extends RecyclerView.Adapter implements SwipeableAdapter {

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



    public RESTVolleyRecyclerAdapter(ArrayList<IOSApp> objs, RecyclerView rv) {
        this.objects        = objs;
        this.recyclerView   = rv;
        this.scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                System.out.println( " scrollListener executing: "
                                    + "\n computeVerticalScrollRange() : " + recyclerView.computeVerticalScrollRange()
                                    + "\n computeVerticalScrollOffset(): " + recyclerView.computeVerticalScrollOffset()
                                    + "\n computeVerticalScrollExtent(): " + recyclerView.computeVerticalScrollExtent() );
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

    }

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
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder) {
            IOSApp appData= objects.get(position);
            ((DataViewHolder) holder).setIOSApp( appData );
//            ((DataViewHolder) holder).setPosition( position );
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return objects.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

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
    public void onItemDismissLR(int position) {
        //Avoid create multiple Tasks
        if( !isBusy ){
            isBusy = true;
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBusy = false;
                }
            }, 5000);
        }

        removeAt(position);

        if( objects.isEmpty() ){
            isBusy = false;
            callback.onLoadMore();
        }

    }

    /**
     * From Custom Class :: SwipeableAdapter
     *  TurnOn when the item is move from Right-Left or Left-Right
     * */
    @Override
    public void onItemDismissRL(int position) {
//        objects.remove(position);
//        notifyItemRemoved(position);
    }

    /*************************  END STAGE :: METHODS AND FUNCTIONS       *************************/


    /*************************  START STAGE :: CLASSES       *************************/

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
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
                            tvCreatedBy;

        private ImageView imageView;

        private DataViewHolder(View itemView) {
            super(itemView);

            tvTitle         = (TextView) itemView.findViewById(R.id.tv_body_title);
            tvDesciption    = (TextView) itemView.findViewById(R.id.tv_body_description);
            tvCreatedBy     = (TextView) itemView.findViewById(R.id.tv_body_created_by);

            imageView = (ImageView) itemView.findViewById(R.id.iv_header);
        }

        private void setIOSApp( IOSApp app ){
            try {
                tvTitle.setText( app.getTitle() );
                tvDesciption.setText( app.getName() );

                Glide
                        .with( imageView.getContext() )
                        .load(app.getImages()[0])
                        .transform(new CircleTransform(recyclerView.getContext()))
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//END DataViewHolder Class
}
