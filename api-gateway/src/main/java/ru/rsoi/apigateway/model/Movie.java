package ru.rsoi.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Movie {
    @NotNull
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Min(1900)
    private Integer year;
    @NotNull
    private String producer;
    @NotNull
    @Min(0)
    private Integer cost;
    @NotNull
    @Min(0)
    private Integer imdb;
    @NotNull
    private Integer count;
}
