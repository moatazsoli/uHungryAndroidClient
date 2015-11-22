package com.example.android.wizardpager;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Order> orders;

    public CustomListAdapter(Activity activity, List<Order> orders) {
        this.activity = activity;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int location) {
        return orders.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView id = (TextView) convertView.findViewById(R.id.title);
        TextView restaurant = (TextView) convertView.findViewById(R.id.restaurant);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        // getting movie data for the row
        Order lOrder = orders.get(position);


        // title
        id.setText(lOrder.getId());
        restaurant.setText(lOrder.getLocation());
        status.setText(lOrder.getStatus());
        time.setText(lOrder.getDate());

        return convertView;
    }

}