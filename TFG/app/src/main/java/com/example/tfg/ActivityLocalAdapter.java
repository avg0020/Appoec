package com.example.tfg;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Actividades;

import java.util.List;

public class ActivityLocalAdapter extends RecyclerView.Adapter<ActivityLocalAdapter.ActivityViewHolder> {

    private List<Actividades> actividadesList;
    private Context context;
    private com.example.tfg.Calendar calendar;
    private String rol;
    private String userName;
    private Usuarios user;

    public ActivityLocalAdapter(List<Actividades> actividadesList, Context context, Calendar calendar,String rol, String userName, Usuarios user) {
        this.actividadesList = actividadesList;
        this.context = context;
        this.calendar = calendar;
        this.rol = rol;
        this.userName = userName;
        this.user = user;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actividad, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        Actividades actividad = actividadesList.get(position);
        holder.firstname.setText(actividad.getNombre() + "\n" + actividad.getCategoria());
        Log.d("pruebaaaaaa","asdasdasddsaasdasddasasddassdaasdsdasdsda");
        int resourceId = context.getResources().getIdentifier(actividad.getIcono(), "drawable", context.getPackageName());
        holder.img.setImageResource(resourceId);
        GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
        grad.setColor(Color.parseColor(actividad.getColor()));

        if (this.rol.equalsIgnoreCase("empleado")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putSerializable("actividad", actividad);
                    args.putString("username",userName);
                    args.putSerializable("user",user);
                    FragmentManager fragmentManager = calendar.getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, Asistencia.class, args);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return actividadesList.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView firstname;
        ImageView img;
        ConstraintLayout container;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            firstname = itemView.findViewById(R.id.textEncima);
            img = itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.constraintLay);
        }
    }
}
