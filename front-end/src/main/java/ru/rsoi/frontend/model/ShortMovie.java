package ru.rsoi.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ShortMovie {
    @NotNull
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Min(1900)
    private Integer year;
    @NotNull
    private Integer count;
}
