package com.clubandroiduvigo.projectsunshine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //Hacemos esta variable global ya que va a ser la que tengamos que avisar cuando haya algún cambio
    private GeneralForecastAdapter adapter;

    private ArrayList<String> stringArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GeneralForecastAdapter();
        recyclerView.setAdapter(adapter);

        Button add_button = (Button) findViewById(R.id.add_one_button);
        Button dec_button = (Button) findViewById(R.id.dec_one_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringArrayList.add(Integer.toString(stringArrayList.size()));
                adapter.notifyItemInserted(stringArrayList.size());
            }
        });
        dec_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringArrayList.size()>0) {
                    stringArrayList.remove(stringArrayList.size() - 1);
                    adapter.notifyItemRemoved(stringArrayList.size());
                }
            }
        });

        stringArrayList = new ArrayList<>(Arrays.asList("0","1","2","3","4","5"));
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
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.generic_recycler_view_layout,parent,false);
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
