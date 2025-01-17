package com.example.dam2_e2_t6_mpgm;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import callbacks.ChangePwdCallback;
import model.dao.UsersDao;

public class Metodos {


    public void pedirCorreo(Context context) {
        // Crear el AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Titulus");

        // Crear un LinearLayout para contener los EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10); // Ajusta el padding si deseas espaciar el contenido

        // Crear el primer EditText
        final EditText email = new EditText(context);
        email.setHint("mete correus"); // Hint para el primer campo de texto
        layout.addView(email); // Agregar el primer EditText al layout

        // Configurar el layout en el AlertDialog
        builder.setView(layout);

        // Configurar los botones
        builder.setPositiveButton("Un button", null); // Usar null para evitar el cierre automático

        // Crear el AlertDialog
        AlertDialog dialog = builder.create();

        // Establecer un listener para el botón "Aceptar"
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                UsersDao usersDao = new UsersDao("changePwd", email.getText().toString(), new ChangePwdCallback() {
                    @Override
                    public void onChangePwd(boolean result) {
                        Log.d("loginProba", result + "");
                        if (result) {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(() -> {
                                Toast.makeText(context, "Contraseña modificada, revisa el correo!", Toast.LENGTH_SHORT).show();
                            });
                            dialog.dismiss();
                        } else {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(() -> {
                                Toast.makeText(context, "Error al modificar la contraseña", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
                usersDao.start();
            });
        });
        // Mostrar el diálogo
        dialog.show();
    }
}
