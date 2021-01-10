package ru.rsoi.cinemaservice.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "cinema")
public class Cinema {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "external_id", nullable = false)
    private UUID cinemaUid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "chief", nullable = false)
    private String chief;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "count", columnDefinition = "integer default 0", nullable = false)
    @Min(0)
    // Количество аренд (потенциальных и оплаченных)
    private Integer count;

    @Column(name = "address")
    private String address;
}
