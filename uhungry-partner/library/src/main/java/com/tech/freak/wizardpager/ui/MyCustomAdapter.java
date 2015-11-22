package com.tech.freak.wizardpager.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.tech.freak.wizardpager.R;
import com.tech.freak.wizardpager.model.DrinkItem;
import com.tech.freak.wizardpager.model.Products;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Products> lProductList;
    private Context context;

    public MyCustomAdapter(ArrayList<Products> aInProductList, Context context) {
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
        final View myview = view;

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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                //list.remove(position); //or some other task
                int lquantity = lProductList.get(position).getQuantity();
                if (lquantity > 0) {
                    lquantity--;
                    lProductList.get(position).setQuantity(lquantity);
                }

                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                if (lProductList.get(position) instanceof DrinkItem) {
                    showCustomView(((DrinkItem) lProductList.get(position)).getSizes());
                }
                int lquantity = lProductList.get(position).getQuantity();
                lquantity++;
                lProductList.get(position).setQuantity(lquantity);
                notifyDataSetChanged();
            }
        });

        return view;
    }
    private EditText passwordInput;
    private View positiveAction;

    private void showCustomView(int aInSizes) {
        int layout;
        switch (aInSizes)
        {
            case 1:
                layout = R.layout.drink_details_1_comments_only;
                break;
            case 2:
                layout = R.layout.drink_details_2;
                break;
            case 3:
                layout = R.layout.drink_details_3;
                break;
            case 4:
                layout = R.layout.drink_details_4;
                break;
            default:
                layout = R.layout.drink_details_1_comments_only;
                break;
        }
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.add_drink)
                .customView(layout, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        showToast("Password: " + passwordInput.getText().toString());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                    }
                }).build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.comments);
//        passwordInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                positiveAction.setEnabled(s.toString().trim().length() > 0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });


        int widgetColor = ThemeSingleton.get().widgetColor;

        MDTintHelper.setTint(passwordInput,
                widgetColor == 0 ? ContextCompat.getColor(context, R.color.material_teal_500) : widgetColor);

        dialog.show();
//        positiveAction.setEnabled(false); // disabled by default
    }
    private Toast mToast;
    private void showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}