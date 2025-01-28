package callbacks;

import java.util.ArrayList;

import model.Horarios;

public interface ScheduleStudentCallback {
    void onScheduleStudent(ArrayList<Horarios> horario);
}