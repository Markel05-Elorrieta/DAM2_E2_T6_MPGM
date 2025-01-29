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
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;

import model.Horarios;
import model.Ikastetxeak;
import model.Reuniones;

public class BilerakIkasleActivity extends AppCompatActivity {
    private Metodos metodos;

    private ArrayList<Reuniones> reunionesIkasle;
    private ArrayList<Ikastetxeak> ikastetxeak;
    private ArrayList<Horarios> horariosIkasle;

    private Reuniones[][] reunionesTable;

    private TableLayout tableLayoutIkasleBilerak;
    private Button btnAtzeraIkasle;
    private Button btnCreateReunion;

    /*-----------------LAUNCHER FUNCTIONS------------------*/
    private final ActivityResultLauncher<Intent> finishCreation =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {

                }
            });

    private final ActivityResultLauncher<Intent> finishDetails =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bilerak_ikasle_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*----------------- INSTANCES ------------------*/
        metodos = new Metodos();

        /*-----------------GET PARCEL ELEMENTS------------------*/
        horariosIkasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIkasle"));
        reunionesIkasle = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIkasle"));

        /*-----------------SET VIEW ELEMENTS------------------*/
        tableLayoutIkasleBilerak = findViewById(R.id.tableLayoutIkasleBilerak);

        btnAtzeraIkasle = findViewById(R.id.btnAtzeraIkasle);
        btnCreateReunion = findViewById(R.id.btnCreateReunion);

        /*-----------------FILL VIEW ELEMENTS------------------*/
        tableLayoutIkasleBilerak.addView(metodos.createHeaderRow(this));
        String[][] schedule = metodos.generateArrayTableWReuniones(horariosIkasle, reunionesIkasle);
        reunionesTable = metodos.generateArrayReunionesTable(reunionesIkasle);
        fillTable(tableLayoutIkasleBilerak, schedule);

        for (int i = 0; i < reunionesTable.length; i++) {
            for (int j = 0; j < reunionesTable[i].length; j++) {
                if (reunionesTable[i][j] == null) {
                    Log.d("pruebaReuniones","Elemento en [" + i + "][" + j + "] es null");
                } else {
                    Log.d("pruebaReuniones","Elemento en [" + i + "][" + j + "] = " + reunionesTable[i][j]);
                }
            }
        }
        /*-----------------LISTENERS------------------*/
        btnCreateReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BilerakIkasleActivity.this, CreateReunionActivity.class);
                finishCreation.launch(intent);
            }
        });

        btnAtzeraIkasle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                final int rowIndex = i;  // Capture row index
                final int colIndex = j;
                // Set a click listener for each cell
                cellTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (reunionesTable[colIndex][rowIndex] != null) {
                            Intent intent = new Intent(BilerakIkasleActivity.this, BilerakInfoActivity.class);
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