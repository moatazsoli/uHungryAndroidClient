/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tech.freak.wizardpager.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tech.freak.wizardpager.ui.AddRemoveItemsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A page offering the user a number of mutually exclusive choices.
 */
public class AddRemoveItemsPage extends Page {
    protected ArrayList<Products> mChoices;

    public AddRemoveItemsPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return AddRemoveItemsFragment.create(getKey());
    }

    public ArrayList<Products> getAllChoices() {
        return mChoices;
    }

    public int getOptionCount() {
        return mChoices.size();
    }

//    @Override
//    public void getReviewItems(ArrayList<ReviewItem> dest) {
//        dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY), getKey()));
//    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public AddRemoveItemsPage setChoices(ArrayList<Products> choices) {
        mChoices = choices;
        return this;
    }

    public AddRemoveItemsPage setValue(String value) {
        mData.putString(SIMPLE_DATA_KEY, value);
        return this;
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        StringBuilder sb = new StringBuilder();

        for(Products lChoices: mChoices)
        {
            sb.append(lChoices.getReviewString());
            sb.append("\n");
        }

        dest.add(new ReviewItem(getTitle(), sb.toString(), getKey()));
    }
}
