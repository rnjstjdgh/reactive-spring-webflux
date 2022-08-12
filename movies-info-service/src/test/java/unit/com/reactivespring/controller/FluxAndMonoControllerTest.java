package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

/***
 * > FluxAndMonoController 를 테스트 한다는 의미
 * > 단순히 api 를 쏴 보는 것을 넘어서 FluxAndMonoController 를 로딩 하고 그걸 테스트 하는 개념
 * > 스프링 서버를 구동 -> 오래걸릴 수 있음
 * > 최대한 필요한 부분만 구동해서 테스트하도록 하자(slice test)
 *
 */
@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebClient // controller test 를 위한 webclient 자동 설정 한다는 의미
class FluxAndMonoControllerTest {

    /***
     * > controller test 를 위한 전용 테스트 웹 클라이언트
     * > @AutoConfigureWebClient 에 의해 설정
     */
    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux() {
        webTestClient
                .get()              // 요청은 GET 메소드로
                .uri("/flux")   // url 은 /flux 로
                .exchange()         // 요청을 보내자!
                .expectStatus()     // 응답이 올껀데
                .is2xxSuccessful()  // 200 응답일꺼구
                .expectBodyList(Integer.class)  // 응답 본문은 Integer 리스트 일꺼야 
                .hasSize(3);                    // 그 리스트 크기는 3일꺼야
    }
}