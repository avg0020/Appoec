package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("usuario");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //button.setText(database.child("00001U").getKey());

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nom = editTextNom.getText().toString();
                        String pass = editTextPass.getText().toString();
                        Toast.makeText(getApplicationContext(), String.valueOf(snapshot.hasChild(nom)), Toast.LENGTH_LONG).show();
                        if (!nom.isEmpty() && !pass.isEmpty()){
                            if(snapshot.hasChild(nom) && snapshot.child(nom).child("password").getValue(String.class).equals(pass)) {
                                Usuarios user = snapshot.child(nom).getValue(Usuarios.class);
                                Intent i = new Intent(LoginActivity.this, Interfaz.class);
                                i.putExtra("Usuarios", user);
                                i.putExtra("nombre",nom);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(), "usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();
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