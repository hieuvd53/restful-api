package vn.jobhunter.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.jobhunter.jobhunter.domain.User;
import vn.jobhunter.jobhunter.repository.UserRepository;
import vn.jobhunter.jobhunter.service.impl.UserServiceImpl;

public interface UserService{

    User handleSave(User user);
    void handleDeleteUser(long id);
    User fetchUserById(long id);
    List<User> fetchUser();
    User updateUser(User reqUser);
}
