package callbacks;

import java.util.ArrayList;

import model.Users;

public interface UsersByTeacherCallback {
    void onUserByTeacher(ArrayList<Users> users);
}