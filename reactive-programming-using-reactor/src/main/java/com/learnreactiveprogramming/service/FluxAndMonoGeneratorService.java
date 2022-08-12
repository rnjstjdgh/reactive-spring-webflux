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

    public Flux<String> namesFluxMap(){
        return Flux.fromIterable(Arrays.asList("alex", "ben", "chloe"))
//                .map(ele -> ele.toUpperCase())
                .map(String::toUpperCase);
    }

    public Flux<String> namesFluxMapImmutabillity(){
         Flux<String> namesFluxMap = Flux.fromIterable(Arrays.asList("alex", "ben", "chloe"));
        /***
         * > 아래 코드가 실행되는 순간 [Arrays.asList("alex", "ben", "chloe")] 이 리스트의 내용이 바뀔까?
         * > 정답은 NO!
         *      > 테스트를 해 보면 namesFluxMap 을 구독 하게 되면 여전히 소문자인 것을 확인할 수 있다.
         *      > 채이닝을 통해 map 을 적용한 그 Flux 를 리턴해야 map 이 적용된다.
         *      > 즉 아래 2 case 의 flux 는 서로 다르다(map function 도 새로운 Flux 를 리턴한다.)
         *          > Flux<String> namesFluxMap = Flux.fromIterable(Arrays.asList("alex", "ben", "chloe"));
         *          > Flux<String> namesFluxMap = Flux.fromIterable(Arrays.asList("alex", "ben", "chloe")).map(~~);
         * > Reactive Steams 에서 immutable 은 중요한 특성이다.
         * > 원천 데이터 자체가 변하는 것이 아니라 구독 시점에 Publisher -> Subscriber 로 전달되는 중간에 map 을 통해 변경되는 것
         */
        namesFluxMap.map(String::toUpperCase);
        return namesFluxMap;
    }

    public Flux<String> namesFluxFilter(int stringLength){
        return Flux.fromIterable(Arrays.asList("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .map(s -> s.length() + " - " + s);
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
