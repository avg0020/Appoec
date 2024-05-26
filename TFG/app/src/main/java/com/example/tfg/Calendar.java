package com.example.tfg;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calendar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView textViewCalendario;
    private TextView textViewFecha;
    private TextView textViewLunes;
    private TextView textViewMartes;
    private TextView textViewMiercoles;
    private TextView textViewJueves;
    private TextView textViewViernes;
    private TextView textViewSabado;
    private TextView textViewDomingo;
    private Button numeroLunes;
    private Button numeroMartes;
    private Button numeroMiercoles;
    private Button numeroJueves;
    private Button numeroViernes;
    private Button numeroSabado;
    private Button numeroDomingo;
    private String mes;
    private ArrayList<Actividades> activitiesList = new ArrayList<>();
    private int dia, mesN, ano;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ActivityAdapter adapter;

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
        // Encabezado
        textViewCalendario = v.findViewById(R.id.textViewCalendario);

        // Fecha actual
        textViewFecha = v.findViewById(R.id.textViewFecha);

        // Dias de la semana
        textViewLunes = v.findViewById(R.id.textViewLunes);
        textViewMartes = v.findViewById(R.id.textViewMartes);
        textViewMiercoles = v.findViewById(R.id.textViewMiercoles);
        textViewJueves = v.findViewById(R.id.textViewJueves);
        textViewViernes = v.findViewById(R.id.textViewViernes);
        textViewSabado = v.findViewById(R.id.textViewSabado);
        textViewDomingo = v.findViewById(R.id.textViewDomingo);

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
        textViewFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        dayWeek();

        /*
        TextView date = (TextView) v.findViewById(R.id.tvDate);
        date.setText(user.getNombre());
        */
        // Inflate the layout for this fragment

        //setSupportActionBar(getView().findViewById(R.id.toolbar));

        RecyclerView recycleViewUser = (RecyclerView) v.findViewById(R.id.recycleViewCalendar);
        // use a linear layout manager (distribucion de vistas configurable)
        //como queremos que se posicionen los elementos en las vistas, como lista o como cuadricula GridLayout
        recycleViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleViewUser.setHasFixedSize(false);
        //puedo añadir animaciones automaticas (ItemAnimator) y sepaaciones automaticas (ItemDecoration)


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        activitiesList=getActivities(myRef);
        user.getHijos();

        recycleViewUser.setAdapter(adapter);

        return v;
    }

    private void openDialog() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                textViewFecha.setText(sacarMes(month) + " " + String.valueOf(year));
                dayWeek(dayOfMonth, month, year);
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

    private ArrayList<Actividades> getActivities(DatabaseReference myRef) {
        ArrayList<Actividades> activitiesList = new ArrayList<>();
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
        return activitiesList;
    }


}


