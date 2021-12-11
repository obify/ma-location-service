package com.mycompany.location.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestModel {

    private String locationType;
    private AddressRequestModel address;
    private Long userid;
    private String status;
    private TimingsRequestModel timings;
    private TreeSet<Integer> serviceIds;
    //@JsonIgnore
    //private List<ServiceRequestModel> services = new ArrayList<>();
}
