package com.valyria.uhungrypartner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.wizardpager.HttpSingleton;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SmsActivationActivity extends AppCompatActivity {
    private static final String TAG = "SmsActivationActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_MAINAPP = 10;
    private String username;
    @InjectView(R.id.input_phone) EditText _phoneText;
    @InjectView(R.id.input_code) EditText _codeText;
    @InjectView(R.id.btn_activate) Button _activateButton;
    @InjectView(R.id.link_resendcode) TextView _resendLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_activation);
        ButterKnife.inject(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = prefs.getString("username", "");
//
        _activateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activate();
            }
        });

        _resendLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
//                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                startActivityForResult(intent, REQUEST_SIGNUP);
                resendActivationCode();
            }
        });


    }

    public void activate()
    {
        Log.d(TAG, "Activate");

        if (!validate()) {
            onActivationFailed();
            return;
        }

        _activateButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SmsActivationActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Activating...");
        progressDialog.show();

        final String phone = _phoneText.getText().toString();
        final String code = _codeText.getText().toString();


        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/customers/checkcode/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.equals("9000"))
                {
                    Toast.makeText(getBaseContext(), "Your Account has been activated! You can make orders now!!", Toast.LENGTH_LONG).show();
                    onActivationSuccess();
                }else{
                    if(response.equals("9001"))
                    {
                        onActivationFailed();
                        Toast.makeText(getBaseContext(), "codes do not match! number not verified", Toast.LENGTH_LONG).show();
                    }else if(response.equals("9002"))
                    {
                        onActivationFailed();
                        Toast.makeText(getBaseContext(), "number not found!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("phonenumber", phone);
                params.put("code", code);
                return params;
            }
        };


        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void resendActivationCode()
    {
        String phone = _phoneText.getText().toString();
        String uri = String.format("https://uhungry-valyriacorp.c9.io/customers/verify/?phonenumber=%1$s",
                phone);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("9000"))
                        {
                            Toast.makeText(SmsActivationActivity.this, "Verification Initiated! wait for sms.", Toast.LENGTH_SHORT).show();
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
        HttpSingleton.getInstance(SmsActivationActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onActivationSuccess() {
        _activateButton.setEnabled(true);
        setResult(RESULT_OK);
        finish();


//        Intent intent = getIntent();
//        setResult(RESULT_OK, intent);
//        finish();
    }

    public void onActivationFailed() {
        Toast.makeText(getBaseContext(), "Activation failed", Toast.LENGTH_LONG).show();

        _activateButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _phoneText.getText().toString();
        String code = _codeText.getText().toString();

        if (phone.isEmpty() || !phone.startsWith("+")) {
            _phoneText.setError("Enter Code with + and country code");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (code.isEmpty() || code.length() == 4) {
            _codeText.setError("Please enter the activation code");
            valid = false;
        } else {
            _codeText.setError(null);
        }

        return valid;
    }
}
