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
import com.example.dam2_e2_t6_mpgm.model.Users;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocalDBDao localDBDao = new LocalDBDao(this);

    private TextView lbl_user;
    private TextView lbl_password;
    private EditText txt_user;
    private EditText txt_password;
    private TextView lbl_forgotPassword;
    private TextView lbl_clickHere;
    private Button btn_login;
    private Spinner hizkuntza;
    private Button btn_changeLanguage;

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

        new Thread(() -> {
            try {
                // Connect to the server via socket
                Socket socket = new Socket("10.5.104.34", 23456);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                // Send request to server (assuming 'user/1' is a valid query)
                out.println("user/1");

                // Read the object sent from the server (now the mobile app has access to the Users class)
                Users response = (Users) ois.readObject();

                // Log the response
                Log.d("UnasPruebas", response.toString());

                // Close the socket connection
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            String kode = localDBDao.getLanguage();
            setLocale(kode);
        }catch (Exception e){
            localDBDao.addLanguage("eu");
        }

        lbl_user = (TextView) findViewById(R.id.lbl_user);
        lbl_password = (TextView) findViewById(R.id.lbl_password);
        txt_user = (EditText) findViewById(R.id.txt_user);
        txt_password = (EditText) findViewById(R.id.txt_password);
        lbl_forgotPassword = (TextView) findViewById(R.id.lbl_forgotPassword);
        lbl_clickHere = (TextView) findViewById(R.id.lbl_clickHere);
        btn_login = (Button) findViewById(R.id.btn_login);
        hizkuntza = (Spinner) findViewById(R.id.s_language);
        btn_changeLanguage = (Button) findViewById(R.id.btn_changeLanguage);

        String[] opciones = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hizkuntza.setAdapter(adapter);

        lbl_clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "pide correus", Toast.LENGTH_SHORT).show();

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do login, depende de el user hace una cosa o otra
            }
        });

        hizkuntza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            localDBDao.changeLanguage("eu"); // Euskara
                            break;
                        case 1:
                            localDBDao.changeLanguage("en"); // English
                            break;
                        case 2:
                            localDBDao.changeLanguage("es"); // Español
                            break;
                        default:
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale(localDBDao.getLanguage());
                recreateView();
            }
        });

    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void recreateView(){
        lbl_user.setText(R.string.user);
        lbl_password.setText(R.string.password);
        txt_user.setHint(R.string.user);
        txt_password.setHint(R.string.password);
        lbl_forgotPassword.setText(R.string.forgotPassword);
        lbl_clickHere.setText(R.string.clickHere);
        btn_login.setText(R.string.login);

    }
}