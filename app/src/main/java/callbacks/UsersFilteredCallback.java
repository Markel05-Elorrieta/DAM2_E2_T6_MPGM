package callbacks;

import java.util.ArrayList;

import model.Users;

public interface UsersFilteredCallback {
    void onUsersFiltered(ArrayList<Users> users);
}