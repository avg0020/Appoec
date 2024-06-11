package com.example.tfg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MenuEmpleado extends Fragment {

    ChildrenActivityAdapter adapter;

    public MenuEmpleado() {
        // Required empty public constructor
    }

    public static MenuEmpleado newInstance(String param1, String param2) {
        MenuEmpleado fragment = new MenuEmpleado();
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
        View v = inflater.inflate(R.layout.fragment_menu_empleado, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recyclerAssist);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        loadRecycler(user, myRef, recycleViewUser);

        return v;
    }

    private void loadRecycler(Usuarios user, DatabaseReference myRef, RecyclerView recycleViewUser) {
        ConcatAdapter adapters = new ConcatAdapter();
        List<String> actividades = user.getActividades();

        for (String actividad : actividades) {
            FirebaseRecyclerOptions<Actividades> options = new FirebaseRecyclerOptions.Builder<Actividades>()
                    .setQuery(myRef.orderByKey().equalTo(actividad), Actividades.class).build();
            adapter = new ChildrenActivityAdapter(options, getContext(), user);
            adapter.startListening();
            adapters.addAdapter(adapter);
        }

        recycleViewUser.setAdapter(adapters);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
