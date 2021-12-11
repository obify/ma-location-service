package com.mycompany.location.controller;

import com.mycompany.location.exception.BusinessException;
import com.mycompany.location.model.LocationRequestModel;
import com.mycompany.location.model.LocationResponseModel;
import com.mycompany.location.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/locations")
    public ResponseEntity<Object> createLocation(@RequestBody LocationRequestModel locationRequestModel) throws BusinessException {
        LocationResponseModel locationResponseModel = locationService.createLocation(locationRequestModel);
        return new ResponseEntity<>(locationResponseModel, HttpStatus.CREATED);
    }

    @DeleteMapping("/locations/{locationId}")
    public ResponseEntity<Object> deleteLocation(@PathVariable(value = "locationId") Long locationId) throws BusinessException {
        LocationResponseModel locationResponseModel = locationService.deleteLocation(locationId);
        return new ResponseEntity<>(locationResponseModel, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/locations")
    public ResponseEntity<Object> getAllLocations() throws BusinessException {
        List<LocationResponseModel> locationResponseModelList = locationService.getAllLocations();
        return new ResponseEntity<>(locationResponseModelList, HttpStatus.OK);
    }

    @GetMapping("/locations/filter")
    public ResponseEntity<Object> filterByLocationType(@RequestParam(value = "locationType") String locationType) throws BusinessException {
        List<LocationResponseModel> locationResponseModelList = locationService.filterByLocationType(locationType);
        return new ResponseEntity<>(locationResponseModelList, HttpStatus.OK);
    }

    @GetMapping("/locations/{locationId}")
    public ResponseEntity<Object> getLocationDetail(@PathVariable(value = "locationId") Long locationId) throws BusinessException {
        LocationResponseModel locationResponseModel = locationService.getLocationDetail(locationId);
        return new ResponseEntity<>(locationResponseModel, HttpStatus.OK);
    }

    @PostMapping("/locations/{locationId}")
    public ResponseEntity<Object> updateLocation(@PathVariable(value = "locationId") Long locationId, @RequestBody LocationRequestModel locationRequestModel) throws BusinessException {
        LocationResponseModel locationResponseModel = locationService.updateLocation(locationId, locationRequestModel);
        return new ResponseEntity<>(locationResponseModel, HttpStatus.CREATED);
    }

}
