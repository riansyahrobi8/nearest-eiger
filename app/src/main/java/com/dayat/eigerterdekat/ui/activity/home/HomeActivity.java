package com.dayat.eigerterdekat.ui.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dayat.eigerterdekat.R;
import com.dayat.eigerterdekat.ui.activity.map.MapActivity;

public class HomeActivity extends AppCompatActivity {

    // konteks yang dipakai
    private Context context;

    private Toolbar toolbar;

    private CardView menuMap,menuFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    private void initWidget() {
        this.context = this;

        // inisialisasi toolbar
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        menuMap = findViewById(R.id.menu_map_cardview);
        menuMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MapActivity.class));
                finish();
            }
        });

        menuFood = findViewById(R.id.menu_food_cardview);
        menuFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // membuat intent untuk browsing
                // menggunakan browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mcdelivery.co.id/m/id/mobile/browsemenu.html"));

                // tampilkan activity
                startActivity(browserIntent);
            }
        });

    }
}