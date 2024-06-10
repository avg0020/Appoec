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

import android.util.Log;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Asistencia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Asistencia extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AssistAdapter adapter;

    public Asistencia() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Asistencia.
     */
    // TODO: Rename and change types and number of parameters
    public static Asistencia newInstance(String param1, String param2) {
        Asistencia fragment = new Asistencia();
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
        View v = inflater.inflate(R.layout.fragment_asistencia, container, false);
        Button button = v.findViewById(R.id.btnAssist);

        Bundle args = getArguments();
        Actividades actividad = (Actividades) args.getSerializable("actividad");
        Usuarios user = (Usuarios) args.getSerializable("user");
        String username = args.getString("username");

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
                                    Log.d("kilo", hijoSnap.getKey());
                                    FirebaseRecyclerOptions<Hijo> options
                                            = new FirebaseRecyclerOptions.Builder<Hijo>()
                                            .setQuery(myRef.child(usuarioSnap.getKey()).child("hijos").orderByKey().equalTo(hijoSnap.getKey()), Hijo.class).build();
                                    Log.d("kilo", hijoSnap.getKey());
                                    adapter = new AssistAdapter(options, hijoSnap.getKey(), usuarioSnap.getKey());
                                    adapter.startListening();
                                    adapters.addAdapter(adapter);
                                }
                            }
                        }
                    }
                }
                recycleViewUser.setAdapter(adapters);
                Log.d("kilo",String.valueOf(adapters.getAdapters().size()));

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
                    Log.d("kilo", String.valueOf(assistAdapter.checkBoxState()));
                    Mensajes mensaje = new Mensajes();
                    mensaje.setActividad(actividad.getKey());
                    mensaje.setEmisor(user.getNombre() + " " + user.getApellido1() + " " + user.getApellido2());
                    if (assistAdapter.checkBoxState()){
                        mensaje.setMensaje("Su hijo " + assistAdapter.getName() + " ha asistido a "+ actividad.getNombre() + " "+ actividad.getCategoria());
                    }else {
                        mensaje.setMensaje("Su hijo " + assistAdapter.getName() + " no ha asistido a "+ actividad.getNombre() + " "+ actividad.getCategoria());
                    }
                    mensaje.setReceptor(assistAdapter.getParent());
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