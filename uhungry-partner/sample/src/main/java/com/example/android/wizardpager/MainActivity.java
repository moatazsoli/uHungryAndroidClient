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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Movie;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import java.security.Key;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        {


    private static final int REQUEST_DROPINUI = 100;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String username;
    private ProgressDialog mProgressDialog;


    String uri = "";
    private ProgressDialog pDialog;
    private List<Order> orders = new ArrayList<Order>();
    private ListView listView;
    private CustomListAdapter adapter;
    private HashMap<Integer,String> lMap = new HashMap<Integer,String>();


    private void addDrawerItems() {
        final String[] osArray = {"Open Orders", "Accepted Orders", "History", "Terms of Condition", "Privacy Policy","Reset",
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

                    }else if(lCurrentListItem.equals("Open Orders"))
                    {
                        setupListForOpenOrders();
                        getOpenListOfOrders();

                    }else if(lCurrentListItem.equals("Accepted Orders"))
                    {
                        setupListForAcceptedOrders();
                        getAcceptedListOfOrders();

                    }else if(lCurrentListItem.equals("History"))
                    {
                        String uri = String.format("https://uhungry-valyriacorp.c9.io/customers/getclosedorders/?username=%1$s",
                        username);

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if(response.length() != 0)
                                        {
//                                            buildClosedOrdersList(response);
                                        }else{
                                            Toast.makeText(getBaseContext(), "No closed orders available", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        HttpSingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);
                    }else if(lCurrentListItem.equals("Reset")) {
//                        resetEverything();
                    }else{
                        Toast.makeText(MainActivity.this, "Selected item: " + lCurrentListItem, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

        if(!username.equals(""))
        {
            uri = String.format("https://uhungry-valyriacorp.c9.io/partners/getdeliveryorders/?username=%1$s",
                    username);
        }

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setIndeterminate(true);


        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, orders);
        listView.setAdapter(adapter);
        setupListForOpenOrders();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position,
//                                    long id) {
//                    final Order lOrder = orders.get(position);
//                    AlertDialog.Builder builderInner = new AlertDialog.Builder(
//                            MainActivity.this);
//                    builderInner.setMessage(
//                            "OrderID : "+ lOrder.getId()+"\n"+
//                                    "Date    : "+ lOrder.getDate()+"\n"+
//                                    "Location: "+ lOrder.getLocation()+"\n"+
//                                    "Status  : "+ lOrder.getStatus()+"\n"+
//                                    "Price   : "+ lOrder.getTotalprice()+"\n"+
//                                    "Details :\n"+ lOrder.getOrderdetails()+"\n"
//                    );
//
//                    builderInner.setTitle("Order");
//                    builderInner.setPositiveButton(
//                            "Accept Order",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(
//                                        DialogInterface dialog,
//                                        int which) {
//                                    AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(
//                                            MainActivity.this);
//                                    areYouSureDialog.setMessage("Are you sure you want to accept the order?\n");
//                                    areYouSureDialog.setNegativeButton(
//                                            "Cancel",
//                                            new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    dialog.dismiss();
//                                                }
//                                            });
//                                    areYouSureDialog.setPositiveButton(
//                                            "Accept Order",
//                                            new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(
//                                                        DialogInterface dialog,
//                                                        int which) {
//                                                    dialog.dismiss();
//                                                    acceptOrder(lOrder.getId());
//                                                }
//                                            });
//                                    areYouSureDialog.show();
////                                            dialog.dismiss();
//                                }
//                            });
//                    builderInner.setNegativeButton(
//                            "Cancel",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builderInner.show();
//
//                Toast.makeText(getApplicationContext(), "Order Details", Toast.LENGTH_SHORT).show();
//                Log.v("Order Details", "showing order details!");
//            }
//        });

        pDialog = new ProgressDialog(this);
        getOpenListOfOrders();

    }

    public void setupListForAcceptedOrders()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                final Order lOrder = orders.get(position);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(
                        MainActivity.this);
                builderInner.setMessage(
                        "OrderID : "+ lOrder.getId()+"\n"+
                                "Date    : "+ lOrder.getDate()+"\n"+
                                "Location: "+ lOrder.getLocation()+"\n"+
                                "Status  : "+ lOrder.getStatus()+"\n"+
                                "Price   : "+ lOrder.getTotalprice()+"\n"+
                                "Details :\n"+ lOrder.getOrderdetails()+"\n"
                );

                builderInner.setTitle("Order");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                builderInner.setView(input);
                builderInner.setPositiveButton(
                        "Order Arrived",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(
                                        MainActivity.this);
                                areYouSureDialog.setMessage("Are you sure you want to finalize the order on your side?\n");
                                areYouSureDialog.setNegativeButton(
                                        "Cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                areYouSureDialog.setPositiveButton(
                                        "Order Arrived",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                String msg = input.getText().toString();
                                                dialog.dismiss();
                                                orderArrived(lOrder.getId(), msg);
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

                Toast.makeText(getApplicationContext(), "Order Details", Toast.LENGTH_SHORT).show();
                Log.v("Order Details", "showing order details!");
            }
        });
    }

    public void orderArrived(final String aInOrderId, final String aInMsg)
    {
        final ProgressDialog acceptingOrderDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Light_Dialog);
        acceptingOrderDialog.setIndeterminate(true);
        acceptingOrderDialog.setMessage("sending msg ...");
        acceptingOrderDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/customers/orderarrived/", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                acceptingOrderDialog.dismiss();
                if(response.equals("7000"))
                {
                    //accept order successfull
                    Toast.makeText(getBaseContext(), "Msg Sent", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                acceptingOrderDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("locationdetails",aInMsg);
                params.put("orderid", aInOrderId);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    public void setupListForOpenOrders()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {
                final Order lOrder = orders.get(position);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(
                        MainActivity.this);
                builderInner.setMessage(
                        "OrderID : "+ lOrder.getId()+"\n"+
                                "Date    : "+ lOrder.getDate()+"\n"+
                                "Location: "+ lOrder.getLocation()+"\n"+
                                "Status  : "+ lOrder.getStatus()+"\n"+
                                "Price   : "+ lOrder.getTotalprice()+"\n"+
                                "Details :\n"+ lOrder.getOrderdetails()+"\n"
                );

                builderInner.setTitle("Order");
                builderInner.setPositiveButton(
                        "Accept Order",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                AlertDialog.Builder areYouSureDialog = new AlertDialog.Builder(
                                        MainActivity.this);
                                areYouSureDialog.setMessage("Are you sure you want to accept the order?\n");
                                areYouSureDialog.setNegativeButton(
                                        "Cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                areYouSureDialog.setPositiveButton(
                                        "Accept Order",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                dialog.dismiss();
                                                acceptOrder(lOrder.getId());
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

                Toast.makeText(getApplicationContext(), "Order Details", Toast.LENGTH_SHORT).show();
                Log.v("Order Details", "showing order details!");
            }
        });
    }

    public void getOpenListOfOrders()
    {
        orders.clear();
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        uri = String.format("https://uhungry-valyriacorp.c9.io/partners/getdeliveryorders/?username=%1$s",
                username);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        if(response.length() != 0)
                        {
                            processJsonOfOrders(response);
                        }else{
                            Toast.makeText(getBaseContext(), "No orders available", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        HttpSingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);
    }


    public void getAcceptedListOfOrders()
    {
        orders.clear();
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        uri = String.format("https://uhungry-valyriacorp.c9.io/partners/getacceptedrorders/?username=%1$s",
                username);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        if(response.length() != 0)
                        {
                            processJsonOfOrders(response);
                        }else{
                            Toast.makeText(getBaseContext(), "No orders available", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        HttpSingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);
    }

    public void processJsonOfOrders(JSONObject aInOpenOrdersJson)
    {
        lMap.clear();

        Iterator lIterator = aInOpenOrdersJson.keys();
        int counter = 0;
        while(lIterator.hasNext())
        {
            String key = (String) lIterator.next();
            try {
                JSONObject lJsonObject = (JSONObject) aInOpenOrdersJson.getJSONObject(key);
                Order lOrder = new Order(lJsonObject.getString("id"),
                        lJsonObject.getString("date"),
                        lJsonObject.getString("location"),
                        lJsonObject.getString("orderdetails"),
                        lJsonObject.getString("status"),
                        lJsonObject.getString("totalprice"));
                orders.add(lOrder);
                lMap.put(counter, key);
                counter++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void acceptOrder(final String aInOrderId)
    {
        final ProgressDialog acceptingOrderDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Light_Dialog);
        acceptingOrderDialog.setIndeterminate(true);
        acceptingOrderDialog.setMessage("Accepting order ...");
        acceptingOrderDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/partners/orderaccepted/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                acceptingOrderDialog.dismiss();
                if(response.equals("7000"))
                {
                    //accept order successfull
                    Toast.makeText(getBaseContext(), "Order Accepted", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                acceptingOrderDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("orderid", aInOrderId);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);
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
//                    addPaymentMethod(paymentMethodNonce);
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

}
