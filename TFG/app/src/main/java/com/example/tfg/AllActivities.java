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

public class AllActivities extends Fragment {

    ChildrenActivityAdapter adapter;

    public AllActivities() {
        // Required empty public constructor
    }

    public static AllActivities newInstance(String param1, String param2) {
        AllActivities fragment = new AllActivities();
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
        View v = inflater.inflate(R.layout.fragment_all_activities, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String userName = args.getString("nombre");

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recyclerAssist);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        ConcatAdapter adapters = new ConcatAdapter();

        FirebaseRecyclerOptions<Actividades> options
                = new FirebaseRecyclerOptions.Builder<Actividades>()
                .setQuery(myRef.orderByChild("actividad").equalTo(true), Actividades.class).build();
        adapter = new ChildrenActivityAdapter(options,getContext(),user, this,userName);
        adapter.startListening();
        adapters.addAdapter(adapter);

        recycleViewUser.setAdapter(adapters);
        return v;
    }
}