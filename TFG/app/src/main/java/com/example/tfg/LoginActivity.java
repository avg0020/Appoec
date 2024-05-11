package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText editTextNom;
    EditText editTextPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextNom = findViewById(R.id.etUser);
        editTextPass =  findViewById(R.id.etPassword);
        Button button =  findViewById(R.id.btnLogIn);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("usuarios");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //button.setText(database.child("00001U").getKey());

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("E","Entro");
                        String nom = editTextNom.getText().toString()+"";
                        String pass = editTextPass.getText().toString()+"";
                        Log.d("E",""+ snapshot.hasChild(nom));
                        if(snapshot.hasChild(nom) && !nom.equals("")) {
                            Log.d("k","que haces?");
                            if(snapshot.child(nom).child("password").getValue(String.class).equals(pass)) {
                                startActivity(new Intent(LoginActivity.this, Prueba.class));
                            }
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.d("E","Error");
                    }
                });
            }
        });
    }
}