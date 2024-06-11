package com.example.tfg;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Message#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Message extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MessageAdapter adapter;

    public Message() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Message.
     */
    // TODO: Rename and change types and number of parameters
    public static Message newInstance(String param1, String param2) {
        Message fragment = new Message();
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
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String nom = args.getString("nombre");
        ImageButton imgBtn = v.findViewById(R.id.reloadBtn);

        /*
        TextView date = (TextView) v.findViewById(R.id.tvDate);
        date.setText(user.getNombre());
        */
        // Inflate the layout for this fragment

        //setSupportActionBar(getView().findViewById(R.id.toolbar));

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.listMesagge);
        // use a linear layout manager (distribucion de vistas configurable)
        //como queremos que se posicionen los elementos en las vistas, como lista o como cuadricula GridLayout
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);
        //puedo añadir animaciones automaticas (ItemAnimator) y sepaaciones automaticas (ItemDecoration)

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("mensajes");

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data

        loadRecycler(user, myRef, nom, recycleViewUser);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRecycler(user, myRef, nom, recycleViewUser);
            }
        });

        return v;
    }

    private void loadRecycler(Usuarios user, DatabaseReference myRef, String nom, RecyclerView recycleViewUser) {
        ConcatAdapter adapters = new ConcatAdapter();
        for (Hijo hijo: user.getHijos().values()){
            for (String actividad:hijo.getActividades()) {
                Log.d("actividad",actividad);
                if (!actividad.equalsIgnoreCase("comedor")){
                    FirebaseRecyclerOptions<Mensajes> options
                            = new FirebaseRecyclerOptions.Builder<Mensajes>()
                            .setQuery(myRef.orderByChild("receptor").equalTo(actividad), Mensajes.class).build();
                    adapter = new MessageAdapter(options,getContext());
                    adapter.startListening();
                    adapters.addAdapter(adapter);
                    Log.d("texto","texto");
                }
            }
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mensajesSnap:snapshot.getChildren()){
                    if(!mensajesSnap.child("actividad").getValue(String.class).equalsIgnoreCase("comedor") &&
                            mensajesSnap.child("receptor").getValue(String.class).equalsIgnoreCase(nom) ){
                        FirebaseRecyclerOptions<Mensajes> options
                                = new FirebaseRecyclerOptions.Builder<Mensajes>()
                                .setQuery(myRef.orderByKey().equalTo(mensajesSnap.getKey()), Mensajes.class).build();
                        adapter = new MessageAdapter(options,getContext());
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
}