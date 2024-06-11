package com.example.tfg;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.firstname.setText( name.substring(0, 1).toUpperCase() + name.substring(1) + " "
                +  model.getApellido1().substring(0, 1).toUpperCase() + model.getApellido1().substring(1) + " " +
                model.getApellido2().substring(0, 1).toUpperCase() + model.getApellido2().substring(1) );
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

    class ActivityViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname;
        CheckBox checkBox;
        public ActivityViewholder(@NonNull View itemView)
        {
            super(itemView);

            firstname = itemView.findViewById(R.id.textEncima);
            checkBox = itemView.findViewById(R.id.checkBox2);

        }
    }
}
