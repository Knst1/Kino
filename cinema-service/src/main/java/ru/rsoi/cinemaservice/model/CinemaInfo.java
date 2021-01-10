package ru.rsoi.cinemaservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CinemaInfo {
    private UUID cinemaUid;
    private String name;
    private String chief;
    private String owner;
    private String phone;
    private Integer count;
    private String address;
}
