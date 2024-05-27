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

import com.example.tfg.Actividades;

import java.util.List;

public class ActivityLocalAdapter extends RecyclerView.Adapter<ActivityLocalAdapter.ActivityViewHolder> {

    private List<Actividades> actividadesList;
    private Context context;

    public ActivityLocalAdapter(List<Actividades> actividadesList, Context context) {
        this.actividadesList = actividadesList;
        this.context = context;
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
