package com.example.dam2_e2_t6_mpgm;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import callbacks.ChangePwdCallback;
import model.Horarios;
import model.Ikastetxeak;
import model.Matriculaciones;
import model.Reuniones;
import model.Users;
import model.dao.MUsers;

public class Metodos {
    public void pedirCorreo(Context context) {
        // Crear el AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.titleResetPwd)); // Título del AlertDialog

        // Crear un LinearLayout para contener los EditText
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10); // Ajusta el padding si deseas espaciar el contenido

        // Crear el primer EditText
        final EditText email = new EditText(context);
        email.setHint(context.getString(R.string.email)); // Hint para el primer campo de texto
        layout.addView(email); // Agregar el primer EditText al layout

        // Configurar el layout en el AlertDialog
        builder.setView(layout);

        // Configurar los botones
        builder.setPositiveButton(context.getString(R.string.reset), null); // Usar null para evitar el cierre automático

        // Crear el AlertDialog
        AlertDialog dialog = builder.create();

        // Establecer un listener para el botón "Aceptar"
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {

                // Comprobar si el campo de texto está vacío
                if (email.getText().toString().isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.errorEmailEmpty), Toast.LENGTH_SHORT).show();
                    return;
                }

                MUsers usersDao = new MUsers("changePwd", email.getText().toString(), new ChangePwdCallback() {
                    @Override
                    public void onChangePwd(boolean result) {
                        if (result) {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(() -> {
                                Toast.makeText(context, context.getString(R.string.titleResetPwdSuccess), Toast.LENGTH_SHORT).show();
                            });
                            dialog.dismiss();
                        } else {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(() -> {
                                Toast.makeText(context, context.getString(R.string.titleResetPwdError), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            });
        });
        // Mostrar el diálogo
        dialog.show();
    }

    public String[][] generateArrayTable(ArrayList<Horarios> horarios) {
        Metodos metodos = new Metodos();
        String[][] schedule = new String[5][5];

        int x = -1;
        int y = -1;

        for (Horarios h : horarios) {
            switch (h.getId().getDia()) {
                case "L/A":
                    x = 0;
                    break;
                case "M/A":
                    x = 1;
                    break;
                case "X":
                    x = 2;
                    break;
                case "J/O":
                    x = 3;
                    break;
                case "V/O":
                    x = 4;
                    break;
            }

            switch (Character.getNumericValue(h.getId().getHora())) {
                case 1:
                    y = 0;
                    break;
                case 2:
                    y = 1;
                    break;
                case 3:
                    y = 2;
                    break;
                case 4:
                    y = 3;
                    break;
                case 5:
                    y = 4;
                    break;
            }

            schedule[x][y] = metodos.nameToCode(h.getModulos().getNombre());
        }

        return schedule;
    }

    public String[][] generateArrayTableWReuniones(ArrayList<Horarios> horarios, ArrayList<Reuniones> reuniones) {
        String[][] schedule = new String[5][5];
        Metodos metodos = new Metodos();

        int x = -1;
        int y = -1;

        for (Horarios h : horarios) {
            switch (h.getId().getDia()) {
                case "L/A":
                    x = 0;
                    break;
                case "M/A":
                    x = 1;
                    break;
                case "X":
                    x = 2;
                    break;
                case "J/O":
                    x = 3;
                    break;
                case "V/O":
                    x = 4;
                    break;
            }


            switch (Character.getNumericValue(h.getId().getHora())) {
                case 1:
                    y = 0;
                    break;
                case 2:
                    y = 1;
                    break;
                case 3:
                    y = 2;
                    break;
                case 4:
                    y = 3;
                    break;
                case 5:
                    y = 4;
                    break;
            }
            schedule[x][y] = metodos.nameToCode(h.getModulos().getNombre());

        }

        for (Reuniones r : reuniones) {
            if(isInCurrentWeek(r.getFecha())) {
                switch (r.getFecha().getDay()){
                    case 1:
                        x = 0;
                        break;
                    case 2:
                        x = 1;
                        break;
                    case 3:
                        x = 2;
                        break;
                    case 4:
                        x = 3;
                        break;
                    case 5:
                        x = 4;
                        break;
                }
                switch (r.getFecha().getHours()){

                    case 8:
                        y = 0;
                        break;
                    case 9:
                        y = 1;
                        break;
                    case 10:
                        y = 2;
                        break;
                    case 11:
                        y = 3;
                        break;
                    case 12:
                        y = 4;
                        break;
                }

                schedule[x][y] = schedule[x][y] + "\n" + r.getTitulo();
            }
        }

        return schedule;
    }

    public Reuniones[][] generateArrayReunionesTable(ArrayList<Reuniones> reuniones) {
        Metodos metodos = new Metodos();

        Reuniones[][] reunionesArray = new Reuniones[5][5];
        int x = -1;
        int y = -1;

        for (Reuniones r : reuniones) {
            if (metodos.isInCurrentWeek(r.getFecha())) {
                switch (r.getFecha().getDay()) {
                    case 1:
                        x = 0;
                        break;
                    case 2:
                        x = 1;
                        break;
                    case 3:
                        x = 2;
                        break;
                    case 4:
                        x = 3;
                        break;
                    case 5:
                        x = 4;
                        break;
                }

                switch (r.getFecha().getHours()) {

                    case 8:
                        y = 0;
                        break;
                    case 9:
                        y = 1;
                        break;
                    case 10:
                        y = 2;
                        break;
                    case 11:
                        y = 3;
                        break;
                    case 12:
                        y = 4;
                        break;
                }
                Log.d("errorBileras", x + " " + y);
                reunionesArray[x][y] = r;
            }
        }
        return reunionesArray;
    }

    public boolean isInCurrentWeek(Timestamp timestamp) {
        Calendar timestampCalendar = Calendar.getInstance();
        timestampCalendar.setTime(timestamp);

        Calendar now = Calendar.getInstance();

        int currentWeek = now.get(Calendar.WEEK_OF_YEAR);
        int currentYear = now.get(Calendar.YEAR);

        int timestampWeek = timestampCalendar.get(Calendar.WEEK_OF_YEAR);
        int timestampYear = timestampCalendar.get(Calendar.YEAR);

        return (timestampWeek == currentWeek) && (timestampYear == currentYear);
    }

    public int charToInt(char c) {
        return c - '1';
    }

    public String[] getNames (ArrayList<Users> users){
        String[] names = new String[users.size()];

        for (int i = 0; i < users.size(); i++) {
            names[i] = users.get(i).getNombre();
        }

        return names;
    }

    public ArrayList<String> generateInfoProfile(Context context, Users user, Matriculaciones matriculaciones) {
        ArrayList<String> info = new ArrayList<>();
            info.add(context.getString(R.string.dni) + ": " + user.getDni());
            info.add(context.getString(R.string.name) + ": " + user.getNombre());
            info.add(context.getString(R.string.surnames) + ": " + user.getApellidos());
            info.add(context.getString(R.string.email) + ": " + user.getEmail());
            info.add(context.getString(R.string.address) + ": " + user.getDireccion());
            info.add(context.getString(R.string.phone) + ": " + user.getTelefono1());
            info.add(context.getString(R.string.phone2) + ": " + user.getTelefono2());

            if (user.getTipos().getId() == 4) {
                info.add(context.getString(R.string.zikloa) + ": " + matriculaciones.getCiclos().getNombre());
                info.add(context.getString(R.string.schoolYear) + ": " + matriculaciones.getId().getCurso());
            }
        return info;
    }

    public byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public TableRow createHeaderRow(Context context) {
        TableRow headerRow = new TableRow(context);
        String[] headersName = {"L/A", "M/A", "X", "J/O", "V/O"};
        for (String header : headersName) {
            TextView headerTextView = new TextView(context);
            headerTextView.setText(header);
            headerTextView.setGravity(Gravity.CENTER);
            headerTextView.setTextColor(Color.WHITE);
            headerTextView.setPadding(16, 16, 16, 16);
            headerRow.addView(headerTextView);
        }
        headerRow.setBackgroundColor(Color.parseColor("#007DC3")); // Blue header background
        return headerRow;
    }

    public String[] arrayListToArrayIkastetxeak(ArrayList<Ikastetxeak> ikastetxeak) {
        String[] ikastetxeakNames = new String[ikastetxeak.size()];
        for (int i = 0; i < ikastetxeak.size(); i++) {
            ikastetxeakNames[i] = ikastetxeak.get(i).getNOM();
        }
        return ikastetxeakNames;
    }

    public String[] arrayListToArrayUser(ArrayList<Users> users) {
        String[] userNames = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            userNames[i] = users.get(i).getNombre() + " " + users.get(i).getApellidos();
        }
        return userNames;
    }

    public String nameToCode(String name) {
        switch (name) {
            case "Tutoria":
                return "T";
            case "Guardia":
                return "G";
            case "Sistemas Informaticos":
                return "SI";
            case "Bases de datos":
                return "BD";
            case "Programación":
                return "PR";
            case "Lenguajes de marcas":
                return "LM";
            case "Entornos de desarrollo":
                return "ED";
            case "Acceso a datos":
                return "AD";
            case "Desarrollo de interfaces":
                return "DI";
            case "Programación multimedia y dispositivos móviles":
                return "PMDM";
            case "Programación de servicios y procesos":
                return "PSP";
            case "Sistemas de gestión empresarial":
                return "SGE";
            case "Empresa e Iniciativa Emprendedora":
                return "EIE";
            default:
                return "N/A";
        }
    }
}
