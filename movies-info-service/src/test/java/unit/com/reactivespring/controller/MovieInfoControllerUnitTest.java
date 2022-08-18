package com.reactivespring.controller;


import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
public class MovieInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoServiceMock;

    static String MOVIES_INFO_URL = "/v1/movieinfos";

    @Test
    void getAllMoviesInfo(){
        List movieinfos = Arrays.asList(
                new MovieInfo(null, "Batman Begins",
                        2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, Arrays.asList("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, Arrays.asList("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        when(movieInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieinfos));

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
        // given
        String movieInfoId = "abc";

        when(movieInfoServiceMock.getMovieInfoById(movieInfoId)).thenReturn(
                Mono.just(new MovieInfo("abc", "Dark Knight Rises", 2012, Arrays.asList("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))));

        // when
        webTestClient
                .get()
                .uri(MOVIES_INFO_URL + "/{id}",movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }

    @Test
    void addMovieInfo() {
        // given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(movieInfoServiceMock.addMovieInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins1",
                        2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

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
                    assertEquals(savedMovieInfo.getMovieInfoId(),"mockId");
                });

        // then
    }

    @Test
    void addMovieInfo_validation() {
        // given
        MovieInfo movieInfo = new MovieInfo(null, "",
                -2005, Arrays.asList(""), LocalDate.parse("2005-06-15"));

        // when
        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String responseBody = stringEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                    System.out.println(responseBody);
                    final String extractedErrorMsg = "movieInfo.cast must be present , movieInfo.name must be present , movieInfo.year must be positive value";
                    assertEquals(responseBody, extractedErrorMsg);
                });


//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    MovieInfo savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(savedMovieInfo.getMovieInfoId());
//                    assertEquals(savedMovieInfo.getMovieInfoId(),"mockId");
//                });

        // then
    }

    @Test
    void updateMovieInfo() {
        // given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins11",
                2005, Arrays.asList("Christian Bale1", "Michael Cane1"), LocalDate.parse("2005-06-15"));
        String movieInfoId = "abc";

        when(movieInfoServiceMock.updateMovieInfo(movieInfo, movieInfoId)).thenReturn(
                Mono.just(new MovieInfo(movieInfoId, "Batman Begins11",
                        2005, Arrays.asList("Christian Bale1", "Michael Cane1"), LocalDate.parse("2005-06-15")))
        );

        // when & then
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

    }

}
