package com.pwl.his.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Holiday",
        description = "Data object for an holiday",
        oneOf = HolidayMatchResource.class
)
public class HolidayMatchResource {
    private LocalDate date;
    private String name1;
    private String name2;

}
