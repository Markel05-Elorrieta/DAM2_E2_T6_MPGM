package callbacks;

import java.util.ArrayList;

import model.Horarios;

public interface ScheduleTeacherCallback {
    void onScheduleTeacher(ArrayList<Horarios> horario);
}