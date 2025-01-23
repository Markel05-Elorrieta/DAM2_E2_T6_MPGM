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
import android.widget.ImageView;
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


import com.bumptech.glide.Glide;
import com.example.dam2_e2_t6_mpgm.localStorageDB.LocalDBDao;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Locale;

import callbacks.GetTeachersCallback;
import callbacks.LoginAndroidCallback;
import callbacks.ScheduleStudentCallback;
import callbacks.ScheduleTeacherCallback;
import callbacks.UsersByTeacherCallback;
import model.Horarios;
import model.Users;
import model.dao.MHorarios;
import model.dao.MUsers;

public class MainActivity extends AppCompatActivity {

    private LocalDBDao localDBDao = new LocalDBDao(this);
    private String lan = "eu";

    private ImageView img_logo;
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
                GlobalVariables.logedUser = null;
                txt_user.setText("");
                txt_password.setText("");
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

        img_logo = (ImageView) findViewById(R.id.gifLogo);
        lbl_user = (TextView) findViewById(R.id.lbl_user);
        lbl_password = (TextView) findViewById(R.id.lbl_password);
        txt_user = (EditText) findViewById(R.id.txt_user);
        txt_password = (EditText) findViewById(R.id.txt_password);
        lbl_clickHere = (TextView) findViewById(R.id.lbl_clickHere);
        lbl_forgotPassword = (TextView) findViewById(R.id.lbl_forgotPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        hizkuntza = (Spinner) findViewById(R.id.s_irakasle);
        btn_changeLanguage = (Button) findViewById(R.id.btn_changeLanguage);

        String[] opciones = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hizkuntza.setAdapter(adapter);

        /* GIF NO WORK*/

        Glide.with(this)
                .asGif()
                .load(R.drawable.elorrieta_logo)// Cambia "tu_gif" por el nombre del archivo GIF
                .into(img_logo);


        try {
            String kode = localDBDao.getLanguage();
            if (kode.equals("eu")) {
                hizkuntza.setSelection(0);
            } else if (kode.equals("en")) {
                hizkuntza.setSelection(1);
            } else {
                hizkuntza.setSelection(2);
            }
            setLocale(kode);
        }catch (Exception e){
            localDBDao.addLanguage("eu");
            setLocale("eu");
        }

        lbl_clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Metodos m = new Metodos();
                m.pedirCorreo(MainActivity.this);
                Toast.makeText(MainActivity.this, "pide correus", Toast.LENGTH_SHORT).show();
            }
        });

        txt_password.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                    event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER &&
                            event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                doLogin();
                return true;
            }
            return false;
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        hizkuntza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        lan = "eu";
                        break;
                    case 1:
                        lan = "en";
                        break;
                    case 2:
                        lan = "es";
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
                localDBDao.changeLanguage(lan);
                setLocale(localDBDao.getLanguage());
            }
        });

    }

    private void doLogin(){
        MUsers usersDao = new MUsers("loginAndroid", txt_user.getText().toString(), txt_password.getText().toString(), new LoginAndroidCallback() {
            @Override
            public void onLoginAndroid(boolean isLogin) {

                if (isLogin) {
                    if (GlobalVariables.logedUser.getTipos().getId() == 3) {

                        Intent intent = new Intent(MainActivity.this, IrakasleActivity.class);

                        MHorarios horariosDao = new MHorarios("scheduleTeacher", GlobalVariables.logedUser.getId(), new ScheduleTeacherCallback() {
                            @Override
                            public void onScheduleTeacher(ArrayList<Horarios> horario) {
                                Log.d("loginProba", horario + "");
                                intent.putExtra("horariosIrakasle", Parcels.wrap(horario));

                                MUsers usersDao = new MUsers("usersByTeacher", GlobalVariables.logedUser.getId(), new UsersByTeacherCallback() {
                                    @Override
                                    public void onUserByTeacher(ArrayList<Users> users) {
                                        Log.d("loginProba", users + "");
                                        intent.putExtra("ikasleList", Parcels.wrap(users));
                                        logout.launch(intent);
                                    }
                                });
                            }
                        });
                    } else {
                        Intent intent = new Intent(MainActivity.this, IkasleActivity.class);

                        MUsers usersDao = new MUsers("scheduleStudent", GlobalVariables.logedUser.getId(), new ScheduleStudentCallback() {
                            @Override
                            public void onScheduleStudent(ArrayList<Horarios> horario) {
                                intent.putExtra("horariosIkasle", Parcels.wrap(horario));

                                MUsers usersDao1 = new MUsers("getTeachers", new GetTeachersCallback() {
                                    @Override
                                    public void onTeachersCallback(ArrayList<Users> irakasleak) {
                                        intent.putExtra("irakasleak", Parcels.wrap(irakasleak));

                                        MHorarios horariosDao = new MHorarios("scheduleTeacher", irakasleak.get(0).getId(), new ScheduleTeacherCallback() {
                                            @Override
                                            public void onScheduleTeacher(ArrayList<Horarios> horarioIrakasle) {
                                                intent.putExtra("sPos", Parcels.wrap(0));
                                                intent.putExtra("horarioIrakasle", Parcels.wrap(horarioIrakasle));
                                                logout.launch(intent);
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        recreateView();
    }

    private void recreateView(){
        lbl_user.setText(R.string.user);
        lbl_password.setText(R.string.password);
        txt_user.setHint(R.string.user);
        txt_password.setHint(R.string.password);
        lbl_forgotPassword.setText(R.string.forgotPassword);
        lbl_clickHere.setText(R.string.clickHere);
        btn_login.setText(R.string.login);

        String[] opciones = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String kode = localDBDao.getLanguage();
        hizkuntza.setAdapter(adapter);
        if (kode.equals("eu")) {
            hizkuntza.setSelection(0);
        } else if (kode.equals("en")) {
            hizkuntza.setSelection(1);
        } else {
            hizkuntza.setSelection(2);
        }
    }
}