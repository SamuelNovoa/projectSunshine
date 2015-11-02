package com.clubandroiduvigo.projectsunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.ejemplo_textView);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setText(getString(R.string.ejemplo_setText)); //Usando el IDE
        Button button = (Button) findViewById(R.id.ejemplo_boton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NewActivity.class);
                startActivity(intent);
            }
        });
    }

//    Métodos usados para enseñar ciclo de vida de una actividad
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause - Entra onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume - Entra onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","onStop - Entra onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart - Entra onStart");
    }
}
