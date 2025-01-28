package model.dao;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import callbacks.GetIkastetxeakCallback;
import model.Ikastetxeak;

public class MIkastxeak extends Thread{
    private Socket socket;
    private PrintWriter pw;
    private ObjectInputStream ois;

    private String key;

    private GetIkastetxeakCallback getIkastetxeakCallback;

    public MIkastxeak(String key, GetIkastetxeakCallback callback) {
        this.key = key;
        this.getIkastetxeakCallback = callback;
        this.start();
    }

    @Override
    public void run() {
        try {
            Conection conection = new Conection();
            socket = conection.connect();
            pw = new PrintWriter(socket.getOutputStream(), true);

            switch (key) {
                case "getIkastetxeak":
                    getIkastetxeak(getIkastetxeakCallback);
                    break;
            }

            conection.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getIkastetxeak(GetIkastetxeakCallback callback) {
        try {
            pw.println("getIkastetxeak");
            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<Ikastetxeak> ikastetxeak = (ArrayList<Ikastetxeak>) ois.readObject();

            callback.onGetIkastetxeakCallback(ikastetxeak);
        } catch (IOException e) {
            Log.d("loginProba", "error");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Log.d("loginProba", "error");
        }
    }
}
