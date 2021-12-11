package com.mycompany.location.utility;

import com.mycompany.location.entity.AddressEntity;
import com.mycompany.location.entity.LocationEntity;
import com.mycompany.location.entity.ServiceEntity;
import com.mycompany.location.entity.TimingsEntity;
import com.mycompany.location.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class LocationConverter {

    public LocationEntity modelToEntity(LocationRequestModel locationRequestModel)
    {
        LocationEntity locationEntity = new LocationEntity();
        AddressEntity addressEntity = null;
        TimingsEntity timingsEntity = null;

        locationEntity.setLocationType(locationRequestModel.getLocationType());

        if(locationRequestModel.getAddress() != null)
        {
            addressEntity = new AddressEntity();
            addressEntity.setAddressLine1(locationRequestModel.getAddress().getAddressLine1());
            addressEntity.setAddressLine2(locationRequestModel.getAddress().getAddressLine2());
            addressEntity.setStreet(locationRequestModel.getAddress().getStreet());
            addressEntity.setCity(locationRequestModel.getAddress().getCity());
            addressEntity.setState(locationRequestModel.getAddress().getState());
            addressEntity.setPincode(locationRequestModel.getAddress().getPincode());
            locationEntity.setAddress(addressEntity);
        }

        locationEntity.setUserid(locationRequestModel.getUserid());
        locationEntity.setStatus(locationRequestModel.getStatus());

        if(locationRequestModel.getTimings() != null)
        {
            timingsEntity = new TimingsEntity();
            timingsEntity.setMonToFriOpenTime(locationRequestModel.getTimings().getMonToFriOpenTime());
            timingsEntity.setMonToFriCloseTime(locationRequestModel.getTimings().getMonToFriCloseTime());
            timingsEntity.setSatOpenTime(locationRequestModel.getTimings().getSatOpenTime());
            timingsEntity.setSatCloseTime(locationRequestModel.getTimings().getSatCloseTime());
            timingsEntity.setSunOpenTime(locationRequestModel.getTimings().getSunOpenTime());
            timingsEntity.setSunCloseTime(locationRequestModel.getTimings().getSunCloseTime());
            locationEntity.setTimings(timingsEntity);
        }

        /*
        if(!locationRequestModel.getServiceIds().isEmpty())
        {
            List<ServiceRequestModel> servicesList = locationRequestModel.getServices();

            for (ServiceRequestModel requestModel : servicesList) {
                ServiceEntity serviceEntity = new ServiceEntity();
                serviceEntity.setServiceId(requestModel.getId());
                serviceEntity.setTitle(requestModel.getTitle());
                serviceEntitySet.add(serviceEntity);
            }
            locationEntity.setServices(serviceEntitySet);
        }
        */
        return locationEntity;
    }

    public LocationResponseModel entityToModel(LocationEntity locationEntity, List<ServiceModel> serviceModelList)
    {
        LocationResponseModel locationResponseModel = new LocationResponseModel();
        AddressResponseModel addressResponseModel = new AddressResponseModel();
        TimingsResponseModel timingsResponseModel = new TimingsResponseModel();

        locationResponseModel.setId(locationEntity.getId());
        locationResponseModel.setLocationType(locationEntity.getLocationType());

        addressResponseModel.setId(locationEntity.getAddress().getId());
        addressResponseModel.setAddressLine1(locationEntity.getAddress().getAddressLine1());
        addressResponseModel.setAddressLine2(locationEntity.getAddress().getAddressLine2());
        addressResponseModel.setStreet(locationEntity.getAddress().getStreet());
        addressResponseModel.setCity(locationEntity.getAddress().getCity());
        addressResponseModel.setState(locationEntity.getAddress().getState());
        addressResponseModel.setPincode(locationEntity.getAddress().getPincode());
        addressResponseModel.setLatitude(locationEntity.getAddress().getLatitude());
        addressResponseModel.setLongitude(locationEntity.getAddress().getLongitude());
        locationResponseModel.setAddress(addressResponseModel);

        locationResponseModel.setUserid(locationEntity.getUserid());
        locationResponseModel.setStatus(locationEntity.getStatus());

        timingsResponseModel.setId(locationEntity.getTimings().getId());
        timingsResponseModel.setMonToFriOpenTime(locationEntity.getTimings().getMonToFriOpenTime());
        timingsResponseModel.setMonToFriCloseTime(locationEntity.getTimings().getMonToFriCloseTime());
        timingsResponseModel.setSatOpenTime(locationEntity.getTimings().getSatOpenTime());
        timingsResponseModel.setSatCloseTime(locationEntity.getTimings().getSatCloseTime());
        timingsResponseModel.setSunOpenTime(locationEntity.getTimings().getSunOpenTime());
        timingsResponseModel.setSunCloseTime(locationEntity.getTimings().getSunCloseTime());
        locationResponseModel.setTimings(timingsResponseModel);

        locationResponseModel.setServices(serviceModelList);

        return locationResponseModel;
    }
}
