package com.clubandroiduvigo.projectsunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String Q_KEY = "q";
    private static final String API_KEY = "APPID";
    private static final String COUNT_KEY = "cnt";
    private static final String UNITS_KEY = "units";
    private static final String LANG_KEY = "lang";
    //Hacemos esta variable global ya que va a ser la que tengamos que avisar cuando haya algún cambio
    private GeneralForecastAdapter adapter;

    private JSONArray forecastJSONArray;

    private final String APPID = "43070a922fe7ec2f792ece8cb9292d8f";
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GeneralForecastAdapter();
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetForecastTask forecastTask = new GetForecastTask();
                Uri uri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(Q_KEY,"Vigo,ES")
                        .appendQueryParameter(COUNT_KEY,"7")
                        .appendQueryParameter(API_KEY, APPID)
                        .appendQueryParameter(UNITS_KEY,"metric")
                        .appendQueryParameter(LANG_KEY,"es")
                        .build();
                URL url;
                try {
                    url = new URL(uri.toString());
                    forecastTask.execute(url);
                } catch (MalformedURLException e) {
                    Log.e("MainActivity","initialize - Problema con Uri: "+e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        initialize();
    }

    private void initialize() { //Metodo para inicializar la actividad
        forecastJSONArray = new JSONArray();
        GetForecastTask forecastTask = new GetForecastTask();
        Uri uri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(Q_KEY,"Vigo,ES")
                .appendQueryParameter(COUNT_KEY,"7")
                .appendQueryParameter(API_KEY, APPID)
                .appendQueryParameter(UNITS_KEY,"metric")
                .appendQueryParameter(LANG_KEY,"es")
                .build();
        URL url;
        try {
            url = new URL(uri.toString());
            forecastTask.execute(url);
        } catch (MalformedURLException e) {
            Log.e("MainActivity","initialize - Problema con Uri: "+e.getMessage());
            e.printStackTrace();
        }
    }


    private class GetForecastTask extends AsyncTask<URL,Void,JSONObject>{ //AsyncTask Parameters -> <doInBackground,Progress,Result>
        @Override
        protected JSONObject doInBackground(URL... params) {
            JSONObject result_json = null;
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
                        result_json = new JSONObject(result);
                        Log.d("GetForecastTask", "doInBackground - Result = " + result);
                    }

                } catch (IOException e) {
                    Log.e("GetForecastTask","doInBackground - Problema con la conexion: "+e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.e("GetForecastTask", "doInBackground - Fallo del servidor: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return result_json; //En nuestro caso, solo hay una url a procesar. Aun asi, lo hacemos asi para mostrar un caso generico
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (!jsonObject.getString("cod").equals("200")){
                    // TODO: 3/11/15 {"cod":"404","message":"Error: Not found city"} reaccionar ante error
                }
                else { //cod == 200
                    refreshLayout.setRefreshing(false);
                    forecastJSONArray = jsonObject.getJSONArray("list");
                    //Detalle: Esto es cambiar el layout. SOLO SE PUEDE HACER EN PRE O POST EXECUTE, ya que estas funciones
                    //son ejecutadas desde el MainThread. Si intentaramos cambiar la pantalla desde doInBackground, la app
                    //rompe
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Log.e("GetForecastTask","onPostExecute - Error al procesar el resultado: "+e.getMessage());
                e.printStackTrace();
            }
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
            return forecastJSONArray.length();
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
            try {
                final JSONObject actual_forecast = forecastJSONArray.getJSONObject(position);
                if (holder.viewType == GENERIC_TYPE){
                    holder.generic_textView.setText(actual_forecast.getString("dt"));
                    holder.generic_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Toast.makeText(MainActivity.this, actual_forecast.getString("dt"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.e("GeneralForecastAdapter","onClick - "+e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                Log.e("GeneralForecastAdapter","onBindViewHolder - "+e.getMessage());
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
}
