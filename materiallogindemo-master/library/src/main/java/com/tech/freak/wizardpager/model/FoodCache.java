package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class FoodCache {

    private HashMap<String,ArrayList<SelectedFoodItem>> selectedProducts = new HashMap<String,ArrayList<SelectedFoodItem>>();

    private static FoodCache instance = null;
    protected FoodCache() {
        // Exists only to defeat instantiation.
    }
    public static FoodCache getInstance() {
        if(instance == null) {
            instance = new FoodCache();
        }
        return instance;
    }

    public void addProduct(String aInProductName, SelectedFoodItem aInSelectedProduct)
    {
        if(selectedProducts.containsKey(aInProductName))
        {
            selectedProducts.get(aInProductName).add(aInSelectedProduct);
        }else{
            selectedProducts.put(aInProductName,new ArrayList<SelectedFoodItem>());
            selectedProducts.get(aInProductName).add(aInSelectedProduct);
        }
    }

    public void removeProduct(String aInProductName)
    {
        if(selectedProducts.containsKey(aInProductName))
        {
            ArrayList<SelectedFoodItem> lArray =  selectedProducts.get(aInProductName);
            if(lArray.size()>1)
            {
                lArray.remove(lArray.size()-1);
            }else if (lArray.size() == 1){
                selectedProducts.remove(aInProductName);
            }
        }
    }

    public HashMap<String,ArrayList<SelectedFoodItem>> getSelectedProducts()
    {
        return selectedProducts;
    }


}
