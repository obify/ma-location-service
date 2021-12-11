package com.mycompany.location.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultModel {
    private Double latitude;
    private Double longitude;
    private String type;
    private String name;
    private String number;
    private String postal_code;
    private String street;
    private Double confidence;
    private String region;
    private String region_code;
    private String county;
    private String locality;
    private String administrative_area;
    private String neighbourhood;
    private String country;
    private String country_code;
    private String continent;
    private String label;
}
