package com.example.dam2_e2_t6_mpgm;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.parceler.Parcels;

import java.util.ArrayList;

import model.Horarios;

public class IkasleActivity extends AppCompatActivity {

    private ArrayList<Horarios> horarioIkasle;

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
        Log.d("horarioIkasle", horarioIkasle.toString());
        TableLayout tableLayout = findViewById(R.id.tableLayoutIkasle);

        TableRow headerRow = new TableRow(this);
        String[] headers = {"Astelehena", "Asteartea", "Asteazkena", "Osteguna", "Ostirala"};
        for (String header : headers) {
            TextView headerTextView = new TextView(this);
            headerTextView.setText(header);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setTextColor(Color.WHITE);
            headerTextView.setPadding(16, 16, 16, 16);
            headerRow.addView(headerTextView);
        }
        headerRow.setBackgroundColor(Color.parseColor("#007DC3")); // Blue header background
        tableLayout.addView(headerRow);

        Metodos metodos = new Metodos();
        String[][] schedule = metodos.generateArrayTable(horarioIkasle);

        // Add rows dynamically
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
            tableLayout.addView(tableRow);
        }
    }
}