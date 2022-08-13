package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest              // DB - Layer Slice test
@ActiveProfiles("test")     // test profile 로 설정 -> 테스트 용 디비 접속 등을 위함
class MovieInfoRepositoryIntgTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

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
    void findAll() {
        // given

        // when
        Flux movieInfoFlux = movieInfoRepository.findAll();

        // then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        // given

        // when
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.findById("abc");

        // then
        StepVerifier.create(movieInfoMono)
//                .expectNextCount(1)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        // given

        // when
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(new MovieInfo(null, "Batman Begins1",
                2005, Arrays.asList("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")));

        // then
        StepVerifier.create(movieInfoMono)
//                .expectNextCount(1)
                .assertNext(movieInfo -> {
                    assertNotNull(movieInfo.getMovieInfoId());
                    assertEquals("Batman Begins1", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        // given
        MovieInfo movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2021);

        // when
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo);

        // then
        StepVerifier.create(movieInfoMono)
//                .expectNextCount(1)
                .assertNext(movieInfoEle -> {
                    assertEquals(2021, movieInfoEle.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        // given

        // when
        movieInfoRepository.deleteById("abc").block();
        Flux movieInfoFlux = movieInfoRepository.findAll();

        // then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}