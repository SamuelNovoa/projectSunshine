package com.clubandroiduvigo.projectsunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String ID_KEY = "id";
    private static final String API_KEY = "APPID";
    //Hacemos esta variable global ya que va a ser la que tengamos que avisar cuando haya algún cambio
    private GeneralForecastAdapter adapter;

    private ArrayList<String> stringArrayList;

    private final String APPID = "43070a922fe7ec2f792ece8cb9292d8f";
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/city";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GeneralForecastAdapter();
        recyclerView.setAdapter(adapter);

        Button refresh_button = (Button) findViewById(R.id.refresh_button);
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetForecastTask forecastTask = new GetForecastTask();
                Uri uri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(ID_KEY,"94043")
                        .appendQueryParameter(API_KEY,APPID)
                        .build();
                URL url = null;
                try {
                    url = new URL(uri.toString());
                    forecastTask.execute(url);
                } catch (MalformedURLException e) {
                    Log.e("MainActivity","onClick - Problema con Uri: "+uri.toString());
                    e.printStackTrace();
                }

            }
        });

        stringArrayList = new ArrayList<>(Arrays.asList("0","1","2","3","4","5"));
    }

    private class GetForecastTask extends AsyncTask<URL,Void,JSONObject>{ //AsyncTask Parameters -> <doInBackground,Progress,Result>
        @Override
        protected JSONObject doInBackground(URL... params) {
            for (int i=0;i<params.length;i++){
                try {
                    HttpURLConnection connection = (HttpURLConnection) params[i].openConnection();
                    connection.connect();
                    if (connection.getResponseCode()!= 200){ //Si no es code OK, miramos que pasa
                        // TODO: 3/11/15  Connection code != 200
                    }
                    else { //Si es code OK, actualizamos la informacion
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine())!=null){ //Mientras queden lineas, continua añadiendo al stringBuilder
                            stringBuilder.append(line);
                        }
                        String result = stringBuilder.toString();
                        Log.d("GetForecastTask", "doInBackground - Result = " + result);
                    }

                } catch (IOException e) {
                    Log.e("GetForecastTask","doInBackground - Problema con la conexion: "+e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private class GeneralForecastAdapter extends RecyclerView.Adapter<GeneralForecastAdapter.ViewHolder>{
        private static final int GENERIC_TYPE = 0;

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView generic_textView;
            RelativeLayout generic_layout;
            int viewType;
            public ViewHolder(View view,int viewType){
                super(view);
                if (viewType == GENERIC_TYPE){
                    generic_textView = (TextView) view.findViewById(R.id.generic_recyclerView_textView);
                    generic_layout = (RelativeLayout) view;
                }
                this.viewType = viewType;
            }
        }

        public GeneralForecastAdapter(){

        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
//            De momento solo hay un tipo
            return GENERIC_TYPE;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //aqui "inflaremos" los layout en funcion del tipo
            View view = null;
            if (viewType == GENERIC_TYPE){ //Ahora mismo, siempre sera de este tipo
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.forecast_recycler_view_layout,parent,false);
            }
            return new ViewHolder(view,viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            //aqui cambiaremos el comportamiento de cada parte en funcion de su posicion
            if (holder.viewType == GENERIC_TYPE){
                holder.generic_textView.setText(stringArrayList.get(position)); //position = 0 es ADD_MORE_TYPE
                holder.generic_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, stringArrayList.get(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
