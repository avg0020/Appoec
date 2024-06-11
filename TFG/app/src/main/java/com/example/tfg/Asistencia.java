package com.example.tfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Asistencia extends Fragment {

    AssistAdapter adapter;

    public Asistencia() {
        // Required empty public constructor
    }

    public static Asistencia newInstance(String param1, String param2) {
        Asistencia fragment = new Asistencia();
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
        View v = inflater.inflate(R.layout.fragment_asistencia, container, false);
        Button button = v.findViewById(R.id.btnAssist);

        Bundle args = getArguments();
        Actividades actividad = (Actividades) args.getSerializable("actividad");
        Usuarios user = (Usuarios) args.getSerializable("user");
        String username = args.getString("username");
        String fecha = args.getString("fecha");

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recyclerAssist);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("usuario");
        DatabaseReference ref = database.getReference().child("mensajes");
        FragmentManager fragManger = this.getParentFragmentManager();

        ConcatAdapter adapters = new ConcatAdapter();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot usuarioSnap:snapshot.getChildren()){
                    if (usuarioSnap.child("rol").getValue(String.class).equalsIgnoreCase("usuario")){
                        for (DataSnapshot hijoSnap:usuarioSnap.child("hijos").getChildren()){
                            for (DataSnapshot actividadeSnap:hijoSnap.child("actividades").getChildren()){
                                if(actividadeSnap.getValue(String.class).equalsIgnoreCase(actividad.getKey())){
                                    FirebaseRecyclerOptions<Hijo> options
                                            = new FirebaseRecyclerOptions.Builder<Hijo>()
                                            .setQuery(myRef.child(usuarioSnap.getKey()).child("hijos").orderByKey().equalTo(hijoSnap.getKey()), Hijo.class).build();
                                    adapter = new AssistAdapter(options, hijoSnap.getKey(), usuarioSnap.getKey());
                                    adapter.startListening();
                                    adapters.addAdapter(adapter);
                                }
                            }
                        }
                    }
                }
                recycleViewUser.setAdapter(adapters);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (RecyclerView.Adapter adapt : adapters.getAdapters()) {
                    AssistAdapter assistAdapter = (AssistAdapter) adapt;
                    Mensajes mensaje = new Mensajes();
                    mensaje.setActividad(actividad.getKey());
                    mensaje.setEmisor(user.getNombre() + " " + user.getApellido1() + " " + user.getApellido2());
                    if (assistAdapter.checkBoxState()){
                        mensaje.setMensaje("Su hijo/a " + assistAdapter.getName() + " ha asistido a "+ actividad.getNombre().toLowerCase() + " "+ actividad.getCategoria().toLowerCase()+"\n"+fecha);
                    }else {
                        mensaje.setMensaje("Su hijo/a " + assistAdapter.getName() + " no ha asistido a "+ actividad.getNombre().toLowerCase() + " "+ actividad.getCategoria().toLowerCase()+"\n"+fecha);
                    }
                    mensaje.setReceptor(assistAdapter.getParent());
                    mensaje.setCodigo(username);
                    ref.push().setValue(mensaje);


                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Se han enviado las asistencias");
                builder.setTitle("Asistencias");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ok button
                        Bundle args = new Bundle();
                        args.putSerializable("actividad", actividad);
                        args.putSerializable("user", user);
                        args.putString("nombre", username);
                        FragmentTransaction fragmentTransaction = fragManger.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, Calendar.class, args);
                        fragmentTransaction.commit();
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}