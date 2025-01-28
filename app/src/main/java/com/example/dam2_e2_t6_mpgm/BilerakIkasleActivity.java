package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import model.Ikastetxeak;
import model.Reuniones;

public class BilerakIkasleActivity extends AppCompatActivity {
    private ArrayList<Reuniones> reunionesIkasle;
    private ArrayList<Ikastetxeak> ikastetxeak;

    private RecyclerView rvReunionesIkasle;
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

        reunionesIkasle = Parcels.unwrap(getIntent().getParcelableExtra("reunionesIkasle"));
        //ikastetxeak = Parcels.unwrap(getIntent().getParcelableExtra("ikastetxeak"));

        Log.d("ikastetxeak", GlobalVariables.ikastetxeak.toString());
        btnAtzeraIkasle = findViewById(R.id.btnAtzeraIkasle);
        btnCreateReunion = findViewById(R.id.btnCreateReunion);

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
}