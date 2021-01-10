package ru.rsoi.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RentRequest {
    @NotNull
    private UUID cinemaUid;
    @NotNull
    private UUID movieUid;
    @NotNull
    private Integer duration;
    @NotNull
    private Integer price;
    @NotNull
    private Boolean confirmed;

}
