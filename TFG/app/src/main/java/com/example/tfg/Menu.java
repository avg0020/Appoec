package com.example.tfg;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class Menu extends Fragment {

    String hijo;

    ArrayList<UserModel> datos;
    TextView txt;

    PanelActivityAdapter adapter;

    private DrawerLayout drawerLayout;

    public Menu() {
        // Required empty public constructor
    }

    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
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
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String userName = args.getString("nombre");
        hijo = user.getHijos().entrySet().iterator().next().getKey();
        Spinner spin = v.findViewById(R.id.sp);
        ArrayList<String> hijos = new ArrayList<String>();
        for (Map.Entry<String, Hijo> entry : user.getHijos().entrySet()) {
            hijos.add(entry.getKey());
        }

        ArrayAdapter<String> adap = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item ,hijos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adap);

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recyclerAssist);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        loadRecycler(user, myRef, recycleViewUser,userName);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hijo = (String) spin.getItemAtPosition(position);
                loadRecycler(user, myRef, recycleViewUser,userName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }

    private void loadRecycler(Usuarios user, DatabaseReference myRef, RecyclerView recycleViewUser, String username) {
        ConcatAdapter adapters = new ConcatAdapter();

        for (String actividad: user.getHijos().get(hijo).getActividades()) {
            if (!actividad.equalsIgnoreCase("comedor")) {
                FirebaseRecyclerOptions<Actividades> options
                        = new FirebaseRecyclerOptions.Builder<Actividades>()
                        .setQuery(myRef.orderByKey().equalTo(actividad), Actividades.class).build();
                adapter = new PanelActivityAdapter(options, getContext(), user, this,username);
                adapter.startListening();
                adapters.addAdapter(adapter);
            }
        }
        recycleViewUser.setAdapter(adapters);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop() {
        super.onStop();
       // adapter.stopListening();
    }
}
