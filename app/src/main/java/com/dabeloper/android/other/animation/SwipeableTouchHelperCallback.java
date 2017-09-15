package com.dabeloper.android.other.animation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.dabeloper.android.R;
import com.dabeloper.android.activity.RESTVolleyActivity;
import com.dabeloper.android.adapter.RESTVolleyBNAVRecyclerAdapter;
import com.dabeloper.android.model.IOSApp;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 08/09/2017.
 *
 * https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
 */

public class SwipeableTouchHelperCallback extends ItemTouchHelper.Callback {

    private final SwipeableAdapter mAdapter;
    private Paint p = new Paint();

    public SwipeableTouchHelperCallback(SwipeableAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //viewHolder.itemView.setAlpha(0.5f);
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * To change position moving from Top or Bottom
     * */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        //Evitar Move en un componente top ProgressBar
        if( viewHolder instanceof RESTVolleyBNAVRecyclerAdapter.ProgressViewHolder ){
            return false;
        }

        //viewHolder.itemView.setAlpha(1f);
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * To Slide the item, see @Method onChildDraw
     * */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Evitar Swipe en un componente top ProgressBar
        if( viewHolder instanceof RESTVolleyBNAVRecyclerAdapter.ProgressViewHolder ) {
            return;
        }
            System.out.println( "onSwiped :: direction : "+direction);
        switch ( direction ){
            case ItemTouchHelper.END:
                mAdapter.onItemDismissLR(viewHolder.getAdapterPosition());
                //viewHolder.itemView.setRight(0);
                break;
            case ItemTouchHelper.START:
                mAdapter.onItemDismissRL(viewHolder.getAdapterPosition());
                //viewHolder.itemView.setLeft(0);
                break;
        }


    }

    /**
     *
     * Override Method to Draw a Component out of bounds
     *  To show when a RecyclerView Item is swipe from Left to Right or Right to Left
     * */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        //Evitar Swipe en un componente top ProgressBar
        if( viewHolder instanceof RESTVolleyBNAVRecyclerAdapter.ProgressViewHolder ) {
            return;
        }

        View itemView = viewHolder.itemView;

        //https://stackoverflow.com/questions/31418905/recyclerview-itemtouchhelper-swipe-remove-animation
        //Show drawable of Left to Right
//        Drawable dl = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.round_button_default_trash);
//        dl.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
//        dl.setAlpha(1);
//        dl.draw(c);
//
//        //Show drawable of Right to Left
//        Drawable d = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.round_button_default_secondary);
//        d.setBounds( (int) dX + itemView.getWidth(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
//        d.draw(c);

        //https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            Bitmap icon;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;
            p.setColor(Color.parseColor("#DDDDDD"));

            if(dX > 0){
                if( dX>(itemView.getWidth()/3) ) p.setColor(Color.parseColor(/*"#388E3C"*/"#FFE57F"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                c.drawRect(background,p);
                c.clipRect(background);
                icon = BitmapFactory.decodeResource(recyclerView.getContext().getResources(), R.drawable.ic_action_important);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            } else {
                if( dX<-(itemView.getWidth()/3) ) p.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                c.drawRect(background,p);
                c.clipRect(background);
                icon = BitmapFactory.decodeResource(recyclerView.getContext().getResources(), R.drawable.ic_action_discard);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                c.drawBitmap(icon,null,icon_dest,p);
            }

            c.restore();
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }//END OnChildDraw


}