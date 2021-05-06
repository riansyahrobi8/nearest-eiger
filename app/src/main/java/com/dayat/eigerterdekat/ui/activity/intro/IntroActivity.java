package com.dayat.eigerterdekat.ui.activity.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dayat.eigerterdekat.R;
import com.dayat.eigerterdekat.ui.activity.MainActivity;

public class IntroActivity extends AppCompatActivity {

    // deklarasi konteks
    private Context context;

    // deklarasi tombol let's begin
    private Button btnBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // panggil layout activity intro
        setContentView(R.layout.activity_intro);

        // jalankan fungsi initwidget
        initWidget();
    }

    private void initWidget() {

        // inisialisasi konteks
        this.context = this;

        // cari tombol begin pada layout activity
        btnBegin = findViewById(R.id.btn_begin);

        // beri event atau gesture agar bisa ditekan
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // arahkan ke halaman main activity atau halaman dimana map dimunculkan
                startActivity(new Intent(context, MainActivity.class));

                // hancurkanactivity
                finish();
            }
        });
    }
}