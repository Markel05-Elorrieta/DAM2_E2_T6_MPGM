package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import model.Reuniones;
import model.Users;
import model.dao.MReuniones;

public class CreateReunionActivity extends AppCompatActivity {
    private ArrayList<Users> irakasleak;

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
        Metodos medodos = new Metodos();

        irakasleak = Parcels.unwrap(getIntent().getParcelableExtra("irakasleak"));

        txt_izenburuaReunion = findViewById(R.id.txt_izenburuaReunion);
        txt_gaiaReunion = findViewById(R.id.txt_gaiaReunion);
        txt_dayReunion = findViewById(R.id.txt_dayReunion);
        txt_hourReunion = findViewById(R.id.txt_hourReunion);
        s_usersReuniones = findViewById(R.id.s_usersReuniones);
        s_ikastetxeakReuniones = findViewById(R.id.s_ikastetxeakReuniones);

        txt_gelaReunion = findViewById(R.id.txt_gelaReunion);

        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);

        String[] opcionesUser = medodos.arrayListToArrayUser(irakasleak);
        ArrayAdapter<String> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesUser);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_usersReuniones.setAdapter(adapterUser);

        String[] opcionesIkastetxe = medodos.arrayListToArrayIkastetxeak(GlobalVariables.ikastetxeak);
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
                String gela = txt_gelaReunion.getText().toString();

                Timestamp fecha = null;
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    Date parsedDate = null;
                    parsedDate = dateFormat.parse(day + " " + hour);
                    fecha = new Timestamp(parsedDate.getTime());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                Reuniones reunion;
                if (GlobalVariables.logedUser.getTipos().getId() == 3) {
                    reunion = new Reuniones(GlobalVariables.logedUser, irakasleak.get(sPosIrakasle), "pendiente", null, GlobalVariables.ikastetxeak.get(sPosIkastetxe).getCCEN() + "", izenburua, gaia, gela, fecha);
                } else {
                    reunion = new Reuniones(irakasleak.get(sPosIrakasle), GlobalVariables.logedUser, "pendiente", null, GlobalVariables.ikastetxeak.get(sPosIkastetxe).getCCEN() + "", izenburua, gaia, gela, fecha);
                }
                MReuniones mReuniones = new MReuniones("newBilera", reunion, new NewBileraCallback() {
                    @Override
                    public void onNewBileraCallback(Reuniones reunion) {
                        Log.d("reunionesssss", reunion +"");
                        Intent intent = new Intent();
                        intent.putExtra("reunion", Parcels.wrap(reunion));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
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
}