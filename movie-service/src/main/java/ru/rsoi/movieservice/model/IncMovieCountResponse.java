package ru.rsoi.movieservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class IncMovieCountResponse {
    @NotNull
    private Integer days;
    @NotNull
    private Integer rentId;
}





