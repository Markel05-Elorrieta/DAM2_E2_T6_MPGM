package model.dao;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import callbacks.AcceptBileraCallback;
import callbacks.BilerakByStudentCallback;
import callbacks.BilerakByTeacherCallback;
import callbacks.DeclineBileraCallback;
import callbacks.NewBileraCallback;
import model.Reuniones;

public class MReuniones extends Thread{
    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;

    private String key;
    private int idStudent;
    private int idTeacher;
    private Reuniones reunion;
    private int idReunion;

    private BilerakByStudentCallback bilerakByStudentCallback;
    private BilerakByTeacherCallback bilerakByTeacherCallback;
    private NewBileraCallback newBileraCallback;
    private AcceptBileraCallback acceptBileraCallback;
    private DeclineBileraCallback declireBileraCallback;

    public MReuniones(String key, int idTeacher, BilerakByTeacherCallback callback) {
        this.key = key;
        this.idTeacher = idTeacher;
        this.bilerakByTeacherCallback = callback;
        this.start();
    }

    public MReuniones(String key, int idStudent, BilerakByStudentCallback callback) {
        this.key = key;
        this.idStudent = idStudent;
        this.bilerakByStudentCallback = callback;
        this.start();
    }

    public MReuniones(String key, Reuniones reunion, NewBileraCallback callback) {
        this.key = key;
        this.reunion = reunion;
        this.newBileraCallback = callback;
        this.start();
    }

    public MReuniones(String key, int idReunion, AcceptBileraCallback callback) {
        this.key = key;
        this.idReunion = idReunion;
        this.acceptBileraCallback = callback;
        this.start();
    }

    public MReuniones(String key, int idReunion, DeclineBileraCallback callback) {
        this.key = key;
        this.idReunion = idReunion;
        this.declireBileraCallback = callback;
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
                case "bilerakByTeacher":
                    bilerakByTeacher(idTeacher, bilerakByTeacherCallback);
                    break;
                case "newBilera":
                    newBilera(reunion, newBileraCallback);
                    break;
                case "acceptBilera":
                    acceptBilera(idReunion, acceptBileraCallback);
                    break;
                case "declineBilera":
                    declineBilera(idReunion, declireBileraCallback);
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

    private void bilerakByTeacher(int idTeacher, BilerakByTeacherCallback callback) {
        try {
            Log.d("bilerakByTeacher", "bilerakByTeacher/" + idTeacher);
            pw.println("bilerakByTeacher/" + idTeacher);

            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Reuniones> reuniones = (ArrayList<Reuniones>) ois.readObject();

            callback.onBilerakByTeacherCallback(reuniones);
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
        }
    }

    private void newBilera(Reuniones reunion, NewBileraCallback callback) {
        try {
            pw.println("newBilera/" + reunion.getUsersByProfesorId().getId() + "/" + reunion.getUsersByAlumnoId().getId() + "/" + reunion.getEstado() + "/" + reunion.getIdCentro() + "/" + reunion.getTitulo() + "/" + reunion.getAsunto() + "/" + reunion.getAula() + "/" + reunion.getFecha());

            ois = new ObjectInputStream(socket.getInputStream());
            Reuniones gettedReunion = (Reuniones) ois.readObject();

            Log.d("gettedReunion", gettedReunion.toString());
            callback.onNewBileraCallback(gettedReunion);
        } catch (IOException e) {
            Log.d("gettedReunion", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("gettedReunion", "error");
        }
    }

    private void acceptBilera(int idReunion, AcceptBileraCallback callback) {
        pw.println("acceptBilera/" + idReunion);
        callback.onAcceptBilera();
    }

    private void declineBilera(int idReunion, DeclineBileraCallback callback) {
        pw.println("declineBilera/" + idReunion);
        callback.onDeclineBilera();
    }
}
