package com.hyl.userapi.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

    /* TODO Retirer le commentaire ci dessous + dans le pom.xml (zipkin dependancy) -> Evite exception cra zipkin-server non déployé
    @Bean
    public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }
     */
}
