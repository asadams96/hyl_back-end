package com.hyl.gatewayserver.service;

import com.hyl.gatewayserver.model.Role;
import com.hyl.gatewayserver.model.SignUpRequest;
import com.hyl.gatewayserver.model.User;
import com.hyl.gatewayserver.proxies.UserApiProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class UserService {

    @Value("${hyl.admin.username}")
    private String adminUsername;

    @Value("${hyl.admin.passwordEncrypted}")
    private String adminPassword;

    private User admin;

    @PostConstruct
    public void init() {
        this.admin = new User(adminUsername, adminPassword, true, Collections.singletonList(Role.ROLE_ADMIN));
    }

    private final UserApiProxy userApiProxy;

    @Autowired
    public UserService(UserApiProxy userApiProxy) {
        this.userApiProxy = userApiProxy;
    }

    public User getAdmin() {
        return admin;
    }

    public Mono<User> findByUsername(String username) {
        if (username.equals(adminUsername)) {
            return Mono.just(admin);
        } else {
            return userApiProxy.getUserByEmail(admin, username).flatMap(userFromApi -> {
                if (userFromApi != null
                        && userFromApi.getUsername() != null && userFromApi.getPassword() != null
                        && !userFromApi.getUsername().isBlank() && !userFromApi.getPassword().isBlank()) {
                    userFromApi.setEnabled(true);
                    userFromApi.setRoles(Collections.singletonList(Role.ROLE_USER));
                    return Mono.just(userFromApi);
                } else {
                    return Mono.empty();
                }
            });
        }
    }

    public Mono<User> doUserInscription(SignUpRequest signUpRequest) {
        return userApiProxy.signup(admin, signUpRequest).flatMap(booleanResponse -> {
            if (booleanResponse) {
                return Mono.just(new User(signUpRequest.getEmail(), signUpRequest.getPassword()));
            } else {
                return Mono.empty();
            }
        });
    }

    public Mono<Boolean> doUserConnection(String email) {
        return userApiProxy.signin(admin, email);
    }

    public Mono<Boolean> doUserDisconnection(String email) {
        return userApiProxy.signout(admin, email);
    }
}
