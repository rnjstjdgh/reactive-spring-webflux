package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // test profile -> test 용 임베디드 몽고디비 사용 (그밖에 다른 테스트 설정도 이걸로 제어 가능)
@AutoConfigureWebTestClient
class MovieInfoControllerIntgTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    static String MOVIES_INFO_URL = "/v1/movieinfos";

    @BeforeEach
    void setUp() {
        List movieinfos = Arrays.asList(
                new MovieInfo(null, "Batman Begins",
                        2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, Arrays.asList("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, Arrays.asList("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {
        // given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        // when
        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(savedMovieInfo.getMovieInfoId());
                });

        // then
    }

    @Test
    void getAllMovieInfos() {
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);

    }

    @Test
    void getMovieInfoById(){
        //given
        String movieInfoId = "abc";

        // when
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL + "/{id}",movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");

//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    MovieInfo movieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assertEquals(movieInfo.getMovieInfoId(), "abc");
//                });
    }

    @Test
    void updateMovieInfo() {
        // given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins11",
                2005, Arrays.asList("Christian Bale1", "Michael Cane1"), LocalDate.parse("2005-06-15"));
        String movieInfoId = "abc";

        // when
        webTestClient
                .put()
                .uri(MOVIES_INFO_URL + "/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(updatedMovieInfo.getMovieInfoId());
                    assertEquals(updatedMovieInfo.getName(), "Batman Begins11");
                });

        // then
    }
}