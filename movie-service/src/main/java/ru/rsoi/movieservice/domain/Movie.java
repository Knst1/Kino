package ru.rsoi.movieservice.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "movie")
public class Movie {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "external_id", nullable = false)
    private UUID movieUid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year", nullable = false)
    @Min(1900)
    private Integer year;

    @Column(name = "producer", nullable = false)
    private String producer;

    @Column(name = "cost", nullable = false)
    @Min(0)
    private Integer cost;

    @Column(name = "imdb", nullable = false)
    @Min(0)
    private Integer imdb;

    @Column(name = "count", columnDefinition="integer default 0", nullable = false)
    @Min(0)
    // Количество дней оплаченной аренды
    private Integer count;
}





