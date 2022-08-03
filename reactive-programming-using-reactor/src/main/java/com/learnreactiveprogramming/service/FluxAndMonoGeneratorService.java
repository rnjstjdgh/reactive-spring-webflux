package com.learnreactiveprogramming.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;


@Slf4j
public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(Arrays.asList("alex", "ben", "chloe")).log();
    }

    public Mono<String> namesMono(){
        return Mono.just("alex");
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    log.info("Name is : " + name);
                });

        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name -> {
                    log.info("Name is : " + name);
                });
    }
}
