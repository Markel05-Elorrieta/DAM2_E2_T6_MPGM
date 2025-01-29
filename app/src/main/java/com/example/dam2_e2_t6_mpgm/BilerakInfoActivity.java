package com.example.dam2_e2_t6_mpgm;

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

import model.Ikastetxeak;
import model.Reuniones;

public class BilerakInfoActivity extends AppCompatActivity {

    private Reuniones reunion;

    private TextView lbl_egoeraBilera;
    private TextView lbl_izenburuaBilera;
    private TextView lbl_gaiaBilera;
    private TextView lbl_dataBilera;
    private TextView lbl_gelaBilera;
    private TextView lbl_Norekin;

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
        btnAtzeraBilerakInfo = findViewById(R.id.btnAtzeraBilera);
        lbl_egoeraBilera = findViewById(R.id.lbl_egoeraBilera);
        lbl_izenburuaBilera = findViewById(R.id.lbl_izenburuaBilera);
        lbl_gaiaBilera = findViewById(R.id.lbl_gaiaBilera);
        lbl_dataBilera = findViewById(R.id.lbl_dataBilera);
        lbl_gelaBilera = findViewById(R.id.lbl_gelaBilera);
        lbl_Norekin = findViewById(R.id.lbl_Norekin);
        mapaIkuspegia = findViewById(R.id.mapa);

        // Set the text for the views based on the reunion object
        lbl_egoeraBilera.setText(reunion.getEstado());
        lbl_izenburuaBilera.setText(reunion.getTitulo());
        lbl_gaiaBilera.setText(reunion.getAsunto());
        lbl_dataBilera.setText(reunion.getFecha().toString());
        lbl_gelaBilera.setText(reunion.getAula());
        lbl_Norekin.setText(reunion.getUsersByProfesorId().getNombre() + " " + reunion.getUsersByProfesorId().getApellidos());

        // Set OnClickListener for the "back" button
        btnAtzeraBilerakInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonClick", "Finish called");
                finish(); // This should finish the current activity
            }
        });

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
    }
}
