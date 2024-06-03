package com.example.tfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateMessage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateMessage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateMessage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateMessage.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateMessage newInstance(String param1, String param2) {
        CreateMessage fragment = new CreateMessage();
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
        View v = inflater.inflate(R.layout.fragment_create_message, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        Spinner spin = v.findViewById(R.id.sp);
        EditText et = v.findViewById(R.id.editTextText);
        Button bt = v.findViewById(R.id.button);
        ArrayList<String> actividades = new ArrayList<String>();
        for (Map.Entry<String, Hijo> entry : user.getHijos().entrySet()) {
            for (String actividad: entry.getValue().getActividades()){
                actividades.add(actividad);
            }
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> actividadesNom = new ArrayList<String>();
                for (String act: actividades){
                    if(snapshot.hasChild(act)){
                        actividadesNom.add(snapshot.child(act).child("nombre").getValue(String.class) + " " + snapshot.child(act).child("categoria").getValue(String.class));
                    }
                }
                ArrayAdapter<String> adap = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_dropdown_item ,actividadesNom);
                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(adap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mensajito", et.getText().toString());
                if(TextUtils.isEmpty(et.getText())){
                    Log.d("mensajito","aisujuhiasdguiadsgoyvyiudasfvyasdiuylasdcvhas");
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("No has escrito ningún mensaje");
                    builder.setTitle("Aviso");
                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // ok button
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
            }
        });

        return v;
    }
}