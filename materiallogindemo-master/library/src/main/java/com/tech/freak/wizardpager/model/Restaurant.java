package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class Restaurant {

    public static String RESTURANT_NAME;
    public LinkedHashMap<String, ArrayList<ProductItem>> products;

    public Restaurant(String aInRestaurantName) {
        RESTURANT_NAME = aInRestaurantName;
    }

    public void setProducts(LinkedHashMap<String, ArrayList<ProductItem>> aInProducts)
    {
        products = aInProducts;
    }

    public LinkedHashMap<String, ArrayList<ProductItem>> getProducts()
    {
        return products;
    }

    public String getName()
    {
        return RESTURANT_NAME;
    }
}
