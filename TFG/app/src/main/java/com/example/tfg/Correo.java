package com.example.tfg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class Correo extends Fragment {// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String hijo;

    ArrayList<UserModel> datos;
    TextView txt;

    ChildrenActivityAdapter adapter;

    private DrawerLayout drawerLayout;

    public Correo() {
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_correo, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        RecyclerView rv=v.findViewById(R.id.recycleViewCorreo);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");
        loadRecycler(user,myRef,rv);

        return v;
    }

    private void loadRecycler(Usuarios user, DatabaseReference myRef, RecyclerView recycleViewUser) {
        ConcatAdapter adapters = new ConcatAdapter();


            FirebaseRecyclerOptions<Actividades> options
                    = new FirebaseRecyclerOptions.Builder<Actividades>()
                    .setQuery(myRef.orderByKey(), Actividades.class).build();
            adapter = new ChildrenActivityAdapter(options, getContext(), user);
            adapter.startListening();
            adapters.addAdapter(adapter);

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

