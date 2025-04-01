package vn.jobhunter.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.jobhunter.jobhunter.domain.User;
import vn.jobhunter.jobhunter.domain.dto.Meta;
import vn.jobhunter.jobhunter.domain.dto.ResultPaginationDTO;
import vn.jobhunter.jobhunter.domain.dto.ResCreateUserDTO;
import vn.jobhunter.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.jobhunter.jobhunter.domain.dto.ResUserDTO;
import vn.jobhunter.jobhunter.repository.UserRepository;
import vn.jobhunter.jobhunter.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    @Override
    public ResultPaginationDTO fetchAllUser(Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();

        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getNumberOfElements());

        rs.setMeta(meta);
        List<ResUserDTO> userDTOs = userPage.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getGender(),
                        item.getAddress(),
                        item.getCreatedDate(),
                        item.getLastModifiedDate()))
                .collect(Collectors.toList());
        rs.setResult(userDTOs);
        return rs;
    }

    @Override
    public User updateUser(User reqUser) {
        User userUpdate = this.fetchUserById(reqUser.getId());
        if (userUpdate != null) {
            userUpdate.setName(reqUser.getName());
            userUpdate.setAddress(reqUser.getAddress());
            userUpdate.setAge(reqUser.getAge());
            userUpdate.setGender(reqUser.getGender());
            this.userRepository.save(userUpdate);
        }
        return userUpdate;
    }

    @Override
    public User handleGetUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }

    @Override
    public boolean isExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public ResCreateUserDTO buildCreatedUserResponse(User user) {
        ResCreateUserDTO userDTO = new ResCreateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedDate(user.getCreatedDate());
        return userDTO;
    }

    @Override
    public ResUpdateUserDTO buildUpdateUserResponse(User user) {
        ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        return userDTO;
    }

    @Override
    public ResUserDTO buildUserResponse(User user) {
        ResUserDTO userDTO = new ResUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setCreatedDate(user.getCreatedDate());
        userDTO.setLastModifiedDate(user.getLastModifiedDate());
        return userDTO;
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUserName(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    @Override
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
