package com.example.tfg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageComedor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageComedor extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MessageAdapter adapter;

    public MessageComedor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MensajeComedor.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageComedor newInstance(String param1, String param2) {
        MessageComedor fragment = new MessageComedor();
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
        View v = inflater.inflate(R.layout.fragment_message_comedor, container, false);
        ImageButton imgBtn = v.findViewById(R.id.reloadBtn);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
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


        reloadRecycler(myRef, recycleViewUser);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadRecycler(myRef, recycleViewUser);
            }
        });

        return v;
    }

    private void reloadRecycler(DatabaseReference myRef, RecyclerView recycleViewUser) {
        ConcatAdapter adapters = new ConcatAdapter();

        FirebaseRecyclerOptions<Mensajes> options
                = new FirebaseRecyclerOptions.Builder<Mensajes>()
                .setQuery(myRef.orderByChild("actividad").equalTo("comedor"), Mensajes.class).build();
        adapter = new MessageAdapter(options,getContext());
        adapter.startListening();
        adapters.addAdapter(adapter);

        recycleViewUser.setAdapter(adapters);
    }
}