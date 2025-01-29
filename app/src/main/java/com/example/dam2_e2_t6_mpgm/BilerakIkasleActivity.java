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

    private TableLayout tableLayoutIkasleBilerak;
    private Button btnAtzeraIkasle;
    private Button btnCreateReunion;


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
        metodos = new Metodos();

        horariosIkasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIkasle"));
        reunionesIkasle = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIkasle"));

        tableLayoutIkasleBilerak = findViewById(R.id.tableLayoutIkasleBilerak);
        btnAtzeraIkasle = findViewById(R.id.btnAtzeraIkasle);
        btnCreateReunion = findViewById(R.id.btnCreateReunion);

        tableLayoutIkasleBilerak.addView(metodos.createHeaderRow(this));
        String[][] schedule = metodos.generateArrayTableWReuniones(horariosIkasle, reunionesIkasle);

        Log.d("holaaa","📅 Weekly Schedule:");
        for (int i = 0; i < schedule.length; i++) {
            Log.d("holaaa","Day " + i + ": ");
            for (int j = 0; j < schedule[i].length; j++) {
                Log.d("holaaa",(schedule[i][j] != null ? schedule[i][j] : "FREE") + "\t");
            }
            Log.d("holaaa","\n");
        }

        fillTable(tableLayoutIkasleBilerak, schedule);


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
        for (int i = 0; i < schedule.length; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < schedule[i].length; j++) {
                final String cellText = schedule[j][i]; // Save cell text for use in listener
                TextView cellTextView = new TextView(this);
                cellTextView.setText(cellText);
                cellTextView.setGravity(Gravity.CENTER);
                cellTextView.setPadding(16, 16, 16, 16);
                cellTextView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                cellTextView.setTextColor(Color.BLACK);

                // Set a click listener for each cell
                cellTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!cellText.isEmpty()) {
                            Toast.makeText(BilerakIkasleActivity.this, "Clicked: " + cellText, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BilerakIkasleActivity.this, "Empty cell clicked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                tableRow.addView(cellTextView);
            }
            layout.addView(tableRow);
        }
    }
}