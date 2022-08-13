package com.reactivespring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document   // RDB <-> JPA 에서 Entity 와 비슷한 의미 & Document 가 NoSQL 에선 일종의 테이블이라고 보면 된다.
public class MovieInfo {

    @Id
    private String movieInfoId;
    private String name;
    private Integer year;           // 영화 출시년
    private List<String> cast;      // 케스팅 된 사람들 목록
    private LocalDate release_date;
}
