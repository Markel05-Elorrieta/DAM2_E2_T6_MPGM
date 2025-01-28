package model.dao;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import callbacks.MatriculacionesUserCallback;
import model.Matriculaciones;

public class MMatriculaciones extends Thread {
    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;

    private String key;
    private int idUser;

    private MatriculacionesUserCallback callback;

    public MMatriculaciones(String key, int idUser, MatriculacionesUserCallback callback) {
        this.key = key;
        this.idUser = idUser;
        this.callback = callback;
        this.start();
    }

    @Override
    public void run() {
        try {
            Log.d("loginProba", "llego run");
            Conection conection = new Conection();
            socket = conection.connect();
            pw = new PrintWriter(socket.getOutputStream(), true);

            switch (key) {
                case "matriculacionesUser":
                    matriculacionesUser(idUser, callback);
                    break;
            }
            Log.d("loginProba", "salgo run");

            conection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void matriculacionesUser(int idUser, MatriculacionesUserCallback callback) {
        try {
            pw.println("matriculacionesUser/" + idUser);

            ois = new ObjectInputStream(socket.getInputStream());
            Matriculaciones matriculaciones = (Matriculaciones) ois.readObject();

            callback.onMatriculacionesUserCallback(matriculaciones);
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
        }
    }
}
