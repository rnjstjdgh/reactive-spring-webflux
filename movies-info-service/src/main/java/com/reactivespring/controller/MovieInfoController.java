package com.reactivespring.controller;


import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class MovieInfoController {

    private final MovieInfoService movieInfoService;

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovieInfos(@RequestParam(value = "year", required = false) Integer year){
        if(year != null)
            return movieInfoService.getMovieInfoByYear(year);
        else
            return movieInfoService.getAllMovieInfos();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id){
        return movieInfoService.getMovieInfoById(id)
                .map(movieInfo1 -> ResponseEntity.ok().body(movieInfo1))
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()))
                .log();
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo){
        return movieInfoService.addMovieInfo(movieInfo);
    }

    /***
     * 1> Mono<ResponseEntity<?>> 형태의 컨트롤러 응답
     *     > 응답 body 와 Status 모두 비동기로 처리
     *     > 이 경우에는 ResponseEntity 값을 적절하게 세팅해 줘야 함
     * 2> ResponseEntity<Mono<?>>>
     *     > 응답 Status 는 바로 알 수 있어서 동기 처리
     *     > 응답 Body 만 비동기로 처리
     * @param updatedMovieInfo
     * @param id
     * @return
     */
    @PutMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id){
        return movieInfoService.updateMovieInfo(updatedMovieInfo, id)
                .map(movieInfo -> ResponseEntity.ok().body(movieInfo))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id){
        return movieInfoService.deleteMovieInfo(id).log();
    }
}
