package com.tech.freak.wizardpager.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Moataz on 2015-09-12.
 */
public class FoodOption {

    public final static int SINGLE_CHOICE = 1;
    public final static int MULTI_CHOICES = 2;

    private int type;
    private String optionName;
    private ArrayList<String> options;

    public FoodOption(String aInOptionName, int aInOptionsType, ArrayList<String> aInOptions)
    {
        optionName = aInOptionName;
        options = aInOptions;
        type = aInOptionsType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }


}
