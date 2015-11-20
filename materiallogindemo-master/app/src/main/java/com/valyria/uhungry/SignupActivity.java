package com.valyria.uhungry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.example.android.wizardpager.MainActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.first_input_name) EditText _firstnameText;
    @InjectView(R.id.last_input_name) EditText _lastnameText;
    @InjectView(R.id.phone_input_name) EditText _phoneText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        TextView textView =(TextView)findViewById(R.id.textView1);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "By clicking Sign Up, you agree to our <a href='http://uhungryottawa.appspot.com/terms'>Terms of Use</a> and our <a href='http://uhungryottawa.appspot.com/privacy'>Privacy Policy</a>";
        textView.setText(Html.fromHtml(text));

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String firstname = _firstnameText.getText().toString();
        final String lastname = _lastnameText.getText().toString();
        final String phone = _phoneText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // signup logic here.

        final StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://uhungry-valyriacorp.c9.io/customers/signup/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.equals("2000"))
                {
                    Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    prefs.edit().putString("username", email).commit(); // email is a string
                    onSignupSuccess();

                    //
                }else if(response.equals("2001"))
                {
                    Toast.makeText(getBaseContext(), "Username already in use", Toast.LENGTH_LONG).show();
                    _emailText.setError("Username already in use");
                    onSignupFailed();
                }else if(response.equals("2002"))
                {
                    Toast.makeText(getBaseContext(), "Not a valid email address", Toast.LENGTH_LONG).show();
                    _emailText.setError("Not a valid email address");
                    onSignupFailed();
                }else if(response.equals("2003"))
                {
                    Toast.makeText(getBaseContext(), "This email already in use", Toast.LENGTH_LONG).show();
                    _emailText.setError("This email already in use");
                    onSignupFailed();
                }
                else if(response.equals("2004"))
                {
                    Toast.makeText(getBaseContext(), "Registration failed with payment server", Toast.LENGTH_LONG).show();
                    _emailText.setError("Registration failed with payment server");
                    onSignupFailed();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                mPostCommentResponse.requestEndedWithError(error.toString());
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",email);
                params.put("email",email);
                params.put("password", password);
                params.put("firstname",firstname);
                params.put("lastname",lastname);
                params.put("phonenumber",phone);

                return params;
            }
        };


        // Add the request to the RequestQueue.
        HttpSingleton.getInstance(this).addToRequestQueue(stringRequest);


        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();

//        finish();
    }

    public void terms(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://http://uhungryottawa.appspot.com/terms"));
        startActivity(intent);
    }
    public void privacy(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://http://uhungryottawa.appspot.com/privacy"));
        startActivity(intent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstname = _firstnameText.getText().toString();
        String lastname = _lastnameText.getText().toString();
        String phonenumber = _phoneText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (firstname.isEmpty() || firstname.length() < 3) {
            _firstnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstnameText.setError(null);
        }

        if (lastname.isEmpty() || lastname.length() < 3) {
            _lastnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastnameText.setError(null);
        }

        if (phonenumber.isEmpty() || phonenumber.length() < 10) {
            _phoneText.setError("at least 10 digits");
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}