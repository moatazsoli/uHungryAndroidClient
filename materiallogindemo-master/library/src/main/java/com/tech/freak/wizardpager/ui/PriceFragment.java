package com.tech.freak.wizardpager.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.tech.freak.wizardpager.R;
import com.tech.freak.wizardpager.model.DrinksCache;
import com.tech.freak.wizardpager.model.FoodCache;
import com.tech.freak.wizardpager.model.PricePage;
import com.tech.freak.wizardpager.model.SelectedDrinkItem;
import com.tech.freak.wizardpager.model.SelectedFoodItem;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TechFreak on 04/09/2014.
 */
public class PriceFragment extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private PricePage mPage;
    private TextView mTotalPriceView;

    public static PriceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        PriceFragment fragment = new PriceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PriceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (PricePage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_price, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mTotalPriceView = ((TextView) rootView.findViewById(R.id.totalprice));
//        mTotalPriceView.setText(mPage.getData().getString(PricePage.PRICE_DATA_KEY));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        double lTotalPrice = 0.0;
        HashMap<String,ArrayList<SelectedFoodItem>> lSelectedFoodProducts =
                FoodCache.getInstance().getSelectedProducts();

        for(String lProduct: lSelectedFoodProducts.keySet())
        {
            ArrayList<SelectedFoodItem> lFoodItems = lSelectedFoodProducts.get(lProduct);
            for (SelectedFoodItem lSelectedFood : lFoodItems)
            {
                lTotalPrice = lTotalPrice + lSelectedFood.getPrice();
            }
        }
        HashMap<String,ArrayList<SelectedDrinkItem>> lSelectedDrinkProducts =
                DrinksCache.getInstance().getSelectedProducts();

        for(String lProduct: lSelectedDrinkProducts.keySet())
        {
            ArrayList<SelectedDrinkItem> lDrinkItems = lSelectedDrinkProducts.get(lProduct);
            for (SelectedDrinkItem lSelectedDrink : lDrinkItems)
            {
                lTotalPrice = lTotalPrice + lSelectedDrink.getPrice();
            }
        }

        // taxes and delivery fare.
        lTotalPrice = lTotalPrice * 1.13;
        if(lTotalPrice > 0 && lTotalPrice < 5)
        {
            lTotalPrice = lTotalPrice + 2;
        }
        if(lTotalPrice >= 5 && lTotalPrice < 10)
        {
            lTotalPrice = lTotalPrice + 3;
        }
        if(lTotalPrice > 10)
        {
            lTotalPrice = lTotalPrice + 4;
        }

        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.UP);
        String total_price = "$"+df.format(lTotalPrice);
        mTotalPriceView.setText(total_price);

        mPage.getData().putString(PricePage.PRICE_DATA_KEY,total_price);
        mPage.notifyDataChanged();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mTotalPriceView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }
}
