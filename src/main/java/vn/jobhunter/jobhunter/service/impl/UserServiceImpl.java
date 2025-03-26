package vn.jobhunter.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.jobhunter.jobhunter.domain.User;
import vn.jobhunter.jobhunter.repository.UserRepository;
import vn.jobhunter.jobhunter.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User handleSave(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void handleDeleteUser(long id){
        this.userRepository.deleteById(id);
    }

    @Override
    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }

    @Override
    public List<User> fetchUser() {
        List<User> users= this.userRepository.findAll();
        return users;
    }

    @Override
    public User updateUser(User reqUser) {
        User userUpdate = this.fetchUserById(reqUser.getId());
        if(userUpdate != null){
            userUpdate.setEmail(reqUser.getEmail());
            userUpdate.setName(reqUser.getName());
            userUpdate.setPassword(reqUser.getPassword());
            this.userRepository.save(userUpdate);
        }
        return userUpdate;
    }
}
