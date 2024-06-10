package com.example.tfg;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String hijo;

    ArrayList<UserModel> datos;
    TextView txt;

    PanelActivityAdapter adapter;

    private DrawerLayout drawerLayout;

    public Menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Menu newInstance(String param1, String param2) {
        Menu fragment = new Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        hijo = user.getHijos().entrySet().iterator().next().getKey();
        Spinner spin = v.findViewById(R.id.sp);
        ArrayList<String> hijos = new ArrayList<String>();
        for (Map.Entry<String, Hijo> entry : user.getHijos().entrySet()) {
            Log.d("mira aqui",entry.getKey() + " : " + entry.getValue().toString());
            hijos.add(entry.getKey());
        }

        ArrayAdapter<String> adap = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item ,hijos);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adap);

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

       /* for (String actividad:user.getHijos()) {
            FirebaseRecyclerOptions<Actividades> options
                    = new FirebaseRecyclerOptions.Builder<Actividades>()
                    .setQuery(myRef.orderByKey().equalTo(actividad), Actividades.class).build();
            adapter = new ChildrenActivityAdapter(options,getContext(),user);
            adapter.startListening();
            adapters.addAdapter(adapter);
        }*/
        loadRecycler(user, myRef, recycleViewUser);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hijo = (String) spin.getItemAtPosition(position);
                loadRecycler(user, myRef, recycleViewUser);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }

    private void loadRecycler(Usuarios user, DatabaseReference myRef, RecyclerView recycleViewUser) {
        ConcatAdapter adapters = new ConcatAdapter();

        for (String actividad: user.getHijos().get(hijo).getActividades()) {
            if (!actividad.equalsIgnoreCase("comedor")) {
                FirebaseRecyclerOptions<Actividades> options
                        = new FirebaseRecyclerOptions.Builder<Actividades>()
                        .setQuery(myRef.orderByKey().equalTo(actividad), Actividades.class).build();
                adapter = new PanelActivityAdapter(options, getContext(), user, this);
                adapter.startListening();
                adapters.addAdapter(adapter);
            }
        }
        recycleViewUser.setAdapter(adapters);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("dasdsadas", "entrado");

    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop() {
        super.onStop();
       // adapter.stopListening();
    }
}
