package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class FoodItem implements Products{

    public int sizes;
    private HashMap<String,Double> sizesPrices;
    public String itemName;
    private int quantity;
    private String selectedSize;
    private ArrayList<FoodOption> options;
    private ArrayList<String> selectedOptions;
    private String selectedComments;
    private Boolean sizesAvailable;

    public FoodItem(String aInItemName, HashMap<String, Double> aInSizesPrices,
                    ArrayList<FoodOption> aInOptions) {
        itemName = aInItemName;
        sizesPrices = aInSizesPrices;
        options = aInOptions;
        sizes = aInSizesPrices.size();
        selectedOptions = new ArrayList<String>();
        quantity = 0;
        if(sizes > 1)
        {
            sizesAvailable = true;
        }else{
            sizesAvailable = false;
        }
    }

    public Boolean isSizesAvailable() {
        return sizesAvailable;
    }

    public void setSizesAvailable(Boolean sizesAvailable) {
        this.sizesAvailable = sizesAvailable;
    }

    public String getReviewString()
    {
        return itemName;
    }

    public int getSizes() {
        return sizes;
    }

    public void setSizes(int sizes) {
        this.sizes = sizes;
    }

    public HashMap<String, Double> getSizesPrices() {
        return sizesPrices;
    }

    public void setSizesPrices(HashMap<String, Double> sizesPrices) {
        this.sizesPrices = sizesPrices;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public ArrayList<FoodOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<FoodOption> options) {
        this.options = options;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public ArrayList<String> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(ArrayList<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public String getSelectedComments() {
        return selectedComments;
    }

    public void setSelectedComments(String selectedComments) {
        this.selectedComments = selectedComments;
    }

    public void reset()
    {
        setSelectedComments(null);
        setSelectedOptions(new ArrayList<String>());
        setSelectedSize(null);
    }

}
