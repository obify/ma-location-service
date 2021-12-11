package com.mycompany.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestModel {

    private String addressLine1;
    private String addressLine2;
    private String street;
    private String city;
    private String state;
    private String pincode;
}
