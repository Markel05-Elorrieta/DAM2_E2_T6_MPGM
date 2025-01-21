package model.dao;

import android.util.Log;

import com.example.dam2_e2_t6_mpgm.GlobalVariables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import callbacks.ChangePwdCallback;
import callbacks.LoginAndroidCallback;
import callbacks.UsersByTeacherCallback;
import model.Horarios;
import model.Users;

public class MUsers extends Thread {
    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;

    private String key;
    private String email;
    private String password;
    private int idIrakasle;

    private LoginAndroidCallback androidCallback;
    private ChangePwdCallback callbackChangePwd;
    private UsersByTeacherCallback callbackUsersByTeacher;

    public MUsers(String key, String email, String password, LoginAndroidCallback callback) {
        Log.d("loginProba", "llego constructor");
        this.key = key;
        this.email = email;
        this.password = password;
        this.androidCallback = callback;
        this.start();
    }

    public MUsers(String key, String email, ChangePwdCallback callback) {
        this.key = key;
        this.email = email;
        this.callbackChangePwd = callback;
        this.start();
    }

    public MUsers(String key, int idIrakasle, UsersByTeacherCallback callback) {
        this.key = key;
        this.idIrakasle = idIrakasle;
        this.callbackUsersByTeacher = callback;
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
                case "loginAndroid":
                    loginAndroid(email, password, androidCallback);
                    break;
                case "changePwd":
                    changePwd(email, callbackChangePwd);
                    break;
                case "usersByTeacher":
                    usersByTeacher(idIrakasle, callbackUsersByTeacher);
                    break;

            }
            Log.d("loginProba", "salgo run");

            conection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loginAndroid(String email, String password, LoginAndroidCallback callback) {
        Log.d("loginProba", "llego metodo");
        try {
            pw.println("loginAndroid/" + email + "/" + password);

            ois = new ObjectInputStream(socket.getInputStream());
            Log.d("loginProba", "toca leer");
            Users user = (Users) ois.readObject();
            if (user != null) {
                GlobalVariables.logedUser = user;
                callback.onLoginAndroid(true);
            } else {
                callback.onLoginAndroid(false);
            }
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        }

    }

    public void changePwd(String email, ChangePwdCallback callback) {
        Log.d("loginProba", "llego metodo");

        try {
            pw.println("changePwd/" + email);

            ois = new ObjectInputStream(socket.getInputStream());
            Object numBoolean = (Object) ois.readObject();

            if (numBoolean.equals("1")) {
                callback.onChangePwd(true);
            } else {
                callback.onChangePwd(false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void usersByTeacher(int idIrakasle, UsersByTeacherCallback callback) {
        try {
            pw.println("usersByTeacher/" + idIrakasle);

            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Users> ikasleak = (ArrayList<Users>) ois.readObject();

            callback.onUserByTeacher(ikasleak);
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        }
    }
}
