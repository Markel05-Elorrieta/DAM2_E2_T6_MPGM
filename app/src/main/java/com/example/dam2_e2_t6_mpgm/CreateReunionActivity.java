package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.media.tv.TimelineRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.parceler.Parcels;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import callbacks.NewBileraCallback;
import callbacks.ScheduleStudentCallback;
import callbacks.ScheduleTeacherCallback;
import model.Horarios;
import model.Reuniones;
import model.Users;
import model.dao.MHorarios;
import model.dao.MReuniones;
import model.dao.MUsers;

public class CreateReunionActivity extends AppCompatActivity {
    private Metodos metodos;
    private ArrayList<Users> usersList;
    private ArrayList<Horarios> horariosUser;
    Reuniones newReunion;

    private String estado = "pendiente";
    private String[] opcionesUser;
    String[] opcionesIkastetxe;

    private EditText txt_izenburuaReunion;
    private EditText txt_gaiaReunion;
    private EditText txt_dayReunion;
    private EditText txt_hourReunion;
    private EditText txt_gelaReunion;
    private Spinner s_usersReuniones;
    private int sPosIkastetxe = 0;
    private Spinner s_ikastetxeakReuniones;
    private int sPosIrakasle = 0;

    private Button btnCancel;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_reunion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*----------------- INSTANCES ------------------*/
        metodos = new Metodos();

        /*-----------------GET PARCEL ELEMENTS------------------*/
        usersList = Parcels.unwrap(getIntent().getParcelableExtra("usersList"));
        horariosUser = Parcels.unwrap(getIntent().getParcelableExtra("horarios"));

        /*-----------------SET VIEW ELEMENTS------------------*/
        txt_izenburuaReunion = findViewById(R.id.txt_izenburuaReunion);
        txt_gaiaReunion = findViewById(R.id.txt_gaiaReunion);
        txt_dayReunion = findViewById(R.id.txt_dayReunion);
        txt_hourReunion = findViewById(R.id.txt_hourReunion);
        s_usersReuniones = findViewById(R.id.s_usersReuniones);
        s_ikastetxeakReuniones = findViewById(R.id.s_ikastetxeakReuniones);
        txt_gelaReunion = findViewById(R.id.txt_gelaReunion);

        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);

        /*-----------------FILL VIEW ELEMENTS------------------*/

        opcionesUser = metodos.arrayListToArrayUser(usersList);
        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesUser);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_usersReuniones.setAdapter(adapterUser);

        opcionesIkastetxe = metodos.arrayListToArrayIkastetxeak(GlobalVariables.ikastetxeak);
        ArrayAdapter<String> adapterIkastetxe = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesIkastetxe);
        adapterIkastetxe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_ikastetxeakReuniones.setAdapter(adapterIkastetxe);

        s_usersReuniones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPosIrakasle = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        s_ikastetxeakReuniones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPosIkastetxe = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                String izenburua = txt_izenburuaReunion.getText().toString();
                String gaia = txt_gaiaReunion.getText().toString();
                String day = txt_dayReunion.getText().toString();
                String hour = txt_hourReunion.getText().toString();
                String fechaString = day + " " + hour;
                Timestamp fecha = stringToTimestamp(fechaString);
                String gela = txt_gelaReunion.getText().toString();


                if (GlobalVariables.logedUser.getTipos().getId() == 3) {
                    if(haveConflict(parseDay(fecha), parseHour(hour), horariosUser)) {
                        Log.d("CreateReunionActivity", "conflictus");
                        estado = "conflicto";
                    }
                    newReunion = new Reuniones(GlobalVariables.logedUser, usersList.get(sPosIrakasle), estado, null, GlobalVariables.ikastetxeak.get(sPosIkastetxe).getCCEN() + "", izenburua, gaia, gela, fecha);
                    finishCreation();

                } else {
                    MHorarios mUsers = new MHorarios("scheduleTeacher", usersList.get(sPosIrakasle).getId(), new ScheduleTeacherCallback() {
                        @Override
                        public void onScheduleTeacher(ArrayList<Horarios> horarios) {
                            if (haveConflict(parseDay(fecha), parseHour(hour), horarios)) {
                                Log.d("CreateReunionActivity", "conflictus");
                                estado = "conflicto";
                            }
                            newReunion = new Reuniones(usersList.get(sPosIrakasle), GlobalVariables.logedUser, estado, null, GlobalVariables.ikastetxeak.get(sPosIkastetxe).getCCEN() + "", izenburua, gaia, gela, fecha);
                            finishCreation();
                        }
                    });
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void finishCreation(){
        MReuniones mReuniones = new MReuniones("newBilera", newReunion, new NewBileraCallback() {
            @Override
            public void onNewBileraCallback(Reuniones reunion) {
                Intent intent = new Intent();
                intent.putExtra("newBilera", Parcels.wrap(reunion));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private String parseDay(Timestamp time) {
        String sDay = "";
        Log.d("CreateReunionActivity", time.getDay() + "");
        switch (time.getDay()) {
            case 1:
                sDay = "L/A";
                break;
            case 2:
                sDay = "M/A";
                break;
            case 3:
                sDay = "X";
                break;
            case 4:
                sDay = "J/O";
                break;
            case 5:
                sDay = "V/O";
                break;
        }
        Log.d("CreateReunionActivity", sDay);
        return sDay;
    }

    private int parseHour(String hour) {
        int iHour = 0;

        String[] separado = hour.split(":");
        Log.d("CreateReunionActivity", Integer.parseInt(separado[0]) + "");
        switch (Integer.parseInt(separado[0])) {
            case 8:
                iHour = 0;
                break;
            case 9:
                iHour = 1;
                break;
            case 10:
                iHour = 2;
                break;
            case 11:
                iHour = 3;
                break;
            case 12:
                iHour = 4;
                break;
        }
        Log.d("CreateReunionActivity", iHour + "");
        return iHour;
    }

    private boolean haveConflict(String sDay, int iHour, ArrayList<Horarios> horarios) {
        boolean conflict = false;

        for (Horarios horario : horarios) {
            if (horario.getId().getDia().equals(sDay) && metodos.charToInt(horario.getId().getHora()) == iHour) {
                Log.d("CreateReunionActivity", horario.getId().getDia() + " " + sDay + " " + horario.getId().getHora() + " " + iHour);
                conflict = true;
            }
        }
        Log.d("CreateReunionActivity", conflict + "");

        return conflict;
    }

    private Timestamp stringToTimestamp(String fechaString) {
        Timestamp fecha = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date parsedDate = null;
            parsedDate = dateFormat.parse(fechaString);
            fecha = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fecha;
    }
}