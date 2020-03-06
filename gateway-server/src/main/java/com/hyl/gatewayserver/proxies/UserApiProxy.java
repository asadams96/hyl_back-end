package com.hyl.gatewayserver.proxies;

import com.hyl.gatewayserver.model.SignInRequest;
import com.hyl.gatewayserver.model.SignUpRequest;
import com.hyl.gatewayserver.model.User;
import com.hyl.gatewayserver.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Component
public class UserApiProxy {

    @Value("${hyl.gateway.url}")
    private String urlGateway;

    WebClient webClient;

    private JWTUtil jwtUtil;

    @Autowired
    public UserApiProxy(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void init() {
        webClient = WebClient.create( urlGateway+"/user" );
    }


    public Mono<Void> signin(User admin, SignInRequest signInRequest) {
        String url = UriComponentsBuilder.fromPath("/signin")
                .build()
                .toString();

        return webClient.post()
                .uri(url)
                .header("Authorization", "Bearer "+jwtUtil.generateToken(admin))
                .bodyValue(signInRequest)
                .retrieve()
                .onStatus(HttpStatus::isError, ClientResponse::createException)
                .bodyToMono(void.class);
    }


    public Mono<Boolean> signup(User admin, SignUpRequest signUpRequest) {
        String url = UriComponentsBuilder.fromPath("/signup")
                .build()
                .toString();

        return webClient.post()
                .uri(url)
                .header("Authorization", "Bearer "+jwtUtil.generateToken(admin))
                .bodyValue(signUpRequest)
                .retrieve()
                .onStatus(HttpStatus::isError, ClientResponse::createException)
                .bodyToMono(Boolean.class);
    }


    public Mono<Boolean> signout(User admin, String email) {
        String url = UriComponentsBuilder.fromPath("/signout")
                .build()
                .toString();

        return webClient.post()
                .uri(url)
                .header("Authorization", "Bearer "+jwtUtil.generateToken(admin))
                .bodyValue(email)
                .retrieve()
                .onStatus(HttpStatus::isError, ClientResponse::createException)
                .bodyToMono(Boolean.class);
    }

}
