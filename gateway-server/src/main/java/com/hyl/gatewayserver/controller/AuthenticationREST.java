package com.hyl.gatewayserver.controller;

import com.hyl.gatewayserver.utils.JWTUtil;
import com.hyl.gatewayserver.encoder.PBKDF2Encoder;
import com.hyl.gatewayserver.model.AuthRequest;
import com.hyl.gatewayserver.model.AuthResponse;
import com.hyl.gatewayserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class AuthenticationREST {

    Logger logger = LoggerFactory.getLogger(AuthenticationREST.class);

    private final JWTUtil jwtUtil;

    private final PBKDF2Encoder passwordEncoder;

    private final UserService userRepository;

    @Autowired
    public AuthenticationREST(JWTUtil jwtUtil, PBKDF2Encoder passwordEncoder, UserService userRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest) {

        return userRepository.findByUsername(authRequest.getEmail()).map((userDetails) -> {

            if (passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // TODO -> Signup + Signout

}
