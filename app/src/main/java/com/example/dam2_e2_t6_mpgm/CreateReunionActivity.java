package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.parceler.Parcels;

public class CreateReunionActivity extends AppCompatActivity {
    private EditText txt_izenburuaReunion;
    private EditText txt_gaiaReunion;
    private EditText txt_dayReunion;
    private EditText txt_hourReunion;
    private EditText txt_gelaReunion;
    private Spinner s_usersReuniones;

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

        txt_izenburuaReunion = findViewById(R.id.txt_izenburuaReunion);
        txt_gaiaReunion = findViewById(R.id.txt_gaiaReunion);
        txt_dayReunion = findViewById(R.id.txt_dayReunion);
        txt_hourReunion = findViewById(R.id.txt_hourReunion);
        s_usersReuniones = findViewById(R.id.s_usersReuniones);
        txt_gelaReunion = findViewById(R.id.txt_gelaReunion);

        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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