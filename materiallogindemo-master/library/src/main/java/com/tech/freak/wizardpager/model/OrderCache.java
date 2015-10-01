package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class OrderCache {

    private HashMap<String,ArrayList<SelectedFoodItem>> selectedProducts = new HashMap<String,ArrayList<SelectedFoodItem>>();

    private static OrderCache instance = null;
    protected OrderCache() {
        // Exists only to defeat instantiation.
    }
    public static OrderCache getInstance() {
        if(instance == null) {
            instance = new OrderCache();
        }
        return instance;
    }

    private String selectedLocation;

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }





}
