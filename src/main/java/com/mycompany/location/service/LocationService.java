package com.mycompany.location.service;

import com.mycompany.location.entity.LocationEntity;
import com.mycompany.location.exception.BusinessException;
import com.mycompany.location.model.LocationRequestModel;
import com.mycompany.location.model.LocationResponseModel;
import com.mycompany.location.model.ServiceModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LocationService {
    ResponseEntity<String> checkValidUser(Long userId);
    List<ServiceModel> getServiceModelList(List<Integer> serviceIds);
    LocationResponseModel getLocationResponseModel(LocationEntity locationEntity);
    LocationResponseModel createLocation(LocationRequestModel locationRequestModel) throws BusinessException;
    LocationResponseModel deleteLocation(Long locationId) throws BusinessException;
    List<LocationResponseModel> getAllLocations() throws BusinessException;
    List<LocationResponseModel> filterByLocationType(String locationType) throws BusinessException;
    LocationResponseModel getLocationDetail(Long locationId) throws BusinessException;
    LocationResponseModel updateLocation(Long locationId, LocationRequestModel locationRequestModel) throws BusinessException;

}
