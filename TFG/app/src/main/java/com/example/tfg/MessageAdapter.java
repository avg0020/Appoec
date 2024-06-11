package com.example.tfg;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    private String userName;
    private FragmentManager fragmentManager;
    private Usuarios user;

    public MessageAdapter(
            @NonNull FirebaseRecyclerOptions<Mensajes> options, Context context, String userName, FragmentManager fragmentManager, Usuarios user) {
        super(options);
        this.context = context;
        this.userName = userName;
        this.fragmentManager = fragmentManager;
        this.user = user;
    }

    @Override
    protected void onBindViewHolder(@NonNull activityViewholder holder, int position, @NonNull Mensajes model) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (model.getActividad().equalsIgnoreCase("comedor")) {
                    holder.firstname.setText(snapshot.child("actividades").child(model.getActividad()).child("nombre").getValue(String.class) + "\n" +
                            model.getEmisor() +
                            "\n" +
                            model.getMensaje());
                    int resourceId = context.getResources().getIdentifier(snapshot.child("actividades").child(model.getActividad()).child("icono").getValue(String.class), "drawable", context.getPackageName());
                    holder.img.setImageResource(resourceId);
                    GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
                    grad.setColor(Color.parseColor(snapshot.child("actividades").child(model.getActividad()).child("color").getValue(String.class)));
                } else {
                    holder.firstname.setText(snapshot.child("actividades").child(model.getActividad()).child("nombre").getValue(String.class) + " " +snapshot.child("actividades").child(model.getActividad()).child("categoria").getValue(String.class) +
                            "\n" +
                            model.getEmisor() +
                            "\n" +
                            model.getMensaje());

                    int resourceId = context.getResources().getIdentifier(snapshot.child("actividades").child(model.getActividad()).child("icono").getValue(String.class), "drawable", context.getPackageName());
                    holder.img.setImageResource(resourceId);
                    GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
                    grad.setColor(Color.parseColor(snapshot.child("actividades").child(model.getActividad()).child("color").getValue(String.class)));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putSerializable("user", user);
                        args.putString("activity", model.getActividad());
                        args.putString("codigo", model.getCodigo());
                        args.putString("username", userName);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, CreateMessage2.class, args);
                        fragmentTransaction.commit();

                    }
                });
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

    class activityViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname;
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
