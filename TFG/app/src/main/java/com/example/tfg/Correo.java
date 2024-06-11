package com.example.tfg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Correo extends Fragment {
    String hijo;

    ArrayList<UserModel> datos;
    TextView txt;

    PanelActivityAdapter adapter;
    Correo correo;

    private DrawerLayout drawerLayout;

    public Correo() {
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
        View v = inflater.inflate(R.layout.activity_correo, container, false);
        correo = this;

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        RecyclerView rv=v.findViewById(R.id.recycleViewCorreo);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");
        loadRecycler(user,myRef,rv);

        return v;
    }

    private void loadRecycler(Usuarios user, DatabaseReference myRef, RecyclerView recycleViewUser) {
        ConcatAdapter adapters = new ConcatAdapter();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot actividadesSnap:snapshot.getChildren()){
                    if(!user.getActividades().contains(actividadesSnap.getKey())){
                        FirebaseRecyclerOptions<Actividades> options
                                = new FirebaseRecyclerOptions.Builder<Actividades>()
                                .setQuery(myRef.orderByKey().equalTo(actividadesSnap.getKey()), Actividades.class).build();
                        adapter = new PanelActivityAdapter(options, getContext(), user, correo);
                        adapter.startListening();
                        adapters.addAdapter(adapter);
                    }
                }
                recycleViewUser.setAdapter(adapters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

