package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        // given


        // when
        Flux namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        /***
         * > Mono & Flux test 에는 대부분 StepVerifier 가 사용됨
         * > 즉 퍼블리셔 테스트에는 StepVerifier 가 사용된다고 보면 됨
         * > 내부적으로 Subscribe 후 onNext 되어 오는 값들을 검증할 것임
         */
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben")  // Flux 를 내부적으로 구독해 보면서 예상된 데이터와 같은지 확인하는듯
                .expectNext("chloe")        // 체이닝도 지원
                .verifyComplete();

        StepVerifier.create(namesFlux)
                .expectNextCount(3)         // 이러한 다양한 테스트 메소드를 체이닝 형식으로 제공
                .verifyComplete();

        StepVerifier.create(namesFlux)
                .expectNext("alex")
                .expectNextCount(2)         // 위에서 1개를 소비 했으니 2개가 남은 것이 맞음
                .verifyComplete();

        /***
         * > 퍼블리셔의 ele 를 전부 소모 안하고 verifyComplete 호출 하면 테스트 실패한다.
         */
        StepVerifier.create(namesFlux)
                .expectNext("alex")
                .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        Flux namesFluxMap = fluxAndMonoGeneratorService.namesFluxMap();

        StepVerifier.create(namesFluxMap)
                .expectNext("ALEX","BEN")
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void namesFluxImmutabillity() {
        Flux<String> namesFluxMap = fluxAndMonoGeneratorService.namesFluxMapImmutabillity();
        StepVerifier.create(namesFluxMap)
                .expectNext("ALEX","BEN")
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void namesFluxMapFilter() {
        Flux<String> namesFluxMapFilter = fluxAndMonoGeneratorService.namesFluxFilter(3);
        StepVerifier.create(namesFluxMapFilter)
                .expectNext("4 - ALEX","5 - CHLOE")
                .verifyComplete();
    }
}