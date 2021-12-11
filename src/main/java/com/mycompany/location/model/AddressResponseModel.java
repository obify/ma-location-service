package com.mycompany.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseModel implements Serializable {

    private Long id;
    private String addressLine1;
    private String addressLine2;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String latitude;
    private String longitude;
}
