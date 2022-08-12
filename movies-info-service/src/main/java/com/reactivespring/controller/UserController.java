package com.reactivespring.controller;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    public static void main(String[] args) {
        Flux<Flux<Flux<Integer>>> flux = Flux.just(
                Flux.just(
                    Flux.fromIterable(Arrays.asList(1,2,3)),
                    Flux.fromIterable(Arrays.asList(4,5,6)),
                    Flux.fromIterable(Arrays.asList(7,8,9))
                )
        );

        flux.map(ele -> ele).subscribe(ele -> log.info("ele: {}",ele));
        flux.flatMap(ele -> ele).subscribe(ele -> log.info("ele: {}",ele));
    }

    @Autowired
    private final UserClient userClient;

    @Autowired
    public UserController(@NonNull UserClient userClient) {
        this.userClient = userClient;
    }

    // 1초마다 User 발생
    @GetMapping(produces = "application/stream+json")
    public Flux<User> users() {

        return Flux.interval(Duration.ofSeconds(1L))
                .take(3)
                .flatMap(number -> userClient.get(number + 1L));
    }

    @Slf4j
    @Service
    public static class UserClient {
        public Flux<User> get(long id) {

            return WebClient.create("https://jsonplaceholder.typicode.com")
                    .get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .bodyToFlux(User.class);
        }
    }

    @Data
    private static class User{
        private long id;
        private String name;
        private String email;
        private Address address;
        private String phone;
        private String website;
        private Company company;
    }

    @Data
    private static class Address{
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;
    }

    @Data
    private static class Geo{
        private String lat;
        private String lng;
    }

    @Data
    private static class Company{
        private String name;
        private String catchPhrase;
        private String bs;
    }
}