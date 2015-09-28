package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class DrinksCache {

    HashMap<String,ArrayList<SelectedDrinkItem>> selectedProducts = new HashMap<String,ArrayList<SelectedDrinkItem>>();

    private static DrinksCache instance = null;
    protected DrinksCache() {
        // Exists only to defeat instantiation.
    }
    public static DrinksCache getInstance() {
        if(instance == null) {
            instance = new DrinksCache();
        }
        return instance;
    }

    public void addProduct(String aInProductName, SelectedDrinkItem aInSelectedProduct)
    {
        if(selectedProducts.containsKey(aInProductName))
        {
            selectedProducts.get(aInProductName).add(aInSelectedProduct);
        }else{
            selectedProducts.put(aInProductName,new ArrayList<SelectedDrinkItem>());
            selectedProducts.get(aInProductName).add(aInSelectedProduct);
        }
    }

    public void removeProduct(String aInProductName)
    {
        if(selectedProducts.containsKey(aInProductName))
        {
            ArrayList<SelectedDrinkItem> lArray =  selectedProducts.get(aInProductName);
            if(lArray.size()>1)
            {
                lArray.remove(lArray.size()-1);
            }else if (lArray.size() == 1){
                selectedProducts.remove(aInProductName);
            }
        }
    }

    public HashMap<String,ArrayList<SelectedDrinkItem>> getSelectedProducts()
    {
        return selectedProducts;
    }


}
