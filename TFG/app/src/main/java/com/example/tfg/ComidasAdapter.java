package com.example.tfg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ComidasAdapter extends FirebaseRecyclerAdapter<Comidas,ComidasAdapter.comidasViewholder> {
    private Context context;
    private boolean prueba=false;
    public ComidasAdapter(
            @NonNull FirebaseRecyclerOptions<Comidas> options, Context context)
    {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ComidasAdapter.comidasViewholder comidasViewholder, int i, @NonNull Comidas comidas) {
        Log.d("Impresio",comidas.getM1()+"hoaaaaa");
        comidasViewholder.plato.setText(comidas.getM1());



        if(i==0){
            comidasViewholder.nMenu.setText("Menu 1");
            GradientDrawable grad = (GradientDrawable) comidasViewholder.container.getBackground().mutate();
            grad.setColor(Color.parseColor("#6BB71D26"));
        } else if (i==1) {
            comidasViewholder.nMenu.setText("Menu 2");
            GradientDrawable grad = (GradientDrawable) comidasViewholder.container.getBackground().mutate();
            grad.setColor(Color.parseColor("#6B028AD3"));
        } else if (i==2) {
            comidasViewholder.nMenu.setText("Menu 3");
            GradientDrawable grad = (GradientDrawable) comidasViewholder.container.getBackground().mutate();
            grad.setColor(Color.parseColor("#6B78C217"));
        }


    }

    @NonNull
    @Override
    public ComidasAdapter.comidasViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(prueba=false){
            return null;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menuscomedor, parent, false);
        return new ComidasAdapter.comidasViewholder(view);
    }



    class comidasViewholder
            extends RecyclerView.ViewHolder {
        TextView nMenu, plato;
        ConstraintLayout container;

        public comidasViewholder(@NonNull View itemView)
        {
            super(itemView);

            nMenu = itemView.findViewById(R.id.nMenu);
            plato = itemView.findViewById(R.id.plato);
            container=itemView.findViewById(R.id.conscome);
        }
    }}