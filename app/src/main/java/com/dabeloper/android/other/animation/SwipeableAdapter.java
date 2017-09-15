package com.dabeloper.android.other.animation;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 08/09/2017.
 */

public interface SwipeableAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismissLR(int position);
    void onItemDismissRL(int position);
}