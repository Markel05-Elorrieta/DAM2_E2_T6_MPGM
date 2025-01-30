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
import model.Reuniones;
import model.Users;

public class BilerakIrakasleActivity extends AppCompatActivity {
    private Metodos metodos;

    private ArrayList<Users> studentsList;
    private ArrayList<Horarios> horariosIrakasle;
    private ArrayList<Reuniones> reunionesIrakasle;

    private Reuniones[][] reunionesTable;

    private TableLayout tableLayoutIrakasleBilerak;
    private Button btnCreateReunionSortu;
    private Button btnAtzera;

    private int x;
    private int y;

    private final ActivityResultLauncher<Intent> finishCreation =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Log.d("llegoalfinish", result.getResultCode() + "");
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Reuniones newReunion = Parcels.unwrap(data.getParcelableExtra("reunion"));
                    Log.d("pruebaReuniones","Reunion creada: " + newReunion.toString());
                    reunionesIrakasle.add(newReunion);
                    String[][] schedule = metodos.generateArrayTableWReuniones(horariosIrakasle, reunionesIrakasle);
                    reunionesTable = metodos.generateArrayReunionesTable(reunionesIrakasle);
                    fillTable(tableLayoutIrakasleBilerak, schedule);
                }
            });

    private final ActivityResultLauncher<Intent> finishDetails =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() != RESULT_CANCELED) {
                    Reuniones reunionUpdated = Parcels.unwrap(result.getData().getParcelableExtra("reunionUpdate"));
                    for (Reuniones reunion : reunionesIrakasle) {
                        if (reunion.getIdReunion() == reunionUpdated.getIdReunion()) {
                            reunion.setEstado(reunionUpdated.getEstado());
                            break;
                        }
                    }
                    String[][] schedule = metodos.generateArrayTableWReuniones(horariosIrakasle, reunionesIrakasle);
                    reunionesTable = metodos.generateArrayReunionesTable(reunionesIrakasle);
                    fillTable(tableLayoutIrakasleBilerak, schedule);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bilerak_irakasle_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        metodos = new Metodos();

        horariosIrakasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIrakasle"));
        reunionesIrakasle = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIrakasle"));
        studentsList = Parcels.unwrap(getIntent().getParcelableExtra("ikasleak"));

        tableLayoutIrakasleBilerak = findViewById(R.id.tableLayoutIrakasleBilerak);
        btnCreateReunionSortu = findViewById(R.id.btnCreateReunionSortu);
        btnAtzera = findViewById(R.id.btnAtzeraIrakasle);

        tableLayoutIrakasleBilerak.addView(metodos.createHeaderRow(this));
        String[][] schedule = metodos.generateArrayTableWReuniones(horariosIrakasle, reunionesIrakasle);
        reunionesTable = metodos.generateArrayReunionesTable(reunionesIrakasle);
        fillTable(tableLayoutIrakasleBilerak, schedule);

        btnCreateReunionSortu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BilerakIrakasleActivity.this, CreateReunionActivity.class);
                intent.putExtra("irakasleak", Parcels.wrap(studentsList));
                finishCreation.launch(intent);
            }
        });

        btnAtzera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fillTable(TableLayout layout, String[][] schedule){
        layout.removeAllViews();

        int i, j;

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


                if (reunionesTable[j][i] != null) {
                    if (reunionesTable[j][i].getEstado().equals("pendiente")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#FFA500"));
                    } else if (reunionesTable[j][i].getEstado().equals("aceptada")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#66C766"));
                    } else if (reunionesTable[j][i].getEstado().equals("denegada")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#FF0000"));
                    } else if (reunionesTable[j][i].getEstado().equals("conflicto")) {
                        cellTextView.setBackgroundColor(Color.parseColor("#808080"));
                    }
                }

                final int rowIndex = i;  // Capture row index
                final int colIndex = j;
                // Set a click listener for each cell
                cellTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reunionesTable[colIndex][rowIndex] != null) {
                            x = colIndex;
                            y = rowIndex;
                            Intent intent = new Intent(BilerakIrakasleActivity.this, BilerakInfoActivity.class);
                            intent.putExtra("reunion", Parcels.wrap(reunionesTable[colIndex][rowIndex]));
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