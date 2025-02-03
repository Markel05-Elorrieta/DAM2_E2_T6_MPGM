package com.example.dam2_e2_t6_mpgm;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import callbacks.UpdateUserCallback;
import model.Users;
import model.dao.MUsers;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private Button btnBack;
    private ProfileInfoAdapter adapter;
    private Button btnChangeImg;
    private Button btnGorde;
    private ImageView img;
    private Uri argazkiUri;
    private String base;
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
        btnGorde = findViewById(R.id.btnGorde);
        img = findViewById(R.id.imgProfile);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        infoList = metodos.generateInfoProfile(this, GlobalVariables.logedUser, GlobalVariables.matriculacion);
        adapter = new ProfileInfoAdapter(infoList, this);
        recyclerView.setAdapter(adapter);

        if (GlobalVariables.logedUser.getArgazkia() != null){
            try {
                img.setImageURI(byteArrayToUri(GlobalVariables.logedUser.getArgazkia(), this));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                            android.Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 100);
                }
                kameraZabaldu();
            }
        });

        btnGorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariables.logedUser.setArgazkia(metodos.imageViewToByte(img));
               MUsers userDao = new MUsers("updateUser", GlobalVariables.logedUser.getId(),base , new UpdateUserCallback() {
                    @Override
                    public void onUpdateUser(boolean isChanged) {
                        if (isChanged) {
                            runOnUiThread(() -> {
                                Toast.makeText(ProfileActivity.this, getString(R.string.updatePhotoSuccesfully), Toast.LENGTH_SHORT).show();
                            });
                            } else {
                            runOnUiThread(() -> {
                                Toast.makeText(ProfileActivity.this, getString(R.string.updatePhotoError), Toast.LENGTH_SHORT).show();
                            });
                        }

                    }
                });
            }
        });
    }

    private void kameraZabaldu() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        argazkiUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, argazkiUri);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            img.setImageURI(argazkiUri);
            base = uriToBase64(argazkiUri);
        }
    }

    private byte[] uriToByteArray(Uri uri) {
        Log.d("uriPrueba", "uriToByteArray: " + uri);
        try {
            // Open an InputStream from the Uri
            InputStream inputStream = getContentResolver().openInputStream(uri);

            // Convert InputStream to ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            // Close the InputStream
            inputStream.close();

            // Return the byte array
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the error case
        }
    }

    public Uri byteArrayToUri(byte[] byteArray, Context context) throws IOException {
        // Create a temporary file in the app's storage
        File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg");

        // Convert byte[] to InputStream
        InputStream inputStream = new ByteArrayInputStream(byteArray);

        // Create a FileOutputStream to write the byte[] to the temp file
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4098];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, length);
        }

        // Close the streams
        inputStream.close();
        fileOutputStream.close();

        // Get the Uri for the saved file
        Uri uri = Uri.fromFile(tempFile);
        Log.d("uriPrueba", "byteArrayToUri: " + uri);
        return uri;
    }

    public String uriToBase64(Uri uri) {
        try {
            // Step 1: Open the InputStream from the Uri
            InputStream inputStream = getContentResolver().openInputStream(uri);

            // Step 2: Convert InputStream to byte[]
            byte[] byteArray = inputStreamToByteArray(inputStream);

            // Step 3: Convert byte[] to Base64 string
            String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            base64String = base64String.replace("\n", "").replace("\r", "");
            // Step 4: Close the InputStream
            inputStream.close();
            Log.d("base64Prueba", base64String);
            return base64String;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        Log.d("base64Prueba", Arrays.toString(byteArrayOutputStream.toByteArray()));
        return byteArrayOutputStream.toByteArray();
    }

    private String byteArrayToBase64(byte[] byteArray) {
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}