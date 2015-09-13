package com.tech.freak.wizardpager.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.common.collect.Iterables;
import com.tech.freak.wizardpager.R;
import com.tech.freak.wizardpager.model.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<ProductItem> lProductList;
    private Context context;

    public MyCustomAdapter(ArrayList<ProductItem> aInProductList, Context context) {
        lProductList = aInProductList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lProductList.size();
    }

    @Override
    public Object getItem(int pos) {
        return lProductList.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

//    public void setlMap(HashMap<String,Integer> aInMap)
//    {
//        lMap.putAll(aInMap);
//    }

//    @Override
//    public long getItemId(int pos) {
//        return list.get(pos).getId();
//        //just return 0 if your list items do not have an Id variable.
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_view, null);
        }

        String lCurrentItem = lProductList.get(position).getItemName();
        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(lCurrentItem);

        TextView counter = (TextView)view.findViewById(R.id.counter);
        if(lProductList.get(position).getQuantity() > 0)
        {
            counter.setText(""+lProductList.get(position).getQuantity());
        }else{
            counter.setText("");
        }
        //TextView counter = (TextView)view.findViewById(R.id.counter);


        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.remove_button);
        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_button);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //list.remove(position); //or some other task
                int lquantity = lProductList.get(position).getQuantity();
                    if(lquantity>0) {
                        lquantity--;
                        lProductList.get(position).setQuantitiy(lquantity);
                    }

                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                int lquantity = lProductList.get(position).getQuantity();
                lquantity++;
                lProductList.get(position).setQuantitiy(lquantity);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}