package com.example.tfg;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class Interfaz extends AppCompatActivity {

    private FrameLayout fragmentContainer;
    private TextView option1, option2, option3, option4;
    private DrawerLayout drawerLayout;
    private Usuarios user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        user = (Usuarios) getIntent().getSerializableExtra("Usuarios");

        // Referencias de los elementos del layout
        fragmentContainer = findViewById(R.id.fragment_container);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        Menu menuFragment = new Menu();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        menuFragment.setArguments(args);
        changeFragment(menuFragment);


        // Manejar clics en las opciones del menú
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu menuFragment = new Menu();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                menuFragment.setArguments(args);
                changeFragment(menuFragment);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message messageFragment = new Message();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                messageFragment.setArguments(args);
                changeFragment(messageFragment);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Correo activityFragment = new Correo();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                activityFragment.setArguments(args);
                changeFragment(activityFragment);
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
