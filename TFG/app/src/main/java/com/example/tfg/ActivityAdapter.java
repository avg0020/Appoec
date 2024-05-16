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

    public ActivityAdapter(
            @NonNull FirebaseRecyclerOptions<Actividades> options, Context context)
    {
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
    protected void onBindViewHolder(@NonNull activityViewholder holder, int position, @NonNull Actividades model) {
        holder.firstname.setText(model.getNombre()+"\n"+model.getCategoria());
        Log.d("como","asdasdasddsaasdasddasasddassdaasdsdasdsda");

        int resourceId = context.getResources().getIdentifier(model.getIcono(), "drawable", context.getPackageName());
        holder.img.setImageResource(resourceId);
        //GradientDrawable grad = (GradientDrawable) holder.container.getBackground().mutate();
        //grad.setColor(Color.parseColor(model.getColor()));

    }

    @NonNull
    @Override
    public activityViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
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
        ImageView container;
        public activityViewholder(@NonNull View itemView)
        {
            super(itemView);

            firstname = itemView.findViewById(R.id.textEncima);
            img = itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.circle_1);
        }
    }
}
