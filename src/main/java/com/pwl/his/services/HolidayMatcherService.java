package com.pwl.his.services;

import com.pwl.his.model.HolidayMatchResource;
import com.pwl.his.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class HolidayMatcherService {

    @Value("${holiday.repo.url}")
    private String url;

    private final RestTemplate restTemplate;

@Autowired
    public HolidayMatcherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HolidayMatchResource getMatchedHoliday(String countryCode, String comparedCountryCode, LocalDate date){

        List<Holiday> firstCountryHoliday = getHolidayForCountryFromDate(countryCode, date);
        List<Holiday> secondCountryHoliday = getHolidayForCountryFromDate(comparedCountryCode, date);

        if(firstCountryHoliday == null || secondCountryHoliday == null){
            return null;
        }

        firstCountryHoliday.addAll(secondCountryHoliday);

        Optional<LocalDate> localDate = firstCountryHoliday.stream()
                .collect(groupingBy(Holiday::getDate, TreeMap::new, Collectors.toList()))
                .entrySet().stream()
                .filter(x -> x.getValue().size() > 1)
                .filter(x -> x.getKey().isAfter(date))
                .findFirst().map(Map.Entry::getKey);

        if(localDate.isEmpty()){
            return null;
        }
        Optional<Holiday> holidayFirstCountry = firstCountryHoliday.stream().filter(c->c.getDate().equals(localDate.get())).findFirst();
        Optional<Holiday> holidaySecondCountry = secondCountryHoliday.stream().filter(c->c.getDate().equals(localDate.get())).findFirst();

        return new HolidayMatchResource(localDate.get(),holidayFirstCountry.get().getLocalName(),holidaySecondCountry.get().getLocalName());
}

    private List<Holiday> getHolidayForCountryFromDate(String countryCode, LocalDate date) {
        try {
           ResponseEntity<Holiday[]> response = restTemplate.getForEntity(
                   url + date.getYear() + "/" + countryCode, Holiday[].class);
            return new ArrayList<>(Arrays.asList(Objects.requireNonNull(response.getBody())));
       }catch (HttpClientErrorException e){
           return null;
       }
    }
}
