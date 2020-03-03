package com.hyl.gatewayserver.proxies;

import com.hyl.gatewayserver.utils.JWTUtil;
import com.hyl.gatewayserver.model.User;
import com.hyl.gatewayserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    public Mono<User> getIdByEmail(UserService userService, String email) {
        String url = UriComponentsBuilder.fromPath("/username")
                .queryParam("email", email)
                .build()
                .toString();

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer "+jwtUtil.generateToken(userService.getAdmin()))
                .retrieve()
                .bodyToMono(User.class);
    }
}
