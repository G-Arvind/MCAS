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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText uid, passEditText, nameEditText,phoneEditText,repass,guardianName,guardianNumber,dob,bloodGroup,medConditions;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String loggedIn;

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        loggedIn = sharedPref.getString("user_id", "no");


        uid = (EditText)findViewById(R.id.uid);
        passEditText= (EditText)findViewById(R.id.passEditText);
        nameEditText= (EditText)findViewById(R.id.nameEditText);
        phoneEditText= (EditText)findViewById(R.id.phoneEditText);
        repass= (EditText)findViewById(R.id.repass);
        guardianName= (EditText)findViewById(R.id.guardianName);
        guardianNumber= (EditText)findViewById(R.id.guardianNumber);
        dob= (EditText)findViewById(R.id.dob);
        bloodGroup= (EditText)findViewById(R.id.bloodGroup);
        medConditions= (EditText)findViewById(R.id.medConditions);
        registerButton = (Button)findViewById(R.id.registerButton);

        if (loggedIn!="no")
        {
            registerButton.setText("Update Details");
            autofill(loggedIn);
        }

        else {

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                        nameEditText.setError("name required");
                        return;
                    }
                    if (TextUtils.isEmpty(uid.getText().toString())) {
                        uid.setError("UID required");
                        return;
                    }
                    if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
                        phoneEditText.setError("number required");
                        //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ((phoneEditText.getText().toString().length() != 10)) {
                        phoneEditText.setError("Invalid Mobile number");
                        //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(passEditText.getText().toString())) {
                        passEditText.setError("password required");
                        //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (!(passEditText.getText().toString().equals(repass.getText().toString()))) {
                        Toast.makeText(getApplicationContext(), "password doesn't match", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(guardianName.getText().toString())) {
                        guardianName.setError("guardian name required");
                        //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(guardianNumber.getText().toString())) {
                        guardianNumber.setError("guardian number required");
                        //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    String name = nameEditText.getText().toString().trim();
                    String password = passEditText.getText().toString().trim();
                    String phoneNumber = phoneEditText.getText().toString().trim();
                    String guardianNameval = guardianName.getText().toString().trim();
                    String guardianNumval = guardianNumber.getText().toString().trim();
                    String blood = bloodGroup.getText().toString().trim();
                    String medCond = medConditions.getText().toString().trim();
                    String dobval = dob.getText().toString().trim();
                    String uidval = uid.getText().toString().trim();

                    registerUser(name, password, phoneNumber, guardianNameval, guardianNumval, blood, medCond, dobval, uidval);
                }
            });
        }
    }

    private void registerUser(final String fname, final String password, final String phoneNumber, final String guardianNameval, final String guardianNumval, final String blood, final String medCond, final String dobval, final String uidval) {



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());


                try {
                    //JSONObject jObj = new JSONObject(response);
                    //boolean error = jObj.getBoolean("error");
                    //if (!error) {
                    // User successfully stored in MySQL
                    // Now store the user in sqlite
                    //  String uid = jObj.getString("uid");

                    //  JSONObject user = jObj.getJSONObject("user");
                    //String name = user.getString("name");
                    //String email = user.getString("email");
                    //String created_at = user
                    //      .getString("created_at");

                    Toast.makeText(getApplicationContext(), "User successfully registered. Login now!", Toast.LENGTH_LONG).show();

                    nameEditText.setText("");
                    phoneEditText.setText("");
                    passEditText.setText("");
                    repass.setText("");

                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("TAG","INTENT CHECK");
                    //    Log.d("TAG",MobileNumber.userMobileNumber);
                  //  intent.putExtra("position", 2);
                    startActivity(intent);
                    finish();

                    // Launch login activity
                    /*Intent intent = new Intent(
                            Register.this,
                            Login.class);
                    startActivity(intent);
                    finish();*/
                    // } else {

                    // Error occurred in registration. Get the error
                    // message
                    //   String errorMsg = jObj.getString("error_msg");
                    // Toast.makeText(getApplicationContext(),
                    //       errorMsg, Toast.LENGTH_LONG).show();
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", fname);
                params.put("mobile", phoneNumber);
                params.put("password", password);
                params.put("uid",uidval);
                params.put("gender","male");
                params.put("dob",dobval);
                params.put("address","Chennai");
                params.put("blood_group",blood);
                params.put("notable_medical_condtions",medCond);
                params.put("guardian_name",guardianNameval);
                params.put("guardian_mobile",guardianNumval);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    public void autofill(final String user_id){

        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>() {

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


                         nameEditText.setText(obj.getString("name"));
                         phoneEditText.setText(obj.getString("mobile"));
                         guardianName.setText(obj.getString("guardian_name"));
                         guardianNumber.setText(obj.getString("guardian_mobile"));
                         bloodGroup.setText(obj.getString("blood_group"));
                         medConditions.setText(obj.getString("notable_medical_condtions"));
                         dob.setText(obj.getString("dob"));
                         uid.setText(obj.getString("uid"));





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
                params.put("user_id", user_id);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(nameEditText.getText().toString())) {
                    nameEditText.setError("name required");
                    return;
                }
                if (TextUtils.isEmpty(uid.getText().toString())) {
                    uid.setError("UID required");
                    return;
                }
                if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
                    phoneEditText.setError("number required");
                    //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((phoneEditText.getText().toString().length() != 10)) {
                    phoneEditText.setError("Invalid Mobile number");
                    //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passEditText.getText().toString())) {
                    passEditText.setError("password required");
                    //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!(passEditText.getText().toString().equals(repass.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "password doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(guardianName.getText().toString())) {
                    guardianName.setError("guardian name required");
                    //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(guardianNumber.getText().toString())) {
                    guardianNumber.setError("guardian number required");
                    //Toast.makeText(getApplicationContext(), "insufficient credentials", Toast.LENGTH_SHORT).show();
                    return;
                }


                String name = nameEditText.getText().toString().trim();
                String password = passEditText.getText().toString().trim();
                String phoneNumber = phoneEditText.getText().toString().trim();
                String guardianNameval = guardianName.getText().toString().trim();
                String guardianNumval = guardianNumber.getText().toString().trim();
                String blood = bloodGroup.getText().toString().trim();
                String medCond = medConditions.getText().toString().trim();
                String dobval = dob.getText().toString().trim();
                String uidval = uid.getText().toString().trim();

                updateUser(name, password, phoneNumber, guardianNameval, guardianNumval, blood, medCond, dobval, uidval);

            }
        });
    }

    private void updateUser(final String fname, final String password, final String phoneNumber, final String guardianNameval, final String guardianNumval, final String blood, final String medCond, final String dobval, final String uidval) {

        registerButton.setText("Update Details");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_USER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());


                try {
                    //JSONObject jObj = new JSONObject(response);
                    //boolean error = jObj.getBoolean("error");
                    //if (!error) {
                    // User successfully stored in MySQL
                    // Now store the user in sqlite
                    //  String uid = jObj.getString("uid");

                    //  JSONObject user = jObj.getJSONObject("user");
                    //String name = user.getString("name");
                    //String email = user.getString("email");
                    //String created_at = user
                    //      .getString("created_at");

                    Toast.makeText(getApplicationContext(), "User Details successfully Updated", Toast.LENGTH_LONG).show();

                    nameEditText.setText("");
                    phoneEditText.setText("");
                    passEditText.setText("");
                    repass.setText("");

                    Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("TAG","INTENT CHECK");
                    //    Log.d("TAG",MobileNumber.userMobileNumber);
                    //  intent.putExtra("position", 2);
                    startActivity(intent);
                    finish();

                    // Launch login activity
                    /*Intent intent = new Intent(
                            Register.this,
                            Login.class);
                    startActivity(intent);
                    finish();*/
                    // } else {

                    // Error occurred in registration. Get the error
                    // message
                    //   String errorMsg = jObj.getString("error_msg");
                    // Toast.makeText(getApplicationContext(),
                    //       errorMsg, Toast.LENGTH_LONG).show();
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", fname);
                params.put("mobile", phoneNumber);
                params.put("password", password);
                params.put("uid",uidval);
                params.put("gender","male");
                params.put("dob",dobval);
                params.put("address","Chennai");
                params.put("blood_group",blood);
                params.put("notable_medical_condtions",medCond);
                params.put("guardian_name",guardianNameval);
                params.put("guardian_mobile",guardianNumval);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

}

