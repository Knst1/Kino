package ru.rsoi.apigateway.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RentInfoResponse {
    @NotNull
    private Integer id;
    @NotNull
    private Movie movie;
    @NotNull
    private Integer duration;
    @NotNull
    private Integer price;
    @NotNull
    private Boolean confirmed;
}





