package com.example.dam2_e2_t6_mpgm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import callbacks.ScheduleTeacherCallback;
import model.Horarios;
import model.Users;
import model.dao.MHorarios;

public class IkasleActivity extends AppCompatActivity {

    private ArrayList<Horarios> horarioIkasle;
    private ArrayList<Users> irakasleak;
    private ArrayList<Horarios> horarioIrakasle;
    private int sPos;
    private TableLayout tableLayoutIkasle;
    private TableLayout tableLayoutIrakasle;
    private Button btnFilter;
    private Button btnLogout;
    private Button btnProfileIkasle;
    private Button btnBilerakIkusiIkasle;

    private final ActivityResultLauncher<Intent> returnProfile =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            });

    private final ActivityResultLauncher<Intent> returnFromBilerak =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ikasle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        horarioIkasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIkasle"));
        irakasleak = Parcels.unwrap(getIntent().getParcelableExtra("irakasleak"));
        horarioIrakasle = Parcels.unwrap(getIntent().getParcelableExtra("horarioIrakasle"));
        sPos = getIntent().getIntExtra("sPos", 0);

        Log.d("horarioIkasle", horarioIkasle.toString());

        tableLayoutIkasle = findViewById(R.id.tableLayoutIkasle);
        tableLayoutIrakasle = findViewById(R.id.tableLayoutIkasleIrakasle);
        Spinner s_irakasleak = findViewById(R.id.s_irakasle);
        btnFilter = (Button) findViewById(R.id.btn_filtratuHorario);
        btnLogout = findViewById(R.id.btnLogoutIkasle);
        btnProfileIkasle = findViewById(R.id.btnProfileIkasle);
        btnBilerakIkusiIkasle = findViewById(R.id.btnBilerakIkusiIkasle);

        Metodos metodos = new Metodos();
        String[] opciones = metodos.getNames(irakasleak);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_irakasleak.setAdapter(adapter);
        s_irakasleak.setSelection(sPos);

        s_irakasleak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                 }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IkasleActivity.this, IkasleActivity.class);

                MHorarios horariosDao = new MHorarios("scheduleTeacher", irakasleak.get(sPos).getId(), new ScheduleTeacherCallback() {
                    @Override
                    public void onScheduleTeacher(ArrayList<Horarios> newHorarioIrakasle) {
                        runOnUiThread(() -> {
                            horarioIrakasle = newHorarioIrakasle; // Update the data
                            refreshTableLayout(tableLayoutIrakasle, horarioIrakasle);
                        });
                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(() -> {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish(); // Finaliza la actividad correctamente
                });
            }
        });

        btnProfileIkasle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IkasleActivity.this, ProfileActivity.class);
                returnProfile.launch(intent);
            }
        });

        btnBilerakIkusiIkasle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IkasleActivity.this, BilerakActivity.class);
                returnFromBilerak.launch(intent);
            }
        });

        tableLayoutIkasle.addView(createHeaderRow());
        tableLayoutIrakasle.addView(createHeaderRow());

        String[][] scheduleIkasle = metodos.generateArrayTable(horarioIkasle);
        String[][] scheduleIrakasle = metodos.generateArrayTable(horarioIrakasle);

        fillTable(tableLayoutIkasle, scheduleIkasle);
        fillTable(tableLayoutIrakasle, scheduleIrakasle);
        // Add rows dynamically

    }

    private TableRow createHeaderRow() {
        TableRow headerRow = new TableRow(this);
        String[] headersName = {"Astelehena", "Asteartea", "Asteazkena", "Osteguna", "Ostirala"};
        for (String header : headersName) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(header);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setTextColor(Color.WHITE);
            headerTextView.setPadding(16, 16, 16, 16);
            headerRow.addView(headerTextView);
        }
        headerRow.setBackgroundColor(Color.parseColor("#007DC3")); // Blue header background
        return headerRow;
    }

    private void fillTable(TableLayout layout, String[][] schedule){
        for (int i = 0; i < schedule.length; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < schedule[i].length; j++) {
                final String cellText = schedule[i][j]; // Save cell text for use in listener
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
                            Toast.makeText(IkasleActivity.this, "Clicked: " + cellText, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(IkasleActivity.this, "Empty cell clicked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                tableRow.addView(cellTextView);
            }
            layout.addView(tableRow);
        }
    }

    private void refreshTableLayout(TableLayout layout, ArrayList<Horarios> horarios) {
        Metodos metodos = new Metodos();
        String[][] newSchedule = metodos.generateArrayTable(horarios);

        // Clear all rows except the header
        layout.removeViews(1, layout.getChildCount() - 1);

        // Add new rows dynamically
        fillTable(layout, newSchedule);
    }
}