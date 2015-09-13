package com.tech.freak.wizardpager.model;

/**
 * Created by Moataz on 2015-09-12.
 */
public class ProductItem {

    public Double PRICE;
    public String ITEM_NAME;
    private int quantitiy;

    public ProductItem(String aInItemName, Double aInPrice) {
        ITEM_NAME = aInItemName;
        PRICE = aInPrice;
        quantitiy = 0;
    }

    public int getQuantity()
    {
        return quantitiy;
    }
    public void setQuantitiy(int aInQuantity)
    {
        quantitiy = aInQuantity;
    }
    public String getItemName()
    {
        return ITEM_NAME;
    }

    public String getReviewString()
    {
        return ITEM_NAME +" ("+quantitiy+")";
    }
}
