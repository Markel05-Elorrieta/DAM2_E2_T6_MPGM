package model.dao;

import android.util.Log;

import com.example.dam2_e2_t6_mpgm.GlobalVariables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import callbacks.ScheduleTeacherCallback;
import model.Horarios;
import model.Users;

public class MHorarios extends Thread{
    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;

    private String key;
    private Users user;
    private ScheduleTeacherCallback callback;

    public MHorarios(String key, Users user, ScheduleTeacherCallback callback) {
        this.key = key;
        this.user = user;
        this.callback = callback;

        this.start();
    }

    @Override
    public void run() {
        try {
            Conection conection = new Conection();
            socket = conection.connect();
            pw = new PrintWriter(socket.getOutputStream(), true);

            switch (key) {
                case "scheduleTeacher":
                    getScheduleTeacher(user);
                    break;
            }

            conection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getScheduleTeacher(Users user){
        try {
            pw.println("scheduleTeacher/" + user.getId());

            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Horarios> horarios = (ArrayList<Horarios>) ois.readObject();

            callback.onScheduleTeacher(horarios);
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        }
    }
}
