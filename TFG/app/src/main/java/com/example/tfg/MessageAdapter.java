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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageAdapter extends FirebaseRecyclerAdapter<
        Mensajes, MessageAdapter.activityViewholder> {

    private Context context;

    public MessageAdapter(
            @NonNull FirebaseRecyclerOptions<Mensajes> options, Context context) {
        super(options);
        this.context = context;
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

    @Override
    protected void onBindViewHolder(@NonNull activityViewholder holder, int position, @NonNull Mensajes model) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (model.getActividad().equalsIgnoreCase("comedor")) {
                    holder.firstname.setText(model.getMensaje());
                    GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
                    grad.setColor(Color.parseColor("#CC028AD3"));
                } else {
                    holder.firstname.setText(snapshot.child("actividades").child(model.getActividad()).child("nombre").getValue(String.class) +
                            "\n" +
                            snapshot.child("usuario").child(model.getEmisor()).child("nombre").getValue(String.class) +
                            "\n" +
                            model.getMensaje());
                    int resourceId = context.getResources().getIdentifier(snapshot.child("actividades").child(model.getActividad()).child("icono").getValue(String.class), "drawable", context.getPackageName());
                    holder.img.setImageResource(resourceId);
                    GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
                    grad.setColor(Color.parseColor(snapshot.child("actividades").child(model.getActividad()).child("color").getValue(String.class)));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("Otro error", "Error");
            }
        });
    }

    @NonNull
    @Override
    public activityViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actividad, parent, false);
        return new activityViewholder(view);
    }


    //creamos nuestro viewHolder con los tipos de elementos a modificar de un elemento (por ejemplo 2 textView)
    //obtenemos los elementos del layout_item que queremos que se vayan cambiado
    //esto es lo que vamos a ir reciclando
    class activityViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname, lastname;
        ImageView img;
        ConstraintLayout container;

        public activityViewholder(@NonNull View itemView) {
            super(itemView);

            firstname = itemView.findViewById(R.id.textEncima);
            img = itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.constraintLay);
        }
    }
}
