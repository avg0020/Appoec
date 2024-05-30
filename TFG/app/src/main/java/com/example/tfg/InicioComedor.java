package com.example.tfg;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioComedor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioComedor extends Fragment {
    private int dia, mesN, ano;
    private TextView textViewFecha;

    private Button numeroLunes;
    private Button numeroMartes;
    private Button numeroMiercoles;
    private Button numeroJueves;
    private Button numeroViernes;
    private Button numeroSabado;
    private Button numeroDomingo;
    private RecyclerView.LayoutManager layoutManager;
    private ComidasAdapter adaptador;
    private RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioComedor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioComedor.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioComedor newInstance(String param1, String param2) {
        InicioComedor fragment = new InicioComedor();
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
        View v=inflater.inflate(R.layout.fragment_inicio_comedor, container, false);
        recyclerView=v.findViewById(R.id.recycleViewMenuComedor);
        // Fecha actual
        textViewFecha = v.findViewById(R.id.textViewFechac);

        // Numeros de la semana
        numeroLunes = v.findViewById(R.id.cnumeroLunes);
        numeroMartes = v.findViewById(R.id.cnumeroMartes);
        numeroMiercoles = v.findViewById(R.id.cnumeroMiercoles);
        numeroJueves = v.findViewById(R.id.cnumeroJueves);
        numeroViernes = v.findViewById(R.id.cnumeroViernes);
        numeroSabado = v.findViewById(R.id.cnumeroSabado);
        numeroDomingo = v.findViewById(R.id.cnumeroDomingo);
        //Poner la fecha actual al cargar
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        dia = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        mesN = calendar.get(java.util.Calendar.MONTH); // Los meses en Calendar van de 0 a 11
        ano = calendar.get(java.util.Calendar.YEAR);
        textViewFecha.setText(sacarMes(mesN) + " " + ano);
        dayWeek();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        int dian= calendar.get(Calendar.DAY_OF_WEEK);
        String dia=getDayOfWeek(dian);

        FirebaseRecyclerOptions<Comidas> options
                = new FirebaseRecyclerOptions.Builder<Comidas>()
                .setQuery(myRef.child("comedor").child("00001C").child("semana").child(dia), Comidas.class).build();
        adaptador = new ComidasAdapter(options,getContext());
        adaptador.startListening();
        recyclerView.setAdapter(adaptador);
        textViewFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(myRef);
            }
        });

        return v;
    }
    private void openDialog( DatabaseReference myRef ) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                textViewFecha.setText(sacarMes(month) + " " + String.valueOf(year));
                dayWeek(dayOfMonth, month, year);
                calendar.set(year,month,dayOfMonth);
                int diaDeLaSemana = calendar.get(java.util.Calendar.DAY_OF_WEEK);
                String auxdia=getDayOfWeek(diaDeLaSemana);
                FirebaseRecyclerOptions<Comidas> options
                        = new FirebaseRecyclerOptions.Builder<Comidas>()
                        .setQuery(myRef.child("comedor").child("00001C").child("semana").child(auxdia), Comidas.class).build();
                adaptador = new ComidasAdapter(options,getContext());
                adaptador.startListening();
                recyclerView.setAdapter(adaptador);

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

    private String getDayOfWeek(int dayOfWeek) {
        String dayName="";
        switch (dayOfWeek) {
            case 1:
                dayName = "1";
                break;
            case 2:
                dayName = "2";
                break;
            case 3:
                dayName = "3";
                break;
            case 4:
                dayName = "4";
                break;
            case 5:
                dayName = "5";
                break;
            case 6:
                dayName = "6";
                break;
            default:
                dayName = "Número de día de la semana inválido";
                break;
        }
        return dayName;
    }
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
}