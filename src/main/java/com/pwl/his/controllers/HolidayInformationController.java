package com.pwl.his.controllers;

import com.pwl.his.model.HolidayMatchResource;
import com.pwl.his.services.HolidayMatcherService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class HolidayInformationController {


    private final HolidayMatcherService service;

    @Value("#{'${coutry.codes}'}")
    private List<String> countryCodes;


    @Autowired
    public HolidayInformationController(HolidayMatcherService service) {
        this.service = service;
    }


    /**
     * Find common holiday for two countries by country code
     *
     * @param firstCountry  first country code
     * @param secondCountry second country code
     * @param date          start to find holiday from
     * @return @HolidayMatchResource
     */
    @Operation(summary = "Holiday matcher by Country Code and date."
            , description = "Find holiday after the given date that will happen on the same day in both countries.", tags = {"holiday"})
    @GetMapping("/holidays")
    public HolidayMatchResource getHolidayInformation(@RequestParam("First Country Code")  String firstCountry,
                                                      @RequestParam("Second Country Code") String secondCountry,
                                                      @RequestParam("From Date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        if (!countryCodes.contains(firstCountry.toUpperCase()) || !countryCodes.contains(secondCountry.toUpperCase())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Country codes not found, try :"
            );
        }
        HolidayMatchResource matchedHoliday = service.getMatchedHoliday(firstCountry, secondCountry, LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date)));
        if (matchedHoliday == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Holiday not found :"
            );
        }
        return matchedHoliday;
    }

}
