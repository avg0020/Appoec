package com.example.tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class Interfaz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout fragmentContainer;
    private TextView option1, option2, option3, option4, option5, option6, option7;
    private LinearLayout menuLayoutComedor, menuLayout;
    private DrawerLayout drawerLayout;
    private Usuarios user;
    private String nom;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interfaz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_icon1);

        user = (Usuarios) getIntent().getSerializableExtra("Usuarios");
        nom=  getIntent().getStringExtra("nombre");
        // Referencias de los elementos del layout
        fragmentContainer = findViewById(R.id.fragment_container);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        option5 = findViewById(R.id.option5);
        option6 = findViewById(R.id.option6);
        option7 = findViewById(R.id.option7);
        menuLayoutComedor = findViewById(R.id.menuLayoutComedor);
        menuLayout = findViewById(R.id.menuLayout);

        Menu menuFragment = new Menu();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        args.putString("nombre", nom);
        menuFragment.setArguments(args);
        changeFragment(menuFragment);



        // Manejar clics en las opciones del menú
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Menu menuFragment = new Menu();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                args.putString("nombre", nom);
                menuFragment.setArguments(args);
                changeFragment(menuFragment);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Calendar caleFragment=new Calendar();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                args.putString("nombre", nom);
                caleFragment.setArguments(args);
                changeFragment(caleFragment);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message messageFragment = new Message();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                args.putString("nombre", nom);
                messageFragment.setArguments(args);
                changeFragment(messageFragment);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllActivities allActivities = new AllActivities();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                args.putString("nombre", nom);
                allActivities.setArguments(args);
                changeFragment(allActivities);
            }
        });

        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InicioComedor inicioComedor = new InicioComedor();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                inicioComedor.setArguments(args);
                changeFragment(inicioComedor);
            }
        });

        option6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageComedor messageComedor = new MessageComedor();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                messageComedor.setArguments(args);
                changeFragment(messageComedor);
            }
        });

        option7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecomendacionesComedor recomendacionesComedor = new RecomendacionesComedor();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                recomendacionesComedor.setArguments(args);
                changeFragment(recomendacionesComedor);
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show();
        if(menuItem.getItemId() == R.id.nav_icon1) {
            if (menuLayout.getVisibility() == View.INVISIBLE){
                Menu menuFragment = new Menu();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                menuFragment.setArguments(args);
                changeFragment(menuFragment);
            }
            menuLayout.setVisibility(View.VISIBLE);
            menuLayoutComedor.setVisibility(View.INVISIBLE);
        } else if (menuItem.getItemId() == R.id.nav_icon2) {
            if (menuLayoutComedor.getVisibility() == View.INVISIBLE){
                InicioComedor inicioComedor = new InicioComedor();
                Bundle args = new Bundle();
                args.putSerializable("user", user);
                inicioComedor.setArguments(args);
                changeFragment(inicioComedor);
            }
            menuLayout.setVisibility(View.INVISIBLE);
            menuLayoutComedor.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
