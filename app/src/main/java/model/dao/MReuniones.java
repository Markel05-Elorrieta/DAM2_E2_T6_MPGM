package model.dao;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import callbacks.BilerakByStudentCallback;
import model.Matriculaciones;
import model.Reuniones;

public class MReuniones extends Thread{
    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;

    private String key;
    private int idStudent;

    private BilerakByStudentCallback bilerakByStudentCallback;

    public MReuniones(String key, int idStudent, BilerakByStudentCallback callback) {
        this.key = key;
        this.idStudent = idStudent;
        this.bilerakByStudentCallback = callback;
        this.start();
    }

    @Override
    public void run() {
        try {
            Conection conection = new Conection();
            socket = conection.connect();
            pw = new PrintWriter(socket.getOutputStream(), true);

            switch (key) {
                case "bilerakByStudent":
                    bilerakByStudent(idStudent , bilerakByStudentCallback);
                    break;
            }

            conection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void bilerakByStudent(int idStudent, BilerakByStudentCallback callback) {
        try {
            pw.println("bilerakByStudent/" + idStudent);

            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Reuniones> reuniones = (ArrayList<Reuniones>) ois.readObject();

            callback.onBilerakByStudentCallback(reuniones);
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
        }
    }
}
