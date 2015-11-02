package com.clubandroiduvigo.projectsunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Hacemos esta variable global ya que va a ser la que tengamos que avisar cuando haya algún cambio
    private GeneralForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GeneralForecastAdapter();
        recyclerView.setAdapter(adapter);
    }

    private class GeneralForecastAdapter extends RecyclerView.Adapter<GeneralForecastAdapter.ViewHolder>{
        public class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View view,int viewType){
                super(view);
            }
        }

        public GeneralForecastAdapter(){

        }

        @Override
        public int getItemCount() {
            // TODO: 2/11/15 crear getItemCount()
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO: 2/11/15 crear getItemViewType()
            return 0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // TODO: 2/11/15 crear onCreateViewHolder()
            //aqui "inflaremos" los layout en funcion del tipo
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // TODO: 2/11/15 crear onBindViewHolder()
            //aqui cambiaremos el comportamiento de cada parte en funcion de su posicion
        }
    }
}
