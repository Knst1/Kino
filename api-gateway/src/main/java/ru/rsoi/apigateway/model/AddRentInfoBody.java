package ru.rsoi.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class AddRentInfoBody {
    @NotNull
    private Integer cinemaId;
    @NotNull
    private Integer movieId;
    @NotNull
    private Integer duration;
}





