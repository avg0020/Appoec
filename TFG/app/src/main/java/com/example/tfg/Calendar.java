package com.example.tfg;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Calendar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView textViewCalendario;
    private TextView textViewFecha;

    private TextView numeroLunes;
    private TextView numeroMartes;
    private TextView numeroMiercoles;
    private TextView numeroJueves;
    private TextView numeroViernes;
    private TextView numeroSabado;
    private TextView numeroDomingo;
    private String mes;
    private ArrayList<Actividades> activitiesList = new ArrayList<>();
    private int dia, mesN, ano;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ActivityLocalAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public Calendar() {

    }

    public static Calendar newInstance(String param1, String param2) {
        Calendar fragment = new Calendar();
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
        View v = inflater.inflate(R.layout.activity_calendario, container, false);

        Bundle args = getArguments();
        Usuarios user = (Usuarios) args.getSerializable("user");
        String nom=args.getString("nombre");

        recyclerView=v.findViewById(R.id.recycleViewCalendar);


        // Fecha actual
        textViewFecha = v.findViewById(R.id.textViewFecha);

        // Numeros de la semana
        numeroLunes = v.findViewById(R.id.numeroLunes);
        numeroMartes = v.findViewById(R.id.numeroMartes);
        numeroMiercoles = v.findViewById(R.id.numeroMiercoles);
        numeroJueves = v.findViewById(R.id.numeroJueves);
        numeroViernes = v.findViewById(R.id.numeroViernes);
        numeroSabado = v.findViewById(R.id.numeroSabado);
        numeroDomingo = v.findViewById(R.id.numeroDomingo);

        //Poner la fecha actual al cargar
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        dia = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        mesN = calendar.get(java.util.Calendar.MONTH); // Los meses en Calendar van de 0 a 11
        ano = calendar.get(java.util.Calendar.YEAR);
        textViewFecha.setText(sacarMes(mesN) + " " + ano);


        dayWeek();

        /*
        TextView date = (TextView) v.findViewById(R.id.tvDate);
        date.setText(user.getNombre());
        */


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        activitiesList=getActivities(myRef,activitiesList);

        adapter=new ActivityLocalAdapter(activitiesList,getContext(),this, user.getRol(),nom, user);
        recyclerView.setAdapter(adapter);

        comparacion(nom, user, myRef, calendar, new FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<Actividades> actividades) {
                activitiesList.clear();
                activitiesList.addAll(actividades);
                adapter.notifyDataSetChanged();
            }
        });

        textViewFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(nom,user,myRef,adapter);
            }
        });

        return v;
    }

    private void openDialog(String nom, Usuarios user, DatabaseReference myRef, ActivityLocalAdapter adapter) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                textViewFecha.setText(sacarMes(month) + " " + String.valueOf(year));
                dayWeek(dayOfMonth, month, year);
                calendar.set(year,month,dayOfMonth);
                comparacion(nom, user, myRef, calendar, new FirebaseCallback() {
                    @Override
                    public void onCallback(ArrayList<Actividades> actividades) {
                        activitiesList.clear();
                        activitiesList.addAll(actividades);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }, ano, mesN, dia);

        dialog.show();
    }

    private String sacarMes(int mesN) {
        String mes;
        switch (mesN + 1) {
            case 1:
                mes = "Enero";
                break;
            case 2:
                mes = "Febrero";
                break;
            case 3:
                mes = "Marzo";
                break;
            case 4:
                mes = "Abril";
                break;
            case 5:
                mes = "Mayo";
                break;
            case 6:
                mes = "Junio";
                break;
            case 7:
                mes = "Julio";
                break;
            case 8:
                mes = "Agosto";
                break;
            case 9:
                mes = "Septiembre";
                break;
            case 10:
                mes = "Octubre";
                break;
            case 11:
                mes = "Noviembre";
                break;
            case 12:
                mes = "Diciembre";
                break;
            default:
                mes = "Mes no válido";
                break;
        }
        return mes;

    }

    //Metodo para sacar el dia en el que se selecciona, esta (en cuestion de Lunes,martes..)
    private String dayWeek() {
        java.util.Calendar fechaActual = java.util.Calendar.getInstance();
        int diaSemana = fechaActual.get(java.util.Calendar.DAY_OF_WEEK);
        String dia;
        switch (diaSemana) {
            case java.util.Calendar.SUNDAY:
                dia = "Domingo";

                numeroDomingo.setPressed(true);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 6);
                break;
            case java.util.Calendar.MONDAY:
                dia = "Lunes";

                numeroLunes.setPressed(true);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -6);
                break;
            case java.util.Calendar.TUESDAY:
                dia = "Martes";
                numeroMartes.setPressed(true);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -6);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                break;
            case java.util.Calendar.WEDNESDAY:
                dia = "Miércoles";
                numeroMiercoles.setPressed(true);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -5);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -6);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 2);
                break;
            case java.util.Calendar.THURSDAY:
                dia = "Jueves";
                numeroJueves.setPressed(true);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -4);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 3);
                break;
            case java.util.Calendar.FRIDAY:
                dia = "Viernes";
                numeroViernes.setPressed(true);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -3);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 4);
                break;
            case java.util.Calendar.SATURDAY:
                dia = "Sábado";
                numeroSabado.setPressed(true);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -2);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 5);
                break;
            default:
                dia = "Día no válido";
                break;
        }
        return dia;
    }

    //version 2 del metodo con parametro del dia elegido segund el calendario
    private String dayWeek(int diaN, int mesN, int ano) {
        java.util.Calendar fechaActual = java.util.Calendar.getInstance();
        fechaActual.set(ano, mesN, diaN);
        int diaSemana = fechaActual.get(java.util.Calendar.DAY_OF_WEEK);
        String dia;
        switch (diaSemana) {
            case java.util.Calendar.SUNDAY:
                dia = "Domingo";
                numeroDomingo.setPressed(true);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 6);
                break;
            case java.util.Calendar.MONDAY:
                dia = "Lunes";

                numeroLunes.setPressed(true);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -6);
                break;
            case java.util.Calendar.TUESDAY:
                dia = "Martes";
                numeroMartes.setPressed(true);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -6);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                break;
            case java.util.Calendar.WEDNESDAY:
                dia = "Miércoles";
                numeroMiercoles.setPressed(true);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -5);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 2);
                break;
            case java.util.Calendar.THURSDAY:
                dia = "Jueves";
                numeroJueves.setPressed(true);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -4);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 3);
                break;
            case java.util.Calendar.FRIDAY:
                dia = "Viernes";
                numeroViernes.setPressed(true);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -3);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 4);
                break;
            case java.util.Calendar.SATURDAY:
                dia = "Sábado";
                numeroSabado.setPressed(true);
                numeroSabado.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -2);
                numeroViernes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroJueves.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroMartes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, -1);
                numeroLunes.setText(String.valueOf(fechaActual.get(java.util.Calendar.DAY_OF_MONTH)));
                fechaActual.add(java.util.Calendar.DAY_OF_MONTH, 5);
                break;
            default:
                dia = "Día no válido";
                break;
        }
        return dia;
    }

    private ArrayList<Actividades> getActivities(DatabaseReference myRef,ArrayList<Actividades>actividades) {
        ArrayList<Actividades> activitiesList = actividades;
        myRef.child("actividades").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ;
                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    Actividades activity = activitySnapshot.getValue(Actividades.class);
                    if (activity != null) {
                        activitiesList.add(activity);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error
                System.err.println("Error: " + error.getMessage());
            }
        });
        return actividades;
    }
