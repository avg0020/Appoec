package com.example.tfg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

public class ChildrenActivityAdapter extends FirebaseRecyclerAdapter<
        Actividades, ChildrenActivityAdapter.ActivityViewholder> {

    private Context context;
    private Usuarios user;
    private AllActivities menu = null;
    private String username;

    public ChildrenActivityAdapter(
            @NonNull FirebaseRecyclerOptions<Actividades> options, Context context,Usuarios user, AllActivities menu, String username)
    {
        super(options);
        this.context = context;
        this.user = user;
        this.menu = menu;
        this.username = username;
    }

    public ChildrenActivityAdapter(
            @NonNull FirebaseRecyclerOptions<Actividades> options, Context context,Usuarios user)
    {
        super(options);
        this.context = context;
        this.user = user;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityViewholder holder, int position, @NonNull Actividades model) {

        holder.firstname.setText(model.getNombre()+"\n"+model.getCategoria());

        int resourceId = context.getResources().getIdentifier(model.getIcono(), "drawable", context.getPackageName());
        holder.img.setImageResource(resourceId);

        GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
        grad.setColor(Color.parseColor(model.getColor()));

        if (menu!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putSerializable("actividad", model);
                    FragmentManager fragmentManager = menu.getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, InfoActivity.class, args);
                    fragmentTransaction.commit();

                }
            });
        }

    }

    @NonNull
    @Override
    public ActivityViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actividad, parent, false);
        return new ActivityViewholder(view);
    }

    class ActivityViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname;
        ImageView img;
        ConstraintLayout container;
        public ActivityViewholder(@NonNull View itemView)
        {
            super(itemView);

            firstname = itemView.findViewById(R.id.textEncima);
            img = itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.constraintLay);
        }
    }
}
