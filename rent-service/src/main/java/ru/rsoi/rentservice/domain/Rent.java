package ru.rsoi.rentservice.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "rent")
public class Rent {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cinema_id", nullable = false)
    private UUID cinemaUid;

    @Column(name = "movie_id", nullable = false)
    private UUID movieUid;

    @Column(name = "duration", nullable = false)
    @Min(1)
    private Integer duration;

    @Column(name = "price", nullable = false)
    @Min(1)
    private Integer price;

    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;
}





