package ru.rsoi.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RentResponse {
    @NotNull
    private Integer id;
    @NotNull
    private UUID movieUid;
    @NotNull
    private Integer duration;
    @NotNull
    private Integer price;
    @NotNull
    private Boolean confirmed;
}
