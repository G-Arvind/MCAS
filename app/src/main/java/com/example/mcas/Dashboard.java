package com.example.mcas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Dashboard extends AppCompatActivity {

    CardView editProfile,assistance,accidentProne,ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        editProfile=(CardView) findViewById(R.id.editProfile);
        assistance=(CardView)findViewById(R.id.assistance);
        accidentProne=(CardView)findViewById(R.id.accidentProne);
        ride=(CardView)findViewById(R.id.ride);

        assistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,Assistance.class);
                startActivity(intent);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,Register.class);
                startActivity(intent);
            }
        });
        accidentProne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,AccidentProne.class);
                startActivity(intent);
            }
        });
        ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,Drive.class);
                startActivity(intent);
            }
        });
    }
}
