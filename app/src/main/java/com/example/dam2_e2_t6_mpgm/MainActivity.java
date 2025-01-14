package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dam2_e2_t6_mpgm.LocalStorage.LocalDBDao;

import java.io.IOException;
import java.net.Socket;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocalDBDao localDBDao = new LocalDBDao(this);

    private final ActivityResultLauncher<Intent> logout =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText txt_user = (EditText) findViewById(R.id.txt_user);
        EditText txt_password = (EditText) findViewById(R.id.txt_password);
        Button btn_login = (Button) findViewById(R.id.btn_login);

        Spinner hizkuntza = (Spinner) findViewById(R.id.s_language);
        String[] opciones = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hizkuntza.setAdapter(adapter);

        hizkuntza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                String selectedOption = parent.getItemAtPosition(position).toString();

                switch (position) {
                    case 0:
                            localDBDao.changeLanguage("eu");

                        break;
                    case 1:
                            localDBDao.changeLanguage("en");

                        break;
                    case 2:
                            localDBDao.changeLanguage("es");

                        break;
                    default:
                            localDBDao.changeLanguage("eu");
                }

                Toast.makeText(MainActivity.this, "Seleccionaste: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        recreate();
    }
}