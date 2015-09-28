package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class DrinkItem implements Products{

    public static final int NO_OPTIONS = 0;
    public static final int SIGNLE_CHOICE = 1;
    public static final int MULTI_CHOICES = 2;

    public int sizes;
    private HashMap<String,Double> sizesPrices;
    public String itemName;
    private int quantity;
    private String selectedSize;
    private int optionsType;
    private ArrayList<String> options;
    private ArrayList<String> selectedOptions;
    private String selectedComments;

//
//    public DrinkItem(String aInItemName, int aInSizes) {
//        itemName = aInItemName;
//        sizes = aInSizes;
//    }

    public DrinkItem(String aInItemName, HashMap<String,Double> aInSizesPrices,
                     int aInOptionsType, ArrayList<String> aInOptions) {
        itemName = aInItemName;
        sizesPrices = aInSizesPrices;
        optionsType = aInOptionsType;
        options = aInOptions;
        sizes = aInSizesPrices.size();
        selectedOptions = new ArrayList<String>();
        quantity = 0;
    }


    public String getReviewString()
    {
        return itemName;
    }

    public int getOptionsType() {
        return optionsType;
    }

    public void setOptionsType(int optionsType) {
        this.optionsType = optionsType;
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

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
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
