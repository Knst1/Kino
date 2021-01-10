package ru.rsoi.apigateway.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Cinema {
    @NotNull
    private String name;
    @NotNull
    private String chief;
    @NotNull
    private String owner;
    @NotNull
    private String phone;
    private String address;
}

