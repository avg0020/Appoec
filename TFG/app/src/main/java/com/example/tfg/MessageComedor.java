package com.example.tfg;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessageComedor extends Fragment {
    MessageAdapter adapter;

    public MessageComedor() {
        // Required empty public constructor
    }

    public static MessageComedor newInstance(String param1, String param2) {
        MessageComedor fragment = new MessageComedor();
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
        View v = inflater.inflate(R.layout.fragment_message_comedor, container, false);
        ImageButton imgBtn = v.findViewById(R.id.reloadBtn);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String nom = args.getString("nombre");

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.listMesagge);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("mensajes");


        reloadRecycler(myRef, recycleViewUser,nom,user);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadRecycler(myRef, recycleViewUser,nom,user);
            }
        });

        return v;
    }

    private void reloadRecycler(DatabaseReference myRef, RecyclerView recycleViewUser, String nom,Usuarios user) {
        ConcatAdapter adapters = new ConcatAdapter();
        FragmentManager fragMan = this.getParentFragmentManager();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mensajesSnap:snapshot.getChildren()){
                    if(mensajesSnap.child("actividad").getValue(String.class).equalsIgnoreCase("comedor") &&
                            mensajesSnap.child("receptor").getValue(String.class).equalsIgnoreCase(nom) ){
                        FirebaseRecyclerOptions<Mensajes> options
                                = new FirebaseRecyclerOptions.Builder<Mensajes>()
                                .setQuery(myRef.orderByKey().equalTo(mensajesSnap.getKey()), Mensajes.class).build();
                        adapter = new MessageAdapter(options,getContext(),nom,fragMan,user);
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

        recycleViewUser.setAdapter(adapters);
    }
}