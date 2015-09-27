package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class FoodCache {

    HashMap<String,ArrayList<SelectedProduct>> selectedProducts = new HashMap<String,ArrayList<SelectedProduct>>();

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

    public void addProduct(String aInProductName, SelectedProduct aInSelectedProduct)
    {
        if(selectedProducts.containsKey(aInProductName))
        {
            selectedProducts.get(aInProductName).add(aInSelectedProduct);
        }else{
            selectedProducts.put(aInProductName,new ArrayList<SelectedProduct>());
            selectedProducts.get(aInProductName).add(aInSelectedProduct);
        }
    }

    public void removeProduct(String aInProductName)
    {
        if(selectedProducts.containsKey(aInProductName))
        {
            ArrayList<SelectedProduct> lArray =  selectedProducts.get(aInProductName);
            if(lArray.size()>1)
            {
                lArray.remove(lArray.size()-1);
            }else if (lArray.size() == 1){
                selectedProducts.remove(aInProductName);
            }
        }
    }


}
