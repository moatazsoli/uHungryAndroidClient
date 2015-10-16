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

package com.example.android.wizardpager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.DrinksCache;
import com.tech.freak.wizardpager.model.FoodCache;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.OrderCache;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;
import com.tech.freak.wizardpager.model.SelectedDrinkItem;
import com.tech.freak.wizardpager.model.SelectedFoodItem;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;
import com.tech.freak.wizardpager.ui.StepPagerStrip;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {

    private static final int REQUEST_DROPINUI = 100;

    private ReviewFragment reviewFragment;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String username;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private ProgressDialog mProgressDialog;
    private boolean mEditingAfterReview;
    private String lCurrentOrder;
    private Double lTotalPrice;

    private AbstractWizardModel mWizardModel = new SandwichWizardModel(this);

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    private void addDrawerItems() {
        final String[] osArray = { "My Orders", "History", "Payment", "Settings", "Terms of Condition", "Privacy Policy",
        "Signout"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lCurrentListItem = osArray[position];
                if(lCurrentListItem != null)
                {
                    if(lCurrentListItem.equals("Signout"))
                    {
                        SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = mySPrefs.edit();
                        editor.remove("username");
                        editor.apply();
                        setResult(RESULT_OK);
                        finish();

                    }else if(lCurrentListItem.equals("My Orders"))
                    {
                        String uri = String.format("https://uhungry-valyriacorp.c9.io/customers/getopenorders/?username=%1$s",
                                username);

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if(response.length() != 0)
                                        {
                                            buildOrdersList(response);
                                            Iterator lIterator = response.keys();
                                            while(lIterator.hasNext())
                                            {
                                                String key = (String) lIterator.next();
                                                try {
                                                    JSONObject lJsonObject = (JSONObject) response.getJSONObject(key);
                                                    String location = lJsonObject.getString("location");
                                                    String orderdetails = lJsonObject.getString("orderdetails");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            // Create new fragment and transaction
                                        }else{
                                            Toast.makeText(getBaseContext(), "No orders available", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        HttpSingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);

                    }else if(lCurrentListItem.equals("History"))
                    {
//                        buildOrdersList();
                    }else{
                        Toast.makeText(MainActivity.this, "Selected item: " + lCurrentListItem, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void buildOrdersList(final JSONObject aInOpenOrdersJson)
    {
        final HashMap<Integer,String> lMap = new HashMap<Integer,String>();
        lMap.clear();//maybe final will not recreate it everytime just to double check
        //remove clear later
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select an Order:");

//        LayoutInflater inflater = getLayoutInflater();
//        View convertView = (View) inflater.inflate(R.layout.open_orders_layout, null);
//        builderSingle.setView(convertView);
//        builderSingle.setTitle("List");
//        ListView lv = (ListView) convertView.findViewById(R.id.open_orders_list);
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
//        lv.setAdapter(arrayAdapter);


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1);
        Iterator lIterator = aInOpenOrdersJson.keys();
        int counter = 0;
        while(lIterator.hasNext())
        {
            String key = (String) lIterator.next();
            try {
                JSONObject lJsonObject = (JSONObject) aInOpenOrdersJson.getJSONObject(key);
                String date = lJsonObject.getString("date");
                String status = lJsonObject.getString("status");
                arrayAdapter.add(date+"-order#"+key+" -"+status);
                lMap.put(counter, key);
                counter++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        arrayAdapter.add("Archit");
        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = lMap.get(which);
                        try {
                            final JSONObject lJsonObject = (JSONObject) aInOpenOrdersJson.getJSONObject(key);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                    MainActivity.this);
                            builderInner.setMessage(
                                    "OrderID : "+ lJsonObject.getString("id")+"\n"+
                                    "Date    : "+ lJsonObject.getString("date")+"\n"+
                                    "Location: "+ lJsonObject.getString("location")+"\n"+
                                    "Status  : "+ lJsonObject.getString("status")+"\n"+
                                    "Price   : "+ lJsonObject.getString("totalprice")+"\n"+
                                    "Details :\n"+ lJsonObject.getString("orderdetails")+"\n"
                            );
                            builderInner.setTitle("Your order");
                            builderInner.setPositiveButton(
                                    "Close Order",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(
                                                    MainActivity.this);
                                            areYouSureDialog.setMessage("Are you sure you want to close the order?\n" +
                                                    "This will finalize the order and charge your card.");
                                            areYouSureDialog.setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            areYouSureDialog.setPositiveButton(
                                                    "Close Order",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            dialog.dismiss();
                                                            try {
                                                                closeOrder(lJsonObject.getString("id"));
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                            areYouSureDialog.show();
//                                            dialog.dismiss();
                                        }
                                    });
                            builderInner.setNegativeButton(
                                    "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builderInner.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builderSingle.show();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sample);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString("username", "");

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setIndeterminate(true);


        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip
                .setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
                    @Override
                    public void onPageStripSelected(int position) {
                        position = Math.min(mPagerAdapter.getCount() - 1,
                                position);
                        if (mPager.getCurrentItem() != position) {
                            mPager.setCurrentItem(position);
                        }
                    }
                });

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);


        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    DialogFragment dg = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            return new AlertDialog.Builder(getActivity())
                                    .setMessage(R.string.submit_confirm_message)
                                    .setPositiveButton(
                                            R.string.submit_confirm_button,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                    //To check if the user has a method of payment
                                                    String uri = String.format("https://uhungry-valyriacorp.c9.io/customers/getpaymentmethod/?username=%1$s",
                                                            username);
                                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    if(response.equals("8000"))
                                                                    {
                                                                        Toast.makeText(MainActivity.this, "Placing your Order", Toast.LENGTH_SHORT).show();
                                                                        placeOrder();
                                                                    }else if(response.equals("8001"))
                                                                    {
                                                                        Toast.makeText(MainActivity.this, "No Payment Method Available. Please add one.", Toast.LENGTH_SHORT).show();
                                                                        setupDropInUi();
                                                                    }else{
                                                                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    // Handle error
                                                                    Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                    // Add the request to the RequestQueue.
                                                    HttpSingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
                                                }
                                            }
                                    )
                                    .setNegativeButton(android.R.string.cancel,
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    Toast.makeText(MainActivity.this, "Order cancelled", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    ).create();
                        }
                    };
                    dg.show(getSupportFragmentManager(), "place_order_dialog");
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }


    private void setupDropInUi(){
        String uri = String.format("https://uhungry-valyriacorp.c9.io/payment/client_token/?username=%1$s",
                username);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0001"))
                        {
                            Toast.makeText(MainActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                        }else{
                            String client_token = response;
                            Intent intent = new Intent(MainActivity.this, BraintreePaymentActivity.class);
                            intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, client_token);
                            startActivityForResult(intent, REQUEST_DROPINUI);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DROPINUI) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    String paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                    addPaymentMethod(paymentMethodNonce);
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    Throwable lThrowable = (Throwable) data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE);
                    if(lThrowable != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(lThrowable.getMessage())
                                .setTitle("Error")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do nothing
                                        // MAYBE CLOSE THE APP OR REPORT PROBLEM TO uHungry.
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    break;
                default:
                    break;
            }

        }
    }

    private void addPaymentMethod(final String aInPaymentMethodNonce)
    {
        final ProgressDialog PaymentMethodProgressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Light_Dialog);
        PaymentMethodProgressDialog.setIndeterminate(true);
        PaymentMethodProgressDialog.setMessage("Adding payment method to your profile ...");
        PaymentMethodProgressDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/customers/addpaymentmethod/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                PaymentMethodProgressDialog.dismiss();
                if(response.equals("6000"))
                {
                    //adding payment method successfull
                    Toast.makeText(getBaseContext(), "Payment Method has been added to your profile", Toast.LENGTH_LONG).show();
                    mProgressDialog.setMessage("Placing Order...");
                    mProgressDialog.show();
                    placeOrder();

                }else
                {
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PaymentMethodProgressDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("nonce_from_the_client",aInPaymentMethodNonce);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void closeOrder(final String OrderId)
    {
        final ProgressDialog closingOrderDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Light_Dialog);
        closingOrderDialog.setIndeterminate(true);
        closingOrderDialog.setMessage("Closing order ...");
        closingOrderDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/customers/closeorder/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                closingOrderDialog.dismiss();
                if(response.equals("7000"))
                {
                    //closing order successfull
                    Toast.makeText(getBaseContext(), "Your order has been finalized", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closingOrderDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("orderid",OrderId);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void placeOrder()
    {
        mProgressDialog.setMessage("Placing Order...");
        mProgressDialog.show();
        lCurrentOrder = "";
        for(ReviewItem lItem: reviewFragment.getReviewItems())
        {
            lCurrentOrder = lCurrentOrder + lItem.getDisplayValue();
            lCurrentOrder = lCurrentOrder +"\n";
        }


        lTotalPrice = 0.0;
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
        if(lTotalPrice > 0 && lTotalPrice < 10)
        {
            lTotalPrice = lTotalPrice + 2;
        }
        if(lTotalPrice > 10)
        {
            lTotalPrice = lTotalPrice + 4;
        }
        lTotalPrice = lTotalPrice * 1.13;

        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/customers/createorder/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgressDialog.dismiss();
                if(response.equals("5000"))
                {
                    Toast.makeText(getBaseContext(), "Success! Sit tight while we bring you your order!", Toast.LENGTH_LONG).show();
                }else{
                    if(response.equals("5001"))
                    {
                        Toast.makeText(getBaseContext(), "User not registered", Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgressDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("location", OrderCache.getInstance().getSelectedLocation());
                params.put("orderdetails", lCurrentOrder);
                DecimalFormat df = new DecimalFormat("#.00");
                params.put("totalprice", df.format(lTotalPrice));
                return params;
            }
        };


        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    public void onBackPressed() {
        if(mPager.getCurrentItem() == 0)
        {
            moveTaskToBack(true);
        }else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 =
        // review
        // step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
//            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview ? R.string.review
                    : R.string.next);
            mNextButton
                    .setBackgroundResource(R.drawable.selectable_item_background);
//            TypedValue v = new TypedValue();
//            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v,
//                    true);
//            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton
                .setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                reviewFragment = new ReviewFragment();
                return reviewFragment;
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return Math.min(mCutOffPage + 1, mCurrentPageSequence == null ? 1
                    : mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }
}
