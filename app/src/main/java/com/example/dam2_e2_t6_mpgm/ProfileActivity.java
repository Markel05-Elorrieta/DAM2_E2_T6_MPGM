package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private Button btnBack;
    private ProfileInfoAdapter adapter;
    private Button btnChangeImg;
    private ImageView img;
    ArrayList<String> infoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Metodos metodos = new Metodos();

        btnBack = findViewById(R.id.btnBack);
        RecyclerView recyclerView = findViewById(R.id.rv_infoProfile);
        btnChangeImg = findViewById(R.id.btnChangeImg);
        img = findViewById(R.id.imgProfile);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoList = metodos.generateInfoProfile(GlobalVariables.logedUser, GlobalVariables.matriculacion);
        adapter = new ProfileInfoAdapter(infoList, this);
        recyclerView.setAdapter(adapter);

        if (GlobalVariables.logedUser.getArgazkia() != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(GlobalVariables.logedUser.getArgazkia(), 0, GlobalVariables.logedUser.getArgazkia().length);
            img.setImageBitmap(bitmap);
        } else {
            img.setImageResource(R.drawable.defaultimg);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btnChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}