package vn.jobhunter.jobhunter.service;

import org.springframework.data.domain.Pageable;
import vn.jobhunter.jobhunter.domain.User;
import vn.jobhunter.jobhunter.domain.response.ResCreateUserDTO;
import vn.jobhunter.jobhunter.domain.response.ResUpdateUserDTO;
import vn.jobhunter.jobhunter.domain.response.ResUserDTO;
import vn.jobhunter.jobhunter.domain.response.ResultPaginationDTO;

public interface UserService {

    User handleCreateUser(User user);

    void handleDeleteUser(long id);

    User fetchUserById(long id);

    ResultPaginationDTO fetchAllUser(Pageable pageable);

    User updateUser(User reqUser);

    User handleGetUserByUserName(String userName);

    boolean isExistsByEmail(String email);

    ResCreateUserDTO buildCreatedUserResponse(User user); // convert to UserDTO for response

    ResUserDTO buildUserResponse(User user);

    ResUpdateUserDTO buildUpdateUserResponse(User user); // convert to UserDTO for response

    void updateUserToken(String token, String email);

    public User getUserByRefreshTokenAndEmail(String token, String email);

}
