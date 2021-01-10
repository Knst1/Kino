package ru.rsoi.frontend.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RentInfoResponse {
    private Integer id;
    private Movie movie;
    private Integer duration;
    private Integer price;
    private Boolean confirmed;
    private Boolean movieAccess;
}





