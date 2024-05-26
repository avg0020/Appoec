package com.example.tfg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoActivity newInstance(String param1, String param2) {
        InfoActivity fragment = new InfoActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        name.setText(activity.getNombre() + "\n"+ activity.getCategoria());

        String[] dias = activity.getDias().split("/");
        dates.setText("Horarios\n");
        for (int i = 0; i < dias.length; i++) {
            if(dias[i].equalsIgnoreCase("L")){
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
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                for (DataSnapshot snap:snapshot.getChildren()){
                    if (snap.hasChild("actividades")){
                        List<String> actividades = snap.child("actividades").getValue(t);
                        for (String act:actividades){
                            if(activity.getKey().equalsIgnoreCase(act)){
                                prof.setText("Profesor: "+snap.child("nombre").getValue(String.class));
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