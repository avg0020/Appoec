package com.example.tfg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateMessage2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateMessage2 extends Fragment {

    public CreateMessage2() {
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
    public static CreateMessage2 newInstance(String param1, String param2) {
        CreateMessage2 fragment = new CreateMessage2();
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
        View v = inflater.inflate(R.layout.fragment_create_message, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String activity = args.getString("activity");
        String codigo = args.getString("codigo");
        String username = args.getString("username");
        EditText et = v.findViewById(R.id.editTextText);
        Button bt = v.findViewById(R.id.button);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et.getText())) {
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
                } else {
                    DatabaseReference ref = database.getReference().child("mensajes");
                    Mensajes mensaje = new Mensajes();

                    mensaje.setActividad(activity);
                    mensaje.setEmisor(user.getNombre().substring(0, 1).toUpperCase() + user.getNombre().substring(1) + " "
                            + user.getApellido1().substring(0, 1).toUpperCase() + user.getApellido1().substring(1) + " "
                            + user.getApellido2().substring(0, 1).toUpperCase() + user.getApellido2().substring(1));
                    mensaje.setMensaje(et.getText().toString());
                    mensaje.setReceptor(codigo);
                    mensaje.setCodigo(username);
                    ref.push().setValue(mensaje);

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