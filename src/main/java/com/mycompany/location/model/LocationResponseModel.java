package com.mycompany.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseModel implements Serializable {

    private Long id;
    private String locationType;
    private AddressResponseModel address;
    private Long userid;
    private String status;
    private TimingsResponseModel timings;
    private List<ServiceModel> services = new ArrayList<>();
}
