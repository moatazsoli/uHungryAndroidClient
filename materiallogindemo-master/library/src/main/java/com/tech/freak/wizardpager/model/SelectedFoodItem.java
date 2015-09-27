package com.tech.freak.wizardpager.model;

import java.util.ArrayList;

/**
 * Created by Moataz on 2015-09-12.
 */
public class SelectedFoodItem implements SelectedProduct{

    private String itemName;
    private Double price;
    private String selectedSize;
    private ArrayList<String> selectedOptions;
    private String selectedComments;

    public SelectedFoodItem(String itemName, Double price, String selectedSize, ArrayList<String> selectedOptions, String selectedComments) {
        this.itemName = itemName;
        this.price = price;
        this.selectedSize = selectedSize;
        this.selectedOptions = selectedOptions;
        this.selectedComments = selectedComments;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSelectedSize() {
        return selectedSize;
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

}
