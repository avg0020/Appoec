package com.example.tfg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Correo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ChildrenActivityAdapter adapter;

    public Correo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllActivities.
     */
    // TODO: Rename and change types and number of parameters
    public static AllActivities newInstance(String param1, String param2) {
        AllActivities fragment = new AllActivities();
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
        View v = inflater.inflate(R.layout.fragment_correo, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        user.getHijos();
        // Inflate the layout for this fragment
        //setSupportActionBar(getView().findViewById(R.id.toolbar));

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recyclerAssist);
        // use a linear layout manager (distribucion de vistas configurable)
        //como queremos que se posicionen los elementos en las vistas, como lista o como cuadricula GridLayout
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);
        //puedo añadir animaciones automaticas (ItemAnimator) y sepaaciones automaticas (ItemDecoration)

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
       /* FirebaseRecyclerOptions<Actividades> options
                = new FirebaseRecyclerOptions.Builder<Actividades>()
                .setQuery(myRef.orderByKey().equalTo("BAI04A"), Actividades.class).build();
        Log.d("child", myRef.child("BAI").getKey());
        adapter = new ChildrenActivityAdapter(options,getContext(),user);
        */
        ConcatAdapter adapters = new ConcatAdapter();

       /* for (String actividad:user.getHijos()) {
            FirebaseRecyclerOptions<Actividades> options
                    = new FirebaseRecyclerOptions.Builder<Actividades>()
                    .setQuery(myRef.orderByKey().equalTo(actividad), Actividades.class).build();
            adapter = new ChildrenActivityAdapter(options,getContext(),user);
            adapter.startListening();
            adapters.addAdapter(adapter);
        }*/


        FirebaseRecyclerOptions<Actividades> options
                = new FirebaseRecyclerOptions.Builder<Actividades>()
                .setQuery(myRef.orderByChild("actividad").equalTo(true), Actividades.class).build();
        adapter = new ChildrenActivityAdapter(options,getContext(),user, this);
        adapter.startListening();
        adapters.addAdapter(adapter);


        // specify an adapter with the list to show
        recycleViewUser.setAdapter(adapters);
        return v;
    }
}
