package com.hyl.gatewayserver.controller;

import com.hyl.gatewayserver.encoder.PBKDF2Encoder;
import com.hyl.gatewayserver.exception.CustomBadRequestException;
import com.hyl.gatewayserver.exception.CustomInternalServerErrorException;
import com.hyl.gatewayserver.model.AuthResponse;
import com.hyl.gatewayserver.model.SignInRequest;
import com.hyl.gatewayserver.model.SignUpRequest;
import com.hyl.gatewayserver.model.User;
import com.hyl.gatewayserver.service.UserService;
import com.hyl.gatewayserver.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException.InternalServerError;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.http.HttpRequest;
import java.util.Optional;

@RestController
public class AuthenticationREST {

    private final JWTUtil jwtUtil;

    private final PBKDF2Encoder passwordEncoder;

    private final UserService userService;

    @Autowired
    public AuthenticationREST(JWTUtil jwtUtil, PBKDF2Encoder passwordEncoder, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    @PreAuthorize("!(hasRole('USER') or hasRole('ADMIN'))")
    public Mono<ResponseEntity<?>> login(@RequestBody SignInRequest signInRequest) {
        return userService.findByUsername(signInRequest.getEmail()).flatMap((userDetails) -> {

            if (passwordEncoder.matches(signInRequest.getPassword(), userDetails.getPassword())) {
                return userService.doUserConnection(userDetails.getUsername()).map(aBoolean -> {
                    if (aBoolean) {
                        return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                }).defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            } else {
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    @PreAuthorize("!(hasRole('USER') or hasRole('ADMIN'))")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> signup(@RequestBody SignUpRequest signUpRequest) {

        if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isBlank()) {
            signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        }
        return userService.doUserInscription(signUpRequest).map(aBoolean -> {
            if (aBoolean) {
                return ResponseEntity.ok(new AuthResponse(
                        jwtUtil.generateToken(new User(signUpRequest.getEmail(), signUpRequest.getPassword()))));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> signout(@Autowired ServerWebExchange swe) {
        String authHeader = swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String email = jwtUtil.getUsernameFromToken(authHeader.substring(7));
            return userService.doUserDisconnection(email).map(aBoolean -> {
                if (aBoolean) {
                    return ResponseEntity.ok("");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }).defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomBadRequestException("Token not provided")));
        }
    }


    @ExceptionHandler({WebClientResponseException.class})
    public ResponseEntity<?> handleException(WebClientResponseException exception) {

        if (BadRequest.class.equals(exception.getClass())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomBadRequestException(exception.getMessage()));
        } else if (InternalServerError.class.equals(exception.getClass())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomInternalServerErrorException(exception.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomInternalServerErrorException(exception.getMessage()));
        }
    }
}
