package com.example.tfg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AssistAdapter extends FirebaseRecyclerAdapter<
        Hijo, AssistAdapter.ActivityViewholder> {

    private Context context;
    private boolean assist = false;
    private String name;
    private String parent;

    public AssistAdapter(@NonNull FirebaseRecyclerOptions<Hijo> options, String name, String parent) {
        super(options);
        this.name = name;
        this.parent = parent;
    }



    /*
    //usamos como base el viewHolder y lo personalizamos con los datos segun la posicion
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name1.setText(userModelList.get(position).getApellidos());
        holder.name2.setText(userModelList.get(position).getApellidos());
        holder.image.setImageResource(R.drawable.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Id: "
                        + userModelList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    */


    @NonNull
    @Override
    public ActivityViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asistencia, parent, false);
        return new ActivityViewholder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityViewholder holder, int i, @NonNull Hijo model) {
        Log.d("kilo","kakita");
        holder.firstname.setText(model.getApellido1() + " " + model.getApellido2());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","clickado");
                holder.checkBox.setChecked(!holder.checkBox.isChecked());
                assist = holder.checkBox.isChecked();
                Log.d("kilo",String.valueOf(holder.checkBox.isChecked())+ " click");
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assist = holder.checkBox.isChecked();
            }
        });
    }

    public boolean checkBoxState(){
        return assist;
    }

    public String getName(){
        return name;
    }

    public String getParent(){
        return parent;
    }

    //creamos nuestro viewHolder con los tipos de elementos a modificar de un elemento (por ejemplo 2 textView)
    //obtenemos los elementos del layout_item que queremos que se vayan cambiado
    //esto es lo que vamos a ir reciclando
    class ActivityViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname, lastname;
        CheckBox checkBox;
        public ActivityViewholder(@NonNull View itemView)
        {
            super(itemView);

            firstname = itemView.findViewById(R.id.textEncima);
            checkBox = itemView.findViewById(R.id.checkBox2);

        }
    }
}
