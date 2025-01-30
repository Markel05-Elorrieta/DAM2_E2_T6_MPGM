package com.example.dam2_e2_t6_mpgm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.parceler.Parcels;

import java.util.ArrayList;

import model.Horarios;
import model.Ikastetxeak;
import model.Reuniones;
import model.Users;

public class BilerakUserActivity extends AppCompatActivity {

    private Metodos metodos;

    private ArrayList<Ikastetxeak> ikastetxeak;
    private ArrayList<Horarios> horariosIkasle;
    private ArrayList<Reuniones> reunionesIkasle;
    private ArrayList<Horarios> horariosIrakasle;
    private ArrayList<Reuniones> reunionesIrakasle;
    private ArrayList<Users> usersList;

    private String[][] scheduleView;
    private Reuniones[][] scheduleReuniones;

    private TableLayout tableLayoutBilerakUsers;
    private Button btnAtzeraBilerakUsers;
    private Button btnCreateReunionBilerakUsers;

    private int x;
    private int y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bilerak_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*----------------- INSTANCES ------------------*/
        metodos = new Metodos();

        /*-----------------GET PARCEL ELEMENTS------------------*/

        if (GlobalVariables.logedUser.getTipos().getId() == 3) {
            horariosIrakasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIrakasle"));
            reunionesIrakasle = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIrakasle"));
        } else {
            horariosIkasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIkasle"));
            reunionesIkasle = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIkasle"));
        }

        usersList = Parcels.unwrap(getIntent().getParcelableExtra("usersList"));


        /*-----------------SET VIEW ELEMENTS------------------*/
        tableLayoutBilerakUsers = findViewById(R.id.tableLayoutBilerakUsers);
        btnAtzeraBilerakUsers = findViewById(R.id.btnAtzeraBilerakUsers);
        btnCreateReunionBilerakUsers = findViewById(R.id.btnCreateReunionBilerakUsers);

        /*-----------------FILL VIEW ELEMENTS------------------*/
        tableLayoutBilerakUsers.addView(metodos.createHeaderRow(this));

        if (GlobalVariables.logedUser.getTipos().getId() == 3) {
            scheduleView = metodos.generateArrayTableWReuniones(horariosIkasle, reunionesIkasle);
        }
/*
        reunionesTable = metodos.generateArrayReunionesTable(reunionesIkasle);
        fillTable(tableLayoutIkasleBilerak, schedule);*/
    }
}