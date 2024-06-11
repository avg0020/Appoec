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
import java.util.concurrent.CountDownLatch;

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
    private String teacherName;

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

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshotValue);
    }

    private void getTeacher(OnGetDataListener interfaz) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("usuario");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                interfaz.onSuccess(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("encontrado","fuera");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_message, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String activity = args.getString("activity");
        EditText et = v.findViewById(R.id.editTextText);
        Button bt = v.findViewById(R.id.button);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                }else{
                    DatabaseReference ref = database.getReference().child("mensajes");
                    Mensajes mensaje = new Mensajes();
                    getTeacher( new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshotValue) {
                            for (DataSnapshot usuarioSnap:dataSnapshotValue.getChildren()){
                                if (usuarioSnap.child("rol").getValue(String.class).equalsIgnoreCase("empleado")){
                                    for (DataSnapshot activitiesSnap:usuarioSnap.child("actividades").getChildren()){
                                        if(activitiesSnap.getValue(String.class).equalsIgnoreCase(activity)){
                                            Log.d("actividad","actividad");
                                            mensaje.setActividad(activity);
                                            Log.d("actividad",activity);
                                            mensaje.setEmisor(user.getNombre().substring(0, 1).toUpperCase() + user.getNombre().substring(1) + " "
                                                    + user.getApellido1().substring(0, 1).toUpperCase() + user.getApellido1().substring(1) + " "
                                                    + user.getApellido2().substring(0, 1).toUpperCase() + user.getApellido2().substring(1));
                                            Log.d("actividad",user.getNombre());
                                            mensaje.setMensaje(et.getText().toString());
                                            Log.d("actividad",et.getText().toString());
                                            mensaje.setReceptor(usuarioSnap.getKey());
                                            ref.push().setValue(mensaje);
                                        }
                                    }
                                }
                            }
                        }
                    });

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Mensaje Enviado");
                    builder.setTitle("Enviado");
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