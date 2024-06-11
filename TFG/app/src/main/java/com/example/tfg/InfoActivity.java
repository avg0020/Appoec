package com.example.tfg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InfoActivity extends Fragment {

    public InfoActivity() {
        // Required empty public constructor
    }

    public static InfoActivity newInstance(String param1, String param2) {
        InfoActivity fragment = new InfoActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info_activity, container, false);

        Bundle args = getArguments();
        Actividades activity = (Actividades) args.getSerializable("actividad");

        TextView name = v.findViewById(R.id.tvActivtyName);
        TextView dates = v.findViewById(R.id.tvActivityDates);
        TextView prof = v.findViewById(R.id.tvProfesor);

        name.setText(activity.getNombre() + "\n" + activity.getCategoria());

        String[] dias = activity.getDias().split("/");
        dates.setText("Horarios\n");
        for (int i = 0; i < dias.length; i++) {
            if (dias[i].equalsIgnoreCase("L")) {
                dates.setText(dates.getText() + "Lunes: " + activity.getHorario() + "\n");
            } else if (dias[i].equalsIgnoreCase("M")) {
                dates.setText(dates.getText() + "Martes: " + activity.getHorario() + "\n");
            } else if (dias[i].equalsIgnoreCase("X")) {
                dates.setText(dates.getText() + "Miercoles: " + activity.getHorario() + "\n");
            } else if (dias[i].equalsIgnoreCase("J")) {
                dates.setText(dates.getText() + "Jueves: " + activity.getHorario() + "\n");
            } else if (dias[i].equalsIgnoreCase("V")) {
                dates.setText(dates.getText() + "Viernes: " + activity.getHorario() + "\n");
            }
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("usuario");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                };
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (snap.hasChild("actividades")) {
                        List<String> actividades = snap.child("actividades").getValue(t);
                        for (String act : actividades) {
                            if (activity.getKey().equalsIgnoreCase(act)) {
                                prof.setText("Profesor: " + snap.child("nombre").getValue(String.class));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v;
    }
}