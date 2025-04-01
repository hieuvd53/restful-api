package vn.jobhunter.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.jobhunter.jobhunter.domain.User;
import vn.jobhunter.jobhunter.domain.dto.LoginDTO;
import vn.jobhunter.jobhunter.domain.dto.ResLoginDTO;
import vn.jobhunter.jobhunter.service.UserService;
import vn.jobhunter.jobhunter.util.SecurityUtil;
import vn.jobhunter.jobhunter.util.annotation.ApiMessage;
import vn.jobhunter.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private UserService userService;

        @Value("${jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(
                        AuthenticationManagerBuilder authenticationManagerBuilder,
                        SecurityUtil securityUtil,
                        UserService userService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDTO.getUserName(), loginDTO.getPassword());

                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                ResLoginDTO res = new ResLoginDTO();
                User userLoginInfo = this.userService.handleGetUserByUserName(loginDTO.getUserName());
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                userLoginInfo.getId(),
                                userLoginInfo.getName(),
                                userLoginInfo.getEmail());
                res.setUserLogin(userLogin);

                // Create access Token
                String access_token = this.securityUtil.createAccessToken(authentication.getName(), res.getUserLogin());
                res.setAccess_token(access_token);

                // create refresh token
                String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUserName(), res);
                System.out.println("Old Refresh Token: " + userLoginInfo.getRefreshToken());

                // update refresh token to table User
                this.userService.updateUserToken(refresh_token, loginDTO.getUserName());
                System.out.println("New Refresh Token: " + refresh_token);

                // set cookie
                ResponseCookie responseCookie = ResponseCookie
                                .from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                // return ResponseEntity.ok().body(res);
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(res);
        }

        @GetMapping("/auth/account")
        @ApiMessage("fetch account login")
        public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
                String email = SecurityUtil.getCurrentUserLogin().orElse("");

                User userLoginInfo = this.userService.handleGetUserByUserName(email);
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                if (userLoginInfo != null) {
                        userLogin.setId(userLoginInfo.getId());
                        userLogin.setEmail(userLoginInfo.getEmail());
                        userLogin.setName(userLoginInfo.getName());
                }

                return ResponseEntity.ok(userLogin);
        }

        @GetMapping("/auth/refresh")
        @ApiMessage("Get user by refresh token")
        public ResponseEntity<ResLoginDTO> getRefreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "abc") String refreshToken)
                        throws IdInvalidException {

                if (refreshToken.equals("abc")) {
                        throw new IdInvalidException("ban k co refresh token o cookie");
                }

                // lay refreshToken tu cookie. name refresh_token la name luu o cookie
                // check refresh token hợp lệ không
                Jwt decodedToken = this.securityUtil.checkRefreshToken(refreshToken);
                // lấy login id từ refreshtoken
                String email = decodedToken.getSubject();

                // check user theo token + email
                User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
                if (currentUser == null) {
                        throw new IdInvalidException("refresh token k hop le!");
                }

                ResLoginDTO res = new ResLoginDTO();
                User userLoginInfo = this.userService.handleGetUserByUserName(email);
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                userLoginInfo.getId(),
                                userLoginInfo.getName(),
                                userLoginInfo.getEmail());
                res.setUserLogin(userLogin);

                // Create access Token
                String access_token = this.securityUtil.createAccessToken(email, res.getUserLogin());
                res.setAccess_token(access_token);

                // create refresh token
                String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

                // update refresh token to table User
                this.userService.updateUserToken(new_refresh_token, email);

                // set cookie
                ResponseCookie responseCookie = ResponseCookie
                                .from("refresh_token", new_refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();
                // return ResponseEntity.ok().body(res);
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(res);

        }

        @PostMapping("/auth/logout")
        @ApiMessage("logout")
        public ResponseEntity<Void> logout() throws IdInvalidException {

                String email = SecurityUtil.getCurrentUserLogin().orElse("");
                if (email.equals("")) {
                        throw new IdInvalidException("access token k hop le");
                }

                this.userService.updateUserToken(null, email);

                // remove refresh token
                ResponseCookie deletedCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deletedCookie.toString()).body(null);
        }

}
