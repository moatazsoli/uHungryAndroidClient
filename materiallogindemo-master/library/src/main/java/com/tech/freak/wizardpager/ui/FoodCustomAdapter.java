package com.tech.freak.wizardpager.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.tech.freak.wizardpager.R;
import com.tech.freak.wizardpager.model.FoodCache;
import com.tech.freak.wizardpager.model.FoodItem;
import com.tech.freak.wizardpager.model.FoodOption;
import com.tech.freak.wizardpager.model.SelectedFoodItem;

import java.util.ArrayList;
import java.util.HashMap;

import info.hoang8f.android.segmented.SegmentedGroup;

public class FoodCustomAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<FoodItem> lProductList;
    private Context context;
    private FoodItem currentFoodItem;
    private View currentView;
    private MaterialDialog currentDialog;
    private HashMap<String, ArrayList<String>> optionsPlaceHolder;
    private int stepNumber = 0;
    private ArrayList<String> lTempArrayList = new ArrayList<String>();
    public FoodCustomAdapter(ArrayList<FoodItem> aInProductList, Context context) {
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
        currentView = view;
        final String lCurrentItem = lProductList.get(position).getItemName();
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
        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.remove_button);
        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_button);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                //list.remove(position); //or some other task
                currentFoodItem = lProductList.get(position);

                int lQuantity = currentFoodItem.getQuantity();
                if (lQuantity > 0) {
                    lQuantity--;
                    currentFoodItem.setQuantity(lQuantity);
                }
                FoodCache.getInstance().removeProduct(currentFoodItem.getItemName());
                currentFoodItem.reset();
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentFoodItem = lProductList.get(position);
                showOptionsDialog();
//                showSizesCommentsView();
//                int lquantity = lProductList.get(position).getQuantity();
//                lquantity++;
//                lProductList.get(position).setQuantity(lquantity);
                notifyDataSetChanged();
            }
        });

        return view;
    }
    private EditText commentsInput;
    private View positiveAction;

    private void showSizesCommentsView() {
        int layout;
        switch (currentFoodItem.getSizes()) {
            case 1:
                layout = R.layout.food_details_1_comments_only;
                break;
            case 2:
                layout = R.layout.food_details_2;
                break;
            case 3:
                layout = R.layout.food_details_3;
                break;
            default:
                layout = R.layout.food_details_1_comments_only;
                break;
        }
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.add_food)
                .customView(layout, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText lComments = (EditText) dialog.getCustomView().findViewById(R.id.comments);
                        currentFoodItem.setSelectedComments(lComments.getText().toString());
                        String selectedSize;
                        if(currentFoodItem.isSizesAvailable())
                        {
                            selectedSize = currentFoodItem.getSelectedSize();
                        }else{
                            selectedSize = null;
                        }
                        SelectedFoodItem lSelectedFoodItem = new SelectedFoodItem(
                                currentFoodItem.getItemName(),
                                currentFoodItem.getSizesPrices().get(currentFoodItem.getSelectedSize()),
                                selectedSize,
                                currentFoodItem.getSelectedOptions(),
                                currentFoodItem.getSelectedComments()
                        );
                        FoodCache.getInstance().addProduct(lSelectedFoodItem.getItemName(), lSelectedFoodItem);
                        currentFoodItem.reset();
                        int lQuantity = currentFoodItem.getQuantity();
                        lQuantity++;
                        currentFoodItem.setQuantity(lQuantity);
                        showToast("Food Item Added");
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        currentFoodItem.reset();
                    }
                }).build();
        dialog.setCanceledOnTouchOutside(false);
        SegmentedGroup segmented2 = (SegmentedGroup) dialog.getCustomView().findViewById(R.id.segmented2);
        currentDialog = dialog;
        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        positiveAction.setEnabled(false); // disabled by default
        if(segmented2 != null)
        {
            segmented2.setOnCheckedChangeListener(
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            currentDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            if (checkedId == R.id.button21) {
                                currentFoodItem.setSelectedSize("S");
                            } else if (checkedId == R.id.button22) {
                                currentFoodItem.setSelectedSize("M");
                            } else if (checkedId == R.id.button23) {
                                currentFoodItem.setSelectedSize("L");
                            } else if (checkedId == R.id.button24) {
                                currentFoodItem.setSelectedSize("XL");
                            } else {
                                currentFoodItem.setSelectedSize(null);
                                currentDialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                            }
                        }
                    }
            );
        }else{
            currentFoodItem.setSelectedSize("S");
            positiveAction.setEnabled(true); // disabled by default
        }

        //noinspection ConstantConditions
        commentsInput = (EditText) dialog.getCustomView().findViewById(R.id.comments);
