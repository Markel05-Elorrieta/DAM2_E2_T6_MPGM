package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private ArrayList<Horarios> horarios;
    private ArrayList<Reuniones> reuniones;
    private ArrayList<Users> usersList;

    private String[][] scheduleView;
    private Reuniones[][] scheduleReuniones;

    private TableLayout tableLayoutBilerakUsers;
    private Button btnAtzeraBilerakUsers;
    private Button btnCreateReunionBilerakUsers;

    private int x;
    private int y;

    private final ActivityResultLauncher<Intent> finishCreation =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Reuniones newBilera = Parcels.unwrap(data.getParcelableExtra("newBilera"));
                    reuniones.add(newBilera);

                    Toast.makeText(BilerakUserActivity.this, getString(R.string.meetingCreationSuccesfully), Toast.LENGTH_SHORT).show();

                    tableLayoutBilerakUsers.removeAllViews();
                    tableLayoutBilerakUsers.addView(metodos.createHeaderRow(this));
                    scheduleView = metodos.generateArrayTableWReuniones(horarios, reuniones);
                    scheduleReuniones = metodos.generateArrayReunionesTable(reuniones);
                    fillTable(tableLayoutBilerakUsers, scheduleView);
                } else {
                    Toast.makeText(BilerakUserActivity.this, getString(R.string.meetingCreationCanceled), Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> finishDetails =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_CANCELED) {

                    Reuniones reunionNewEstado = Parcels.unwrap(result.getData().getParcelableExtra("reunionNewEstado"));
                    for (Reuniones reunion : reuniones) {
                        if (reunion.getIdReunion() == reunionNewEstado.getIdReunion()) {
                            reunion.setEstado(reunionNewEstado.getEstado());
                            break;
                        }
                    }

                    Toast.makeText(BilerakUserActivity.this, getString(R.string.meetingUpdatedSuccesfully), Toast.LENGTH_SHORT).show();


                    tableLayoutBilerakUsers.removeAllViews();
                    tableLayoutBilerakUsers.addView(metodos.createHeaderRow(this));
                    scheduleView = metodos.generateArrayTableWReuniones(horarios, reuniones);
                    scheduleReuniones = metodos.generateArrayReunionesTable(reuniones);
                    fillTable(tableLayoutBilerakUsers, scheduleView);

                }
            });

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
            horarios = Parcels.unwrap(getIntent().getParcelableExtra("horariosIrakasle"));
            reuniones = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIrakasle"));
        } else if (GlobalVariables.logedUser.getTipos().getId() == 4){
            horarios = Parcels.unwrap(getIntent().getParcelableExtra("horariosIkasle"));
            reuniones = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIkasle"));
        }
        usersList = Parcels.unwrap(getIntent().getParcelableExtra("usersList"));

        /*-----------------SET VIEW ELEMENTS------------------*/
        tableLayoutBilerakUsers = findViewById(R.id.tableLayoutBilerakUsers);
        btnAtzeraBilerakUsers = findViewById(R.id.btnAtzeraBilerakUsers);
        btnCreateReunionBilerakUsers = findViewById(R.id.btnCreateReunionBilerakUsers);

        /*-----------------FILL VIEW ELEMENTS------------------*/

        tableLayoutBilerakUsers.removeAllViews();
        tableLayoutBilerakUsers.addView(metodos.createHeaderRow(this));
        scheduleView = metodos.generateArrayTableWReuniones(horarios, reuniones);
        scheduleReuniones = metodos.generateArrayReunionesTable(reuniones);
        fillTable(tableLayoutBilerakUsers, scheduleView);

        /*-----------------LISTENERS------------------*/

        btnCreateReunionBilerakUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BilerakUserActivity.this, CreateReunionActivity.class);
                intent.putExtra("usersList", Parcels.wrap(usersList));
                intent.putExtra("horarios", Parcels.wrap(horarios));
                finishCreation.launch(intent);
            }
        });

        btnAtzeraBilerakUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fillTable(TableLayout layout, String[][] schedule){
        int i;
        int j;

        for (i = 0; i < schedule.length; i++) {
            TableRow tableRow = new TableRow(this);
            for (j = 0; j < schedule[i].length; j++) {
                final String cellText = schedule[j][i]; // Save cell text for use in listener
                TextView cellTextView = new TextView(this);
                cellTextView.setText(cellText);
                cellTextView.setGravity(Gravity.CENTER);
                cellTextView.setPadding(16, 16, 16, 16);
                cellTextView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                cellTextView.setTextColor(Color.BLACK);


                if (scheduleReuniones[j][i] != null) {
                    if (scheduleReuniones[j][i].getEstado().equals("pendiente")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#FFA500"));
                    } else if (scheduleReuniones[j][i].getEstado().equals("aceptada")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#66C766"));
                    } else if (scheduleReuniones[j][i].getEstado().equals("denegada")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#FF0000"));
                    } else if (scheduleReuniones[j][i].getEstado().equals("conflicto")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#808080"));
                    }
                }

                final int rowIndex = i;  // Capture row index
                final int colIndex = j;
                // Set a click listener for each cell
                cellTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (scheduleReuniones[colIndex][rowIndex] != null) {
                            x = colIndex;
                            y = rowIndex;
                            Intent intent = new Intent(BilerakUserActivity.this, BilerakInfoActivity.class);
                            intent.putExtra("reunion", Parcels.wrap(scheduleReuniones[colIndex][rowIndex]));
                            finishDetails.launch(intent);
                        }
                    }
                });

                tableRow.addView(cellTextView);
            }
            layout.addView(tableRow);
        }

        i = 0;
        j = 0;

    }
}