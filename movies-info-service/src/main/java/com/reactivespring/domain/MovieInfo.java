package com.reactivespring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document   // RDB <-> JPA 에서 Entity 와 비슷한 의미 & Document 가 NoSQL 에선 일종의 테이블이라고 보면 된다.
public class MovieInfo {

    @Id
    private String movieInfoId;

    @NotBlank(message = "movieInfo.name must be present")
    private String name;

    @NotNull
    @Positive(message = "movieInfo.year must be positive value")
    private Integer year;           // 영화 출시년


    private List<@NotBlank(message = "movieInfo.cast must be present") String> cast;      // 케스팅 된 사람들 목록
    private LocalDate release_date;
}
