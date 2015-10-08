package com.app.countdowntodolist.Listener;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.app.countdowntodolist.TaskList_Fragment;


public class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        //super.onScrollStateChanged(recyclerView, newState);
        try {

            int firstPos = TaskList_Fragment.layoutManager.findFirstCompletelyVisibleItemPosition();

            if (firstPos > 0) {
                TaskList_Fragment.PullToRefresh.setEnabled(false);
            } else {
                TaskList_Fragment.PullToRefresh.setEnabled(true);
                if (TaskList_Fragment.mRecyclerView.getScrollState() == 1)
                    if (TaskList_Fragment.PullToRefresh.isRefreshing())
                        TaskList_Fragment.mRecyclerView.stopScroll();
            }

        } catch (Exception e) {
            Log.e("Recy ", "Scroll Error : " + e.getLocalizedMessage());
        }
    }
}
