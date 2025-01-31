package com.example.dam2_e2_t6_mpgm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.parceler.Parcels;

import callbacks.AcceptBileraCallback;
import callbacks.DeclineBileraCallback;
import model.Ikastetxeak;
import model.Reuniones;
import model.dao.MReuniones;

public class BilerakInfoActivity extends AppCompatActivity {

    private Reuniones reunion;

    private TextView lbl_egoeraBilera;
    private TextView lbl_izenburuaBilera;
    private TextView lbl_gaiaBilera;
    private TextView lbl_dataBilera;
    private TextView lbl_gelaBilera;
    private TextView lbl_Norekin;

    private Button btnOnartu;
    private Button btnEzeztatu;
    private Button btnAtzeraBilerakInfo;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private MapView mapaIkuspegia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bilerak_info); // Only call this once

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the passed data from the previous activity
        reunion = Parcels.unwrap(getIntent().getParcelableExtra("reunion"));

        // Bind views to their respective widgets
        btnOnartu = findViewById(R.id.btnOnartu);
        btnEzeztatu = findViewById(R.id.btnEzeztatu);
        btnAtzeraBilerakInfo = findViewById(R.id.btnAtzeraBilera);
        lbl_egoeraBilera = findViewById(R.id.lbl_egoeraBilera);
        lbl_izenburuaBilera = findViewById(R.id.lbl_izenburuaBilera);
        lbl_gaiaBilera = findViewById(R.id.lbl_gaiaBilera);
        lbl_dataBilera = findViewById(R.id.lbl_dataBilera);
        lbl_gelaBilera = findViewById(R.id.lbl_gelaBilera);
        lbl_Norekin = findViewById(R.id.lbl_Norekin);
        mapaIkuspegia = findViewById(R.id.mapa);

        if (reunion.getEstado().equals("pendiente") || reunion.getEstado().equals("conflicto")) {
            btnOnartu.setVisibility(View.VISIBLE);
            btnEzeztatu.setVisibility(View.VISIBLE);
        } else {
            btnOnartu.setVisibility(View.GONE);
            btnEzeztatu.setVisibility(View.GONE);
        }

        // Set the text for the views based on the reunion object
        lbl_egoeraBilera.setText(reunion.getEstado());
        lbl_izenburuaBilera.setText(reunion.getTitulo());
        lbl_gaiaBilera.setText(reunion.getAsunto());
        lbl_dataBilera.setText(reunion.getFecha().toString());
        lbl_gelaBilera.setText(reunion.getAula());

        if (GlobalVariables.logedUser.getTipos().getId() == 3) {
            lbl_Norekin.setText(reunion.getUsersByAlumnoId().getNombre() + " " + reunion.getUsersByAlumnoId().getApellidos());
        } else {
            lbl_Norekin.setText(reunion.getUsersByProfesorId().getNombre() + " " + reunion.getUsersByProfesorId().getApellidos());
        }

        // Initialize the map and set the zoom and center
        Configuration.getInstance().setUserAgentValue(getPackageName());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapaIkuspegia.setMultiTouchControls(true);
        // Add markers for ikastetxeak
        GeoPoint g1;
        Marker txintxeta;
        for (Ikastetxeak ikastetxeak : GlobalVariables.ikastetxeak) {
            if (ikastetxeak.getCCEN() == Integer.parseInt(reunion.getIdCentro())) {
                g1 = new GeoPoint(ikastetxeak.getLONGITUD(), ikastetxeak.getLATITUD());
                mapaIkuspegia.getController().setZoom(17.0); // Set the zoom level
                mapaIkuspegia.getController().setCenter(g1); // Center the map

                txintxeta = new Marker(mapaIkuspegia);
                txintxeta.setPosition(g1);
                txintxeta.setTitle(ikastetxeak.getNOM());
                mapaIkuspegia.getOverlays().add(txintxeta);
            }
        }

        // Set OnClickListener for the "back" button
        btnAtzeraBilerakInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        btnOnartu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MReuniones mReuniones = new MReuniones("acceptBilera", reunion.getIdReunion(), new AcceptBileraCallback() {
                    @Override
                    public void onAcceptBilera() {
                        Intent intent = new Intent();
                        reunion.setEstado("aceptada");
                        intent.putExtra("reunionNewEstado", Parcels.wrap(reunion));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

            }
        });

        btnEzeztatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MReuniones mReuniones = new MReuniones("declineBilera", reunion.getIdReunion(), new DeclineBileraCallback() {
                    @Override
                    public void onDeclineBilera() {
                        Intent intent = new Intent();
                        reunion.setEstado("denegada");
                        intent.putExtra("reunionNewEstado", Parcels.wrap(reunion));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });
    }
}
