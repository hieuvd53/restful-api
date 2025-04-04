package vn.jobhunter.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.jobhunter.jobhunter.domain.User;
import vn.jobhunter.jobhunter.domain.response.ResCreateUserDTO;
import vn.jobhunter.jobhunter.domain.response.ResUpdateUserDTO;
import vn.jobhunter.jobhunter.domain.response.ResUserDTO;
import vn.jobhunter.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobhunter.jobhunter.service.UserService;
import vn.jobhunter.jobhunter.util.annotation.ApiMessage;
import vn.jobhunter.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User reqUser) throws IdInvalidException {
        if (this.userService.isExistsByEmail(reqUser.getEmail())) {
            throw new IdInvalidException("Email " + reqUser.getEmail() + " da ton tai. Vui long tao email khac!");
        }
        String hashPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(reqUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.userService.buildCreatedUserResponse(newUser));
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if (user == null) {
            throw new IdInvalidException("User voi id=" + id + " khong ton tai!");
        }
        return ResponseEntity.ok().body(this.userService.buildUserResponse(user));
    }

    // fetch all user
    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {

        String sCurrent = currentOptional.orElse("");
        String sPageSize = pageSizeOptional.orElse("");

        int currentPage = Integer.parseInt(sCurrent) - 1;
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return ResponseEntity.ok().body(this.userService.fetchAllUser(pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User reqUser) throws IdInvalidException {
        User user = this.userService.fetchUserById(reqUser.getId());
        if (user == null) {
            throw new IdInvalidException("User voi id=" + reqUser.getId() + " khong ton tai!");
        }
        User userUpdate = this.userService.updateUser(user);
        return ResponseEntity.ok().body(this.userService.buildUpdateUserResponse(userUpdate));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if (user == null) {
            throw new IdInvalidException("User voi id=" + id + " khong ton tai!");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok().body(null);
    }

}
