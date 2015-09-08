package com.app.countdowntodolist.Listener;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.countdowntodolist.MainList_Fragment;


public class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        //super.onScrollStateChanged(recyclerView, newState);
        try {

            int firstPos = MainList_Fragment.layoutManager.findFirstCompletelyVisibleItemPosition();

            if (firstPos > 0) {
                MainList_Fragment.PullToRefresh.setEnabled(false);
            } else {
                MainList_Fragment.PullToRefresh.setEnabled(true);
                if (MainList_Fragment.mRecyclerView.getScrollState() == 1)
                    if (MainList_Fragment.PullToRefresh.isRefreshing())
                        MainList_Fragment.mRecyclerView.stopScroll();
            }

        } catch (Exception e) {
            Log.e("Recy ", "Scroll Error : " + e.getLocalizedMessage());
        }
    }
}
