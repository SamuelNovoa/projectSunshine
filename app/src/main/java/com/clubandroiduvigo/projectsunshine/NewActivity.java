package com.clubandroiduvigo.projectsunshine;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ImageView bigAndroid = (ImageView) findViewById(R.id.ejemplo_bigAndroid);
        bigAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewActivity.this, getString(R.string.ejemplo_bigAndroid_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
