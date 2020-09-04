package com.pwl.his.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"date",
"localName"
})
public class Holiday {

    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("localName")
    private String localName;


}
