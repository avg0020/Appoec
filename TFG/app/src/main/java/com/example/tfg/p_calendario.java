package com.example.tfg;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class p_calendario extends AppCompatActivity {
    private TextView textViewCalendario;
    private TextView textViewFecha;
    private TextView textViewLunes;
    private TextView textViewMartes;
    private TextView textViewMiercoles;
    private TextView textViewJueves;
    private TextView textViewViernes;
    private TextView textViewSabado;
    private TextView textViewDomingo;
    private TextView numeroLunes;
    private TextView numeroMartes;
    private TextView numeroMiercoles;
    private TextView numeroJueves;
    private TextView numeroViernes;
    private TextView numeroSabado;
    private TextView numeroDomingo;
    private String mes;
   private ActivityAdapter adapter;
    private int dia,mesN,ano;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendario);




        // Fecha actual
         textViewFecha = findViewById(R.id.textViewFecha);

        // Dias de la semana
       textViewLunes = findViewById(R.id.textViewLunes);
        textViewMartes = findViewById(R.id.textViewMartes);
         textViewMiercoles = findViewById(R.id.textViewMiercoles);
        textViewJueves = findViewById(R.id.textViewJueves);
        textViewViernes = findViewById(R.id.textViewViernes);
         textViewSabado = findViewById(R.id.textViewSabado);
         textViewDomingo = findViewById(R.id.textViewDomingo);

        // Numeros de la semana
         numeroLunes = findViewById(R.id.numeroLunes);
         numeroMartes = findViewById(R.id.numeroMartes);
        numeroMiercoles = findViewById(R.id.numeroMiercoles);
         numeroJueves = findViewById(R.id.numeroJueves);
         numeroViernes = findViewById(R.id.numeroViernes);
         numeroSabado = findViewById(R.id.numeroSabado);
         numeroDomingo = findViewById(R.id.numeroDomingo);


         //Poner la fecha actual al cargar
        Calendar calendar = Calendar.getInstance();

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mesN = calendar.get(Calendar.MONTH) ; // Los meses en Calendar van de 0 a 11
         ano = calendar.get(Calendar.YEAR);
        textViewFecha.setText(sacarMes(mesN)+" "+ano);
        textViewFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        dayWeek();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recycleViewUser = (RecyclerView) findViewById(R.id.recycleViewCalendar);
        // use a linear layout manager (distribucion de vistas configurable)
        //como queremos que se posicionen los elementos en las vistas, como lista o como cuadricula GridLayout
        recycleViewUser.setLayoutManager(new LinearLayoutManager(this));
        //puedo añadir animaciones automaticas (ItemAnimator) y sepaaciones automaticas (ItemDecoration)

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("actividades");

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Actividades> options
                = new FirebaseRecyclerOptions.Builder<Actividades>()
                .setQuery(myRef, Actividades.class)
                .build();

        adapter = new ActivityAdapter(options,this);
        // specify an adapter with the list to show
        recycleViewUser.setAdapter(adapter);
        Log.d("dasdsadas",options.getSnapshots().toString());

    }
    private void openDialog(){
        DatePickerDialog dialog=new DatePickerDialog(this,  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            textViewFecha.setText(sacarMes(month)+" "+String.valueOf(year));
            dayWeek(dayOfMonth,month,year);
            }
        },ano, mesN, dia);
        dialog.show();
    }
    private String sacarMes(int mesN){
        String mes;
        switch (mesN+1) {
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
    private String dayWeek(){
        Calendar fechaActual=Calendar.getInstance();
        int diaSemana=fechaActual.get(Calendar.DAY_OF_WEEK);
        String dia;
        switch (diaSemana) {
            case Calendar.SUNDAY:
                dia = "Domingo";

                numeroDomingo.setPressed(true);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,6);
                break;
            case Calendar.MONDAY:
                dia = "Lunes";

                numeroLunes.setPressed(true);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-6);
                break;
            case Calendar.TUESDAY:
                dia = "Martes";
                numeroMartes.setPressed(true);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-6);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                break;
            case Calendar.WEDNESDAY:
                dia = "Miércoles";
                numeroMiercoles.setPressed(true);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-5);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-6);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,2);
                break;
            case Calendar.THURSDAY:
                dia = "Jueves";
                numeroJueves.setPressed(true);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-4);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,3);
                break;
            case Calendar.FRIDAY:
                dia = "Viernes";
                numeroViernes.setPressed(true);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-3);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,4);
                break;
            case Calendar.SATURDAY:
                dia = "Sábado";
                numeroSabado.setPressed(true);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-2);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,5);
                break;
            default:
                dia = "Día no válido";
                break;
        }
        return dia;
    }
    //version 2 del metodo con parametro del dia elegido segund el calendario
    private String dayWeek(int diaN, int mesN, int ano){
        Calendar fechaActual=Calendar.getInstance();
        fechaActual.set(ano,mesN,diaN);
        int diaSemana=fechaActual.get(Calendar.DAY_OF_WEEK);
        String dia;
        switch (diaSemana) {
            case Calendar.SUNDAY:
                dia = "Domingo";
                numeroDomingo.setPressed(true);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,6);
                break;
            case Calendar.MONDAY:
                dia = "Lunes";

                numeroLunes.setPressed(true);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-6);
                break;
            case Calendar.TUESDAY:
                dia = "Martes";
                numeroMartes.setPressed(true);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-6);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                break;
            case Calendar.WEDNESDAY:
                dia = "Miércoles";
                numeroMiercoles.setPressed(true);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-5);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,2);
                break;
            case Calendar.THURSDAY:
                dia = "Jueves";
                numeroJueves.setPressed(true);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-4);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,3);
                break;
            case Calendar.FRIDAY:
                dia = "Viernes";
                numeroViernes.setPressed(true);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-3);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,4);
                break;
            case Calendar.SATURDAY:
                dia = "Sábado";
                numeroSabado.setPressed(true);
                numeroSabado.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,1);
                numeroDomingo.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-2);
                numeroViernes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroJueves.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMiercoles.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroMartes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,-1);
                numeroLunes.setText(String.valueOf(fechaActual.get(Calendar.DAY_OF_MONTH)));
                fechaActual.add(Calendar.DAY_OF_MONTH,5);
                break;
            default:
                dia = "Día no válido";
                break;
        }
        return dia;
    }
    //metodo para cargar desde el día en el que estamos y marcar el boton en el que estamos
    private void marcarDia(String dia){


    }
}