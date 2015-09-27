package com.tech.freak.wizardpager.model;

/**
 * Created by Moataz on 2015-09-12.
 */
public class ProductItem implements Products {

    public Double PRICE;
    public String ITEM_NAME;
    private int quantity;

    public ProductItem(String aInItemName, Double aInPrice) {
        ITEM_NAME = aInItemName;
        PRICE = aInPrice;
        quantity = 0;
    }

    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int aInQuantity)
    {
        quantity = aInQuantity;
    }
    public String getItemName()
    {
        return ITEM_NAME;
    }

    public String getReviewString()
    {
        return ITEM_NAME +" ("+quantity+")";
    }
}
