package com.example.mcas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView signup;


    SharedPreferences pref;

    SharedPreferences.Editor editor;

    String spval;


    String tag_string_req = "req_login_fetch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String loggedIn;

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        loggedIn = sharedPref.getString("user_id", "no");

        if (loggedIn!="no")
        {
            startActivity(new Intent(this,Dashboard.class));
            finish();
        }

        signup=(TextView)findViewById(R.id.signup);
        emailEditText = (EditText)findViewById(R.id.email);
        passwordEditText = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(emailEditText.getText().toString())){
                    emailEditText.setError("number required");
                    // Toast.makeText(getContext(), "insufficient credentials!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((emailEditText.getText().toString().length()!=10)){
                    emailEditText.setError("Invalid Mobile number");
                    //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( TextUtils.isEmpty(passwordEditText.getText().toString())){
                    passwordEditText.setError("password required");
                    //Toast.makeText(getContext(), "insufficient credentials!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                checkLogin(email, password);


            }
        });

    }

    private void checkLogin(final String phoneNumber, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());

                //response is a html page
                //if success move to main activity of the app
                try {
                    //create a login session
                    // session.setLogin(true);
                    String res = response.toString();

                    Log.e("TAG",res);

                    if(response.contains("user_id")){

                        JSONObject obj=new JSONObject();

                        try {

                             obj = new JSONObject(response);

                            Log.d("My App", obj.getString("user_id"));

                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }

                        //     SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("user_id",obj.getString("user_id") );
                        editor.apply();

//                        editor.putString("mobilenum",phoneNumber );
//                        editor.commit();


                        //  MobileNumber.userMobileNumber=phoneNumber;
                        Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this,Dashboard.class);
                        startActivity(intent);
                        finish();


                        emailEditText.setText("");
                        passwordEditText.setText("");

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Invalid details", Toast.LENGTH_SHORT).show();
                        //  MobileNumber.userMobileNumber=phoneNumber;
                        // Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();


                        //  loginpage.setVisibility(View.GONE);
                        //   profilepage.setVisibility(View.VISIBLE);
                        //   scrollView.setVisibility(View.GONE);
                        //  emailEditText.setText("");
                        //passwordEditText.setText("");

                        //scrollView.setVisibility(View.GONE);
                        //  emailEditText.setText("");
                        //  passwordEditText.setText("");
                        //  setdetails(MobileNumber.userMobileNumber);
                    }


                } catch (Exception e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", phoneNumber);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }





}
