package com.valyria.uhungry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.android.wizardpager.HttpSingleton;


public class MainActivity extends AppCompatActivity {

    private  final static int LOGIN_ACITIVITY_CODE = 0x12;
    private  final static int REQUEST_MAINAPP_CODE = 0x13;
    private final static boolean DEBUG = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Parse.initialize(this, "dDEIBkzplVgMdT8CY4JWQLJrUGlFZUyKRtLsLJgC", "nYu5mIT3HDW34URw1FEk3ED9iWwkXWjALBk4x1yh");
//        ParseInstallation.getCurrentInstallation().saveInBackground();
        RequestQueue queue = HttpSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        if(DEBUG)
        {
            Intent intent = new Intent(this, com.example.android.wizardpager.MainActivity.class);
            startActivity(intent);
        }else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String username = prefs.getString("username", "");
            if(!username.equals(""))
            {
                Intent intent = new Intent(this, com.example.android.wizardpager.MainActivity.class);
                startActivityForResult(intent, REQUEST_MAINAPP_CODE);
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACITIVITY_CODE);
            }
//            startActivityForResult(intent, LOGIN_ACITIVITY_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_ACITIVITY_CODE) {
            if(resultCode == RESULT_OK){
                Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, com.example.android.wizardpager.MainActivity.class);
                startActivityForResult(intent,REQUEST_MAINAPP_CODE);
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == REQUEST_MAINAPP_CODE)
        {
            if(resultCode == RESULT_OK){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACITIVITY_CODE);
            }
        }
    }//onActivityResult
}
