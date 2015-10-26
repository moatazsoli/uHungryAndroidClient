package com.tech.freak.wizardpager.model;

import android.support.v4.app.Fragment;

import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;
import com.tech.freak.wizardpager.ui.PriceFragment;

import java.util.ArrayList;


/**
 * A page asking for a name and an email.
 */
public class PricePage extends Page {
    public static final String PRICE_DATA_KEY = "totalprice";

    public PricePage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return PriceFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Total Pice", mData.getString(PRICE_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
//        return !TextUtils.isEmpty(mData.getString(NAME_DATA_KEY));
        return true;
    }

}
