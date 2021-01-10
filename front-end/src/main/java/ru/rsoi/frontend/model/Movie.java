package ru.rsoi.frontend.model;

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
    private String id;
    private String name;
    @Min(1900)
    private Integer year;
    private String producer;
    @Min(0)
    private Integer cost;
    @Min(0)
    private Integer imdb;
    private Integer count;
}
