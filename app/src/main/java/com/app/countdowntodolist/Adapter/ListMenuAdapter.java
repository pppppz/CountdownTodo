package com.app.countdowntodolist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.countdowntodolist.Model.Menu;
import com.app.countdowntodolist.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.ViewHolder> {

    private List<Menu> menus;
    private Context mContext;


    public ListMenuAdapter(Context context, List<Menu> dataset) {
        menus = dataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_row_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Menu menu = menus.get(position);
        viewHolder.title.setText(menu.getTitle());
        viewHolder.icon.setImageResource(menu.getIcon());
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CircleImageView icon;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.menu_title);
            icon = (CircleImageView) view.findViewById(R.id.menu_icon);
        }
    }
}