//Obtenemos los hijos de los cuales sacamos las actividades que tienen que ser mostradas para ese usario, obsoleto
    private List<Hijo> mapeoHijos(Usuarios usuario) {

    List<Hijo> hijitos=new ArrayList<>();
                if (usuario != null && usuario.getHijos() != null) {
                    // Obtener información de los hijos
                    for (Map.Entry<String, Hijo> entry : usuario.getHijos().entrySet()) {
                        String key = entry.getKey();
                        Hijo hijo = entry.getValue();
                        hijitos.add(hijo);
                    }
                }
                return hijitos;
            }

    //metodo que compara las actividades relacionadas del usuario con las que hay en la base de datos y cuya coincidencia aparecerá en el recycler view
    //interfaz para que no devuelva la lista de actividades vacia
    public interface FirebaseCallback {
        void onCallback(ArrayList<Actividades> actividades);
    }
    public void comparacion(String nom, Usuarios user, DatabaseReference myRef, java.util.Calendar calendar, FirebaseCallback callback) {
        //donde acumularemos las actividades a mostrar
        ArrayList<Actividades> mostrar = new ArrayList<>();
        //Sacamos primero el dia
        int diaDeLaSemana = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        // Convertir el valor numérico del día de la semana a su nombre correspondiente
        String nombreDiaDeLaSemana = "";
        switch (diaDeLaSemana) {
            case java.util.Calendar.SUNDAY:
                nombreDiaDeLaSemana = "D";
                break;
            case java.util.Calendar.MONDAY:
                nombreDiaDeLaSemana = "L";
                break;
            case java.util.Calendar.TUESDAY:
                nombreDiaDeLaSemana = "M";
                break;
            case java.util.Calendar.WEDNESDAY:
                nombreDiaDeLaSemana = "X";
                break;
            case java.util.Calendar.THURSDAY:
                nombreDiaDeLaSemana = "J";
                break;
            case java.util.Calendar.FRIDAY:
                nombreDiaDeLaSemana = "V";
                break;
            case java.util.Calendar.SATURDAY:
                nombreDiaDeLaSemana = "S";
                break;
        }
        final String finalNombreDiaDeLaSemana = nombreDiaDeLaSemana;
        //saca las calves de los hijos, seran nombres de los hijos

        DatabaseReference prueba = myRef.child("usuario").child(nom).getRef();
        DatabaseReference profes=myRef.child("usuario").child("actividades").getRef();
        // Añadir un listener para obtener los datos del nodo padre
        if(user.getRol().toString().equalsIgnoreCase("usuario")){
        prueba.child("hijos").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> clavesHijos = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    // Procesar los datos obtenidosup
                    for (DataSnapshot hijoSnapshot : dataSnapshot.getChildren())
                        clavesHijos.add(hijoSnapshot.getKey());
                } else {
                    Log.d("Firebase", "No hay datos disponibles");
                }
                // Contador para verificar cuando todas las actividades hayan sido procesadas

                for (int i = 0; i < clavesHijos.size(); i++) {

                    for (String actividad : user.getHijos().get(clavesHijos.get(i).toString()).getActividades()) {
                        Log.d("actividad",actividad);
                        DatabaseReference reference = myRef.child("actividades").child(actividad).getRef();


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Verifica si la actividad actual tiene un hijo "dias"
                                if (dataSnapshot.hasChild("dias")) {
                                    // Obtiene el valor del hijo "dias"
                                    String diasValue = dataSnapshot.child("dias").getValue(String.class);
                                    String[] diaExacto = diasValue.split("/");
                                    // Comprueba si el valor coincide con tu string deseado
                                    if (diaExacto[0].equalsIgnoreCase(finalNombreDiaDeLaSemana)) {
                                        mostrar.add(dataSnapshot.getValue(Actividades.class));
                                        Log.d("TAG", "Se agregó una actividad a la lista: " + dataSnapshot.getValue(Actividades.class).toString());

                                    } else if (diaExacto[1].equalsIgnoreCase(finalNombreDiaDeLaSemana)) {
                                        mostrar.add(dataSnapshot.getValue(Actividades.class));
                                        Log.d("TAG", "Se agregó una actividad a la lista: " + dataSnapshot.getValue(Actividades.class).getNombre().toString());
                                    }
                                }
                                callback.onCallback(mostrar);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar errores de base de datos, si es necesario
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
                System.out.println("Error al obtener datos: " + databaseError.getMessage());
            }


        });
    }else if (user.getRol().toString().equalsIgnoreCase("empleado")){
            profes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (String actividad : user.getActividades()) {
                            DatabaseReference reference = myRef.child("actividades").child(actividad).getRef();


                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Verifica si la actividad actual tiene un hijo "dias"
                                    if (dataSnapshot.hasChild("dias")) {
                                        // Obtiene el valor del hijo "dias"
                                        String diasValue = dataSnapshot.child("dias").getValue(String.class);
                                        String[] diaExacto = diasValue.split("/");
                                        // Comprueba si el valor coincide con tu string deseado
                                        if (diaExacto[0].equalsIgnoreCase(finalNombreDiaDeLaSemana)) {
                                            mostrar.add(dataSnapshot.getValue(Actividades.class));
                                            Log.d("TAG", "Se agregó una actividad a la lista: " + dataSnapshot.getValue(Actividades.class).toString());

                                        } else if (diaExacto[1].equalsIgnoreCase(finalNombreDiaDeLaSemana)) {
                                            mostrar.add(dataSnapshot.getValue(Actividades.class));
                                            Log.d("TAG", "Se agregó una actividad a la lista: " + dataSnapshot.getValue(Actividades.class).getNombre().toString());
                                        }
                                    }
                                    callback.onCallback(mostrar);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Manejar errores de base de datos, si es necesario
                                }
                            });
                        }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }
    public void obtenerTodasLasActividades(DatabaseReference myRef, FirebaseCallback callback) {
        ArrayList<Actividades> todasLasActividades = new ArrayList<>();

        DatabaseReference actividadesRef = myRef.child("actividades");
        actividadesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot actividadSnapshot : dataSnapshot.getChildren()) {
                    Actividades actividad = actividadSnapshot.getValue(Actividades.class);
                    todasLasActividades.add(actividad);
                }
                callback.onCallback(todasLasActividades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de la base de datos, si es necesario
            }
        });
    }
        }




