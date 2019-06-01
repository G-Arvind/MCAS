package com.example.mcas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Drive extends AppCompatActivity implements IBaseGpsListener{

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    Button drivestop;

    static GifImageView img;
    static GifImageView img1;
    static TextView drivemsg;

    private SensorManager sensorManager;
    private Sensor gyroSensor, accSensor;
    private boolean isGyro,isAccle;

    float[][] data = new float[51][3];
    int data_length = 0;

    float[][] dataGyro = new float[120][3];
    int dataGyro_length = 0;


    public static IResult mResultCallback = null;
    public static VolleyService mVolleyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        drivestop=(Button)findViewById(R.id.drivestop);
        img=(GifImageView)findViewById(R.id.img);
        img1=(GifImageView)findViewById(R.id.img1);
        drivemsg=(TextView)findViewById(R.id.drivemsg);

        drivestop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Drive.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor     = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor    = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(accSensor != null) {
            isAccle = true;
        } else {
            Toast.makeText(getApplicationContext(),"AcceleratorMeter is Not Available",Toast.LENGTH_SHORT).show();
        }

        if(gyroSensor !=null){
            isGyro = true;
        } else {
            Toast.makeText(getApplicationContext(),"GyroScopeMeter is Not Available",Toast.LENGTH_SHORT).show();
        }

        startSensorActivity();



        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);

//        CheckBox chkUseMetricUntis = (CheckBox) this.findViewById(R.id.chkMetricUnits);
//        chkUseMetricUntis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // TODO Auto-generated method stub
//                Drive.this.updateSpeed(null);
//            }
//        });


    }



    public void finish()
    {
        super.finish();
        System.exit(0);
    }

    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if(location != null)
        {
            location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "kms/hour";
        if(this.useMetricUnits())
        {
            strUnits = "meters/second";
        }

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
    }

    private boolean useMetricUnits() {
        // TODO Auto-generated method stub
       // CheckBox chkUseMetricUnits = (CheckBox) this.findViewById(R.id.chkMetricUnits);
       // return chkUseMetricUnits.isChecked();
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            CLocation myLocation = new CLocation(location, this.useMetricUnits());
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }

    public void startSensorActivity() {
        if (isAccle) {
                sensorManager.registerListener(accelerometerSensorListener, accSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        if(isGyro) {
                sensorManager.registerListener(gyroMeterListener,gyroSensor,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void stopSensorActivity() {
        if (isAccle) {
            sensorManager.unregisterListener(accelerometerSensorListener);
        }
        if(isGyro) {
            sensorManager.unregisterListener(gyroMeterListener);
        }
    }
    SensorEventListener accelerometerSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.

            final float alpha = (float) 0.8;
            float gravity[] = new float[4];
            float linear_acceleration[] = new float[4];

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
            data[data_length][0] = linear_acceleration[0];
            data[data_length][1] = linear_acceleration[1];
            data[data_length][2] = linear_acceleration[2];
            data_length++;
            if(data_length == 50) {
                try {
                    sendData();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                data_length = 0;
            }
            //            Log.d(TAG, event.timestamp +linear_acceleration[0]+","+linear_acceleration[1]+","+linear_acceleration[2]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    SensorEventListener gyroMeterListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // Axis of the rotation sample, not normalized yet.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            dataGyro[dataGyro_length][0] = axisX;
            dataGyro[dataGyro_length][1] = axisY;
            dataGyro[dataGyro_length][2] = axisZ;
            dataGyro_length++;
            if(dataGyro_length == 120) {
                try {
                    //sendGyroData();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                dataGyro_length = 0;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    public  void sendSMSMessage() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("919220592205", null, "7MQH4 id:1 lat:13.0067 lng:80.2206", null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
    public void sendData() throws JSONException {
        //define callback functions
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallback,getApplicationContext());
        JSONObject params = new JSONObject();
        JSONArray outerJsonArray = new JSONArray();

        for (int i=0; i < 50; i++) {
            JSONArray innerJsonArray = new JSONArray();

            for (int j=0; j < data[i].length; j++) innerJsonArray.put(data[i][j]);

            outerJsonArray.put(innerJsonArray);
        }
        params.put("data",outerJsonArray);
        mVolleyService.postDataVolley("post","http://192.168.100.200:5000/predict",params);
    }

    //used by login
    public    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) throws JSONException {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + response);
                if(response.getString("isHit").equals("yes")){
                    drivemsg.setText("Are You Fine?");
                    img.setVisibility(View.INVISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    sendSMSMessage();
                    sendReport();
                }
            }
            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
            }
        };
    }


    public void sendGyroData() throws JSONException {
        //define callback functions
        initVolleyCallbackGyro();
        mVolleyService = new VolleyService(mResultCallback,getApplicationContext());
        JSONObject params = new JSONObject();
        JSONArray outerJsonArray = new JSONArray();

        for (int i=0; i < dataGyro.length; i++) {
            JSONArray innerJsonArray = new JSONArray();

            for (int j=0; j < dataGyro[i].length; j++) innerJsonArray.put(data[i][j]);

            outerJsonArray.put(innerJsonArray);
        }
        params.put("data",outerJsonArray);
        mVolleyService.postDataVolley("post",AppConfig.URL_PREDICT,params);
    }

    //used by login
    public  static void initVolleyCallbackGyro(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + response);
            }
            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
            }
        };
    }

    public  void sendReport() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REPORT, new Response.Listener<String>() {

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


                        Toast.makeText(getApplicationContext(), "Alerted!", Toast.LENGTH_SHORT).show();

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
                params.put("user_id", "1");
                params.put("lat", "13.00067");
                params.put("lng", "80.2206");
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


}
