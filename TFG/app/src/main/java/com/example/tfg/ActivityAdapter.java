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
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ActivityAdapter extends FirebaseRecyclerAdapter<
        Actividades, ActivityAdapter.activityViewholder> {

    private Context context;
    private boolean prueba=false;
    public ActivityAdapter(
            @NonNull FirebaseRecyclerOptions<Actividades> options, Context context)
    {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull activityViewholder holder, int position, @NonNull Actividades model) {
        holder.firstname.setText(model.getNombre()+"\n"+model.getCategoria());
        if(model.getDias().contains("M")){
            prueba=true;
        }
        int resourceId = context.getResources().getIdentifier(model.getIcono(), "drawable", context.getPackageName());
        holder.img.setImageResource(resourceId);

    }

    @NonNull
    @Override
    public activityViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        if(prueba=false){
            return null;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actividad, parent, false);
        return new activityViewholder(view);
    }

    class activityViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname;
        ImageView img;
        public activityViewholder(@NonNull View itemView)
        {
            super(itemView);

            firstname = itemView.findViewById(R.id.textEncima);
            img = itemView.findViewById(R.id.imageView);
        }
    }
}
