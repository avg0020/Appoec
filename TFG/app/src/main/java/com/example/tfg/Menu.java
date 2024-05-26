package com.example.tfg;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<UserModel> datos;
    TextView txt;

    ChildrenActivityAdapter adapter;

    private DrawerLayout drawerLayout;

    public Menu() {
        // Required empty public constructor
    }

    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        if (user == null) {
            Log.e("Menu", "El usuario es nulo.");
            Toast.makeText(getContext(), "Error: Usuario no disponible.", Toast.LENGTH_LONG).show();
            return v;
        }

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recycleViewUser);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        ConcatAdapter adapters = new ConcatAdapter();

        Map<String, Alumno> hijos = user.getHijos();
        if (hijos != null) {
            Alumno alumno = hijos.get("fulanita");
            if (alumno != null) {
                List<String> actividades = alumno.getActividades();
                if (actividades != null) {
                    for (String actividad : actividades) {
                        FirebaseRecyclerOptions<Actividades> options = new FirebaseRecyclerOptions.Builder<Actividades>()
                                .setQuery(myRef.orderByKey().equalTo(actividad), Actividades.class)
                                .build();
                        adapter = new ChildrenActivityAdapter(options, getContext(), user);
                        adapter.startListening();
                        adapters.addAdapter(adapter);
                    }
                } else {
                    Log.e("Menu", "El alumno 'fulanita' no tiene actividades.");
                    Toast.makeText(getContext(), "El alumno 'fulanita' no tiene actividades.", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e("Menu", "El alumno 'fulanita' no existe.");
                Toast.makeText(getContext(), "El alumno 'fulanita' no existe.", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.e("Menu", "El usuario no tiene hijos.");
            Toast.makeText(getContext(), "El usuario no tiene hijos.", Toast.LENGTH_LONG).show();
        }

        recycleViewUser.setAdapter(adapters);
        return v;
    }

    @Override public void onStart() {
        super.onStart();
        Log.d("Menu", "Fragmento iniciado.");
    }

    @Override public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
