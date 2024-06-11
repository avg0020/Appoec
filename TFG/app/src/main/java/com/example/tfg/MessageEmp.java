package com.example.tfg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageEmp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageEmp extends Fragment {

    MessageAdapter adapter;

    public MessageEmp() {
        // Required empty public constructor
    }

    public static MessageEmp newInstance(String param1, String param2) {
        MessageEmp fragment = new MessageEmp();
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
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        ImageButton imgBtn = v.findViewById(R.id.reloadBtn);
        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String nom = args.getString("nombre");
        MessageEmp mensaje = this;


        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.listMesagge);
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("mensajes");

        reloadRecycler(myRef, nom, recycleViewUser,user);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadRecycler(myRef, nom, recycleViewUser, user);
            }
        });

        return v;
    }

    private void reloadRecycler(DatabaseReference myRef, String nom, RecyclerView recycleViewUser,Usuarios user) {
        ConcatAdapter adapters = new ConcatAdapter();

        FirebaseRecyclerOptions<Mensajes> options
                = new FirebaseRecyclerOptions.Builder<Mensajes>()
                .setQuery(myRef.orderByChild("receptor").equalTo(nom), Mensajes.class).build();
        adapter = new MessageAdapter(options, getContext(),nom,this.getParentFragmentManager(),user);
        adapter.startListening();
        adapters.addAdapter(adapter);

        recycleViewUser.setAdapter(adapters);
    }


}