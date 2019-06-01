package com.example.mcas;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;

public class Assistance extends AppCompatActivity {

    CardView tow,puncture,refuel,vigilance,medical,other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);

        tow=(CardView)findViewById(R.id.tow);
        puncture=(CardView)findViewById(R.id.puncture);
        refuel=(CardView)findViewById(R.id.refuel);
        vigilance=(CardView)findViewById(R.id.vigilance);
        medical=(CardView)findViewById(R.id.medical);
        other=(CardView)findViewById(R.id.other);

        tow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8428953969");
            }
        });
        puncture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8428953969");
            }
        });
        refuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8428953969");
            }
        });
        vigilance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8428953969");
            }
        });
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8428953969");
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("8428953969");
            }
        });


    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}
