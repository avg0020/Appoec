package com.example.tfg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MessageAdapter adapter;

    public MessageEmp() {
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
    public static MessageEmp newInstance(String param1, String param2) {
        MessageEmp fragment = new MessageEmp();
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
        MessageEmp mensaje = this;


        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.listMesagge);
        // use a linear layout manager
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("mensajes");

        ConcatAdapter adapters = new ConcatAdapter();

        // Modificar la lógica para obtener las actividades del usuario
        // Verificamos si user y user.getActividades() no son nulos antes de acceder a las actividades
        if (user != null && user.getActividades() != null) {
            for (String actividad : user.getActividades()) {
                FirebaseRecyclerOptions<Mensajes> options
                        = new FirebaseRecyclerOptions.Builder<Mensajes>()
                        .setQuery(myRef.orderByChild("actividad").equalTo(actividad), Mensajes.class).build();
                adapter = new MessageAdapter(options, getContext());
                adapter.startListening();
                adapters.addAdapter(adapter);
            }
        } else {
            // Si no hay actividades asociadas al usuario, puedes manejar esto de alguna manera, como mostrar un mensaje de error o no mostrar ningún mensaje en absoluto.
            Log.d("MessageEmp", "El usuario o sus actividades asociadas son nulos.");
        }

        recycleViewUser.setAdapter(adapters);

        return v;
    }



}