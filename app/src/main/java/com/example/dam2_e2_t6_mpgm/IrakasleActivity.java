package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;

import callbacks.UsersByTeacherCallback;
import callbacks.UsersFilteredCallback;
import model.Horarios;
import model.Users;
import model.dao.MUsers;

public class IrakasleActivity extends AppCompatActivity {

    private ArrayList<Horarios> horariosIrakasle;
    private ArrayList<Users> ikasleList;

    private IkasleListAdapter adapter;
    private EditText et_filterZiklo;
    private EditText et_filterIkasturte;
    private Button btnLogout;
    private Button btnProfileIrakasle;

    private final ActivityResultLauncher<Intent> returnProfile =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_irakasle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        horariosIrakasle = Parcels.unwrap(getIntent().getParcelableExtra("horariosIrakasle"));
        ikasleList = Parcels.unwrap(getIntent().getParcelableExtra("ikasleList"));

        TableLayout tableLayout = findViewById(R.id.tableLayoutIrakasle);
        RecyclerView recyclerView = findViewById(R.id.rv_ikasleList);
        et_filterZiklo = findViewById(R.id.et_filterZikloa);
        et_filterIkasturte = findViewById(R.id.et_filterIkasturtea);
        btnLogout = findViewById(R.id.btnLogoutIrakasle);
        btnProfileIrakasle = findViewById(R.id.btnProfileIrakasle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IkasleListAdapter(ikasleList, this);
        recyclerView.setAdapter(adapter);

        et_filterZiklo.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                    event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER &&
                            event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                doFilter();
                return true;
            }
            return false;
        });

        et_filterIkasturte.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                    event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER &&
                            event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                doFilter();
                return true;
            }
            return false;
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish(); // Finaliza la actividad correctamente
            }
        });

        btnProfileIrakasle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IrakasleActivity.this, ProfileActivity.class);
                returnProfile.launch(intent);
            }
        });


        /*--------------------------------------------*/

        // Add the header row
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
        String[][] schedule = metodos.generateArrayTable(horariosIrakasle);

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
                            Toast.makeText(IrakasleActivity.this, "Clicked: " + cellText, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(IrakasleActivity.this, "Empty cell clicked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                tableRow.addView(cellTextView);
            }
            tableLayout.addView(tableRow);
        }
    }

    private void doFilter(){
        if (et_filterIkasturte.getText().toString().isEmpty() && et_filterZiklo.getText().toString().isEmpty()) {
            MUsers usersDao = new MUsers("usersByTeacher", GlobalVariables.logedUser.getId(), new UsersByTeacherCallback() {
                @Override
                public void onUserByTeacher(ArrayList<Users> users) {
                    runOnUiThread(() -> {
                        adapter.setIkasleList(users);
                        adapter.notifyDataSetChanged();
                    });
                }
            });

        } else {
            String auxZiklo;
            String auxIkasturte;
            if (et_filterZiklo.getText().toString().isEmpty()) {
                auxZiklo = "0";
            } else {
                auxZiklo = et_filterZiklo.getText().toString();
            }

            if (et_filterIkasturte.getText().toString().isEmpty()) {
                auxIkasturte = "0";
            } else {
                auxIkasturte = et_filterIkasturte.getText().toString();
            }

            MUsers mUsers = new MUsers("usersFiltered", auxZiklo, auxIkasturte, new UsersFilteredCallback() {
                @Override
                public void onUsersFiltered(ArrayList<Users> users) {
                    runOnUiThread(() -> {
                        adapter.setIkasleList(users);
                        adapter.notifyDataSetChanged();
                    });
                }
            });
        }
    }
}