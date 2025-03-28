package vn.jobhunter.jobhunter.service;

import java.util.List;
import vn.jobhunter.jobhunter.domain.User;

public interface UserService {

    User handleSave(User user);

    void handleDeleteUser(long id);

    User fetchUserById(long id);

    List<User> fetchUser();

    User updateUser(User reqUser);

    User handleGetUserByUserName(String userName);
}