//        commentsInput.addTextChangedListener(new TextWatcher() {
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

        MDTintHelper.setTint(commentsInput,
                widgetColor == 0 ? ContextCompat.getColor(context, R.color.material_teal_500) : widgetColor);

        dialog.show();

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

    public void showOptionsDialog() {

        if(currentFoodItem.getOptions() != null)
        {
            if(currentFoodItem.getOptions().size()>0)
            {
                stepNumber = 0;
                FoodOption lFirstOption = currentFoodItem.getOptions().get(stepNumber);
                if(lFirstOption.getType() == FoodOption.MULTI_CHOICES)
                {
                    showMultiChioceDialog(lFirstOption);
                }else if (lFirstOption.getType() == FoodOption.SINGLE_CHOICE)
                {
                    showSingleChoiceDialog(lFirstOption);
                }
            }
        }
    }

    public void showMultiChioceDialog(FoodOption aInFoodOption)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        lTempArrayList.clear();
        // Set the dialog title
//        builder.setTitle(R.string.pick_toppings)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                builder.setMultiChoiceItems(aInFoodOption.getOptions().toArray(new CharSequence[aInFoodOption.getOptions().size()]), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                String itemChecked = currentFoodItem.getOptions().get(stepNumber)
                                        .getOptions().get(which);

                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    lTempArrayList.add(itemChecked);
                                } else if (currentFoodItem.getSelectedOptions().contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    lTempArrayList.remove(itemChecked);
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        currentFoodItem.getSelectedOptions().addAll(lTempArrayList);
                        lTempArrayList.clear();
                        stepNumber++;

                        if (stepNumber < currentFoodItem.getOptions().size()) {
                            FoodOption lFirstOption = currentFoodItem.getOptions().get(stepNumber);
                            if (lFirstOption.getType() == FoodOption.MULTI_CHOICES) {
                                showMultiChioceDialog(lFirstOption);
                            } else if (lFirstOption.getType() == FoodOption.SINGLE_CHOICE) {
                                showSingleChoiceDialog(lFirstOption);
                            }
                        } else {
                            showSizesCommentsView();
                        }
                    }
                })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
//                        currentFoodItem.getSelectedOptions().clear();
                                lTempArrayList.clear();
                                currentFoodItem.reset();                            }
                        });
        builder.create().setCanceledOnTouchOutside(false);
         builder.show();
    }

    public void showSingleChoiceDialog(FoodOption aInFoodOption)
    {
        lTempArrayList.clear();
        lTempArrayList.add(currentFoodItem.getOptions().get(stepNumber)
                .getOptions().get(0));//first choice is always selected in single choice dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder .setSingleChoiceItems(aInFoodOption.getOptions().toArray(new CharSequence[currentFoodItem.getOptions().size()]),0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String itemSelected = currentFoodItem.getOptions().get(stepNumber)
                                .getOptions().get(which);
                        lTempArrayList.clear();
                        lTempArrayList.add(itemSelected);
                    }
                })
                // Set the action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        currentFoodItem.getSelectedOptions().addAll(lTempArrayList);
                        lTempArrayList.clear();
                        stepNumber++;

                        if (stepNumber < currentFoodItem.getOptions().size()) {
                            FoodOption lFirstOption = currentFoodItem.getOptions().get(stepNumber);
                            if (lFirstOption.getType() == FoodOption.MULTI_CHOICES) {
                                showMultiChioceDialog(lFirstOption);
                            } else if (lFirstOption.getType() == FoodOption.SINGLE_CHOICE) {
                                showSingleChoiceDialog(lFirstOption);
                            }
                        } else {
                            showSizesCommentsView();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        lTempArrayList.clear();
                        currentFoodItem.reset();
                    }
                });
        builder.create().setCanceledOnTouchOutside(false);
        builder.show();
    }
}