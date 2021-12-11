package com.mycompany.location.service;

import com.mycompany.location.entity.AddressEntity;
import com.mycompany.location.entity.LocationEntity;
import com.mycompany.location.entity.ServiceEntity;
import com.mycompany.location.entity.TimingsEntity;
import com.mycompany.location.exception.BusinessException;
import com.mycompany.location.model.*;
import com.mycompany.location.repository.AddressRepository;
import com.mycompany.location.repository.LocationRepository;
import com.mycompany.location.repository.ServiceRepository;
import com.mycompany.location.utility.LocationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private LocationConverter locationConverter;

    @Value("${user.api.url}")
    private String userApiBaseUrl;

    @Value("${position.api.url}")
    private String positionApiBaseUrl;

    @Value("${services.api.url}")
    private String servicesApiBaseUrl;

    /*
    @Value("${services.api.url1}")
    private String servicesApiBaseUrl1;

    @Value("${services.api.url2}")
    private String servicesApiBaseUrl2;
    */

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> checkValidUser(Long userId) {
        ResponseEntity<String> validUserResponse = restTemplate.getForEntity(userApiBaseUrl+"/{id}", String.class, userId);
        return validUserResponse;
    }

    @Override
    public List<ServiceModel> getServiceModelList(List<Integer> serviceIds)
    {
        List<ServiceModel> serviceModelList = new ArrayList<>();
        ServiceModel serviceModel =  null;

        for(int i = 0; i < serviceIds.size(); i++) {
            ResponseEntity<ServiceModel> res = restTemplate.getForEntity(servicesApiBaseUrl + "/{id}", ServiceModel.class, serviceIds.get(i));
            if (res.getStatusCode() == HttpStatus.OK) {
                serviceModel = res.getBody();
                serviceModelList.add(serviceModel);
            }
        }
        return serviceModelList;
    }

    @Override
    public LocationResponseModel getLocationResponseModel(LocationEntity locationEntity) {

        List<ServiceEntity> serviceEntityList = serviceRepository.findByLocationId(locationEntity.getId());
        List<Integer> serviceIds = new ArrayList<>();
        /*for(int j = 0; j < serviceEntityList.size(); j++)
        {
            serviceIds.add(serviceEntityList.get(j).getServiceId());
        }*/
        for(ServiceEntity serviceEntity : serviceEntityList)
        {
            serviceIds.add(serviceEntity.getServiceId());
        }
        List<ServiceModel> serviceModelList = getServiceModelList(serviceIds);

        LocationResponseModel locationResponseModel = locationConverter.entityToModel(locationEntity, serviceModelList);

        return locationResponseModel;
    }

    @Override
    public LocationResponseModel createLocation(LocationRequestModel locationRequestModel) throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;
        List<ServiceModel> serviceModelList = new ArrayList<>();

        LocationEntity locationEntity = null;

        if(locationRequestModel.getLocationType() == null || "".equals(locationRequestModel.getLocationType().trim()))
        {
            errorModel = new ErrorModel("CREATE_001", "Location Type is a mandatory Field for Creating a New Location!");
            errorModelList.add(errorModel);
        }
        else
        {
            if(locationRequestModel.getLocationType().equalsIgnoreCase("Office"))
            {
                locationRequestModel.setLocationType("Office");
            }
            else if(locationRequestModel.getLocationType().equalsIgnoreCase("ATM"))
            {
                locationRequestModel.setLocationType("ATM");
            }
            else
            {
                errorModel = new ErrorModel("CREATE_002", "Location Type can be either Office or ATM!");
                errorModelList.add(errorModel);
            }

        }
        if(locationRequestModel.getAddress() == null)
        {
            errorModel = new ErrorModel("CREATE_003", "Address is a mandatory Field for Creating a New Location!");
            errorModelList.add(errorModel);
        }
        else
        {
            if(locationRequestModel.getAddress().getAddressLine1() == null || "".equals(locationRequestModel.getAddress().getAddressLine1().trim()))
            {
                errorModel = new ErrorModel("CREATE_004", "AddressLine1 in Address is a mandatory Field for Creating a New Location!");
                errorModelList.add(errorModel);
            }
            if(locationRequestModel.getAddress().getCity() == null || "".equals(locationRequestModel.getAddress().getCity().trim()))
            {
                errorModel = new ErrorModel("CREATE_005", "City in Address is a mandatory Field for Creating a New Location!");
                errorModelList.add(errorModel);
            }
            if(locationRequestModel.getAddress().getState() == null || "".equals(locationRequestModel.getAddress().getState().trim()))
            {
                errorModel = new ErrorModel("CREATE_006", "State in Address is a mandatory Field for Creating a New Location!");
                errorModelList.add(errorModel);
            }
            if(locationRequestModel.getAddress().getPincode() == null || "".equals(locationRequestModel.getAddress().getPincode().trim()))
            {
                errorModel = new ErrorModel("CREATE_007", "PinCode in Address is a mandatory Field for Creating a New Location!");
                errorModelList.add(errorModel);
            }
        }
        if(locationRequestModel.getUserid() == null)
        {
            errorModel = new ErrorModel("CREATE_008", "UserID is a mandatory Field for Creating a New Location!");
            errorModelList.add(errorModel);
        }
        if(locationRequestModel.getStatus() == null || "".equals(locationRequestModel.getStatus().trim()))
        {
            errorModel = new ErrorModel("CREATE_009", "Status is a mandatory Field for Creating a New Location!");
            errorModelList.add(errorModel);
        }
        if(locationRequestModel.getTimings() == null)
        {
            errorModel = new ErrorModel("CREATE_010", "Timings is a mandatory Field for Creating a New Location!");
            errorModelList.add(errorModel);
        }
        else
        {
            if(locationRequestModel.getTimings().getMonToFriOpenTime() == null)
            {
                errorModel = new ErrorModel("CREATE_011", "MondayToFridayOpenTime in Timings should be provided for Creating a New Location!");
                errorModelList.add(errorModel);
            }
            if(locationRequestModel.getTimings().getMonToFriCloseTime() == null)
            {
                errorModel = new ErrorModel("CREATE_012", "MondayToFridayCloseTime in Timings should be provided for Creating a New Location!");
                errorModelList.add(errorModel);
            }
            if(locationRequestModel.getTimings().getSatOpenTime() == null ^ locationRequestModel.getTimings().getSatCloseTime() == null)
            {
                errorModel = new ErrorModel("CREATE_013", "SatOpenTime provided without SatCloseTime or vice versa");
                errorModelList.add(errorModel);
            }
            if(locationRequestModel.getTimings().getSunOpenTime() == null ^ locationRequestModel.getTimings().getSunCloseTime() == null)
            {
                errorModel = new ErrorModel("CREATE_014", "SunOpenTime provided without SunCloseTime or vice versa");
                errorModelList.add(errorModel);
            }
        }
        if(locationRequestModel.getServiceIds() == null || locationRequestModel.getServiceIds().isEmpty())
        {
            errorModel = new ErrorModel("CREATE_015", "ServiceId is a mandatory Field for Creating a New Location!");
            errorModelList.add(errorModel);
        }

        ResponseEntity<String> validUserResponse = checkValidUser(locationRequestModel.getUserid());

        if(errorModelList.isEmpty() && validUserResponse.getStatusCode()==HttpStatus.OK)
        {
            List<Integer> serviceIds = new ArrayList<>(locationRequestModel.getServiceIds());
            serviceModelList = getServiceModelList(serviceIds);

            AddressRequestModel addressRequestModel = locationRequestModel.getAddress();
/*
            List<AddressEntity> addressEntityList = addressRepository.findByAddressLine1AndCityAndStateAndPincodeAllIgnoreCase(
                    addressRequestModel.getAddressLine1(), addressRequestModel.getCity(), addressRequestModel.getState(), addressRequestModel.getPincode());

            if(!addressEntityList.isEmpty())
            {
                for(int i = 0; i < addressEntityList.size(); i++)
                {
                    Long addressId = addressEntityList.get(i).getId();
                    Optional<LocationEntity> optLocationEntity = locationRepository.findByLocationTypeIgnoreCaseAndAddressId(
                            locationRequestModel.getLocationType(), addressId);
                    if (optLocationEntity.isPresent())
                    {
                        errorModel = new ErrorModel("CREATE_015", "Duplicate Address based on AddressLine1, City, State, Pincode and Location Type Entered!");
                        errorModelList.add(errorModel);
                        throw new BusinessException(errorModelList);
                    }
                }
            }
*/
            Optional<LocationEntity> optLocationEntity =
                    locationRepository.findByLocationTypeAndAddress_AddressLine1AndAddressCityAndAddressStateAndAddressPincodeAllIgnoreCase(locationRequestModel.getLocationType(), addressRequestModel.getAddressLine1(), addressRequestModel.getCity(), addressRequestModel.getState(), addressRequestModel.getPincode());
            if (optLocationEntity.isPresent())
            {
                errorModel = new ErrorModel("CREATE_016", "Duplicate Address based on AddressLine1, City, State, Pincode and Location Type Entered!");
                errorModelList.add(errorModel);
                throw new BusinessException(errorModelList);
            }

            List<ResultModel> resultModelList = new ArrayList<>();
            ResultModel resultModel = null;

            String query = addressRequestModel.getAddressLine1().trim() + ", "
                    + addressRequestModel.getCity().trim() + ", " + addressRequestModel.getState().trim();
            //query = "1600 Pennsylvania Ave NW, Washington DC";
            ResponseEntity<PositionStackResponseModel> res = restTemplate.getForEntity
                    (positionApiBaseUrl+"&query={query}"+"&country=IN&limit=1",
                            PositionStackResponseModel.class, query);
            if(res.getStatusCode() == HttpStatus.OK && res.getBody()!=null)
            {
                PositionStackResponseModel positionStackResponseModel = res.getBody();
                if(positionStackResponseModel != null)
                {
                    resultModelList = positionStackResponseModel.getData();
                    if(resultModelList == null || resultModelList.isEmpty())
                    {
                        errorModel = new ErrorModel("CREATE_017", "Latitude and Longitude Information could not be fetched properly!");
                        errorModelList.add(errorModel);
                        throw new BusinessException(errorModelList);
                    }
                }
            }

            resultModel = resultModelList.get(0);

            locationEntity = locationConverter.modelToEntity(locationRequestModel);
            locationEntity.getAddress().setLatitude(resultModel.getLatitude().toString());
            locationEntity.getAddress().setLongitude(resultModel.getLongitude().toString());
            locationEntity = locationRepository.save(locationEntity);

            for(Integer serviceId: serviceIds)
            {
                ServiceEntity serviceEntity = new ServiceEntity();
                serviceEntity.setLocationId(locationEntity.getId());
                serviceEntity.setServiceId(serviceId);
                serviceRepository.save(serviceEntity);
            }
            return locationConverter.entityToModel(locationEntity, serviceModelList);
        }

        throw new BusinessException(errorModelList);
    }

    @Override
    public LocationResponseModel deleteLocation(Long locationId) throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;

        LocationResponseModel locationResponseModel = null;

        Optional<LocationEntity> optLocationEntity = locationRepository.findById(locationId);
        if(optLocationEntity.isPresent())
        {
            LocationEntity locationEntity = optLocationEntity.get();
            locationResponseModel = getLocationResponseModel(locationEntity);
            List<ServiceEntity> serviceEntityList = serviceRepository.findByLocationId(locationId);
            locationRepository.delete(locationEntity);
            serviceRepository.deleteAll(serviceEntityList);
        }
        else
        {
            errorModel = new ErrorModel("DELETE_001", "No Location information available for the LocationId provided!");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        return locationResponseModel;
    }

    @Override
    @Cacheable(cacheNames = "allLocationsCache")
    public List<LocationResponseModel> getAllLocations() throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;

        List<LocationResponseModel> locationResponseModelList = new ArrayList<>();

        List<LocationEntity> locationEntityList = locationRepository.findAll();

        if(locationEntityList.isEmpty())
        {
            errorModel = new ErrorModel("FETCHALL_001", "No Location information available!");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        for(int i = 0; i < locationEntityList.size(); i++)
        {
            LocationEntity locationEntity = locationEntityList.get(i);

            LocationResponseModel locationResponseModel = getLocationResponseModel(locationEntity);
            locationResponseModelList.add(locationResponseModel);
        }

        return locationResponseModelList;
    }

    @Override
    @Cacheable(cacheNames = "allLocationsCache", key = "#locationType")
    public List<LocationResponseModel> filterByLocationType(String locationType) throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;

        List<LocationResponseModel> locationResponseModelList = new ArrayList<>();

        List<LocationEntity> locationEntityList = locationRepository.findByLocationTypeIgnoreCase(locationType);
        if(!locationEntityList.isEmpty())
        {
            for(LocationEntity locationEntity : locationEntityList)
            {
                LocationResponseModel locationResponseModel = getLocationResponseModel(locationEntity);
                locationResponseModelList.add(locationResponseModel);
            }
        }
        else
        {
            errorModel = new ErrorModel("FETCH_001", "No Location information available for the LocationType provided!");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        return locationResponseModelList;
    }

    @Override
    @Cacheable(cacheNames = "allLocationsCache", key = "#locationId")
    public LocationResponseModel getLocationDetail(Long locationId) throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;

        LocationResponseModel locationResponseModel = null;

        Optional<LocationEntity> optLocationEntity = locationRepository.findById(locationId);
        if(optLocationEntity.isPresent())
        {
            LocationEntity locationEntity = optLocationEntity.get();
            locationResponseModel = getLocationResponseModel(locationEntity);
        }
        else
        {
            errorModel = new ErrorModel("FETCH_002", "No Location information available for the LocationId provided!");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        return locationResponseModel;
    }

    @Override
    public LocationResponseModel updateLocation(Long locationId, LocationRequestModel locationRequestModel) throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;
        List<ServiceModel> serviceModelList = new ArrayList<>();

        LocationEntity locationEntity = null;

        Optional<LocationEntity> locationEntityOpt = locationRepository.findById(locationId);
        if(locationEntityOpt.isPresent())
        {
            locationEntity = locationEntityOpt.get();
        }
        if(locationEntity == null)
        {
            errorModel = new ErrorModel("UPDATE_001", "No Location information available for the LocationId provided!");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }
        if(locationRequestModel == null || (
                (locationRequestModel.getLocationType() == null || "".equals(locationRequestModel.getLocationType().trim())) &&
                (locationRequestModel.getAddress() == null ||
                        (
                        (locationRequestModel.getAddress().getAddressLine1() == null || "".equals(locationRequestModel.getAddress().getAddressLine1().trim())) &&
                        (locationRequestModel.getAddress().getCity() == null || "".equals(locationRequestModel.getAddress().getCity().trim())) &&
                        (locationRequestModel.getAddress().getState() == null || "".equals(locationRequestModel.getAddress().getState().trim())) &&
                        (locationRequestModel.getAddress().getPincode() == null || "".equals(locationRequestModel.getAddress().getPincode().trim()))
                        )
                ) &&
                (locationRequestModel.getUserid() == null) &&
                (locationRequestModel.getStatus() == null || "".equals(locationRequestModel.getStatus().trim())) &&
                (locationRequestModel.getTimings() == null ||
                        (
                        (locationRequestModel.getTimings().getMonToFriOpenTime() == null) &&
                        (locationRequestModel.getTimings().getMonToFriCloseTime() == null) &&
                        (locationRequestModel.getTimings().getSatOpenTime() == null) &&
                        (locationRequestModel.getTimings().getSatCloseTime() == null) &&
                        (locationRequestModel.getTimings().getSunOpenTime() == null) &&
                        (locationRequestModel.getTimings().getSunCloseTime() == null)
                        )
                ) &&
                (locationRequestModel.getServiceIds() == null || locationRequestModel.getServiceIds().isEmpty())
                                            )
        )
        {
            errorModel = new ErrorModel("UPDATE_002", "No input data provided!");
            errorModelList.add(errorModel);
            throw new BusinessException(errorModelList);
        }

        if(!(locationRequestModel.getLocationType() == null || "".equals(locationRequestModel.getLocationType().trim())))
        {
            if(locationRequestModel.getLocationType().equalsIgnoreCase("Office"))
            {
                locationEntity.setLocationType("Office");
            }
            else if(locationRequestModel.getLocationType().equalsIgnoreCase("ATM"))
            {
                locationEntity.setLocationType("ATM");
            }
            else
            {
                errorModel= new ErrorModel("UPDATE_003", "Location Type can be either Office or ATM!");
                errorModelList.add(errorModel);
            }
        }

        if(locationRequestModel.getAddress() != null)
        {
            AddressRequestModel addressRequestModel = locationRequestModel.getAddress();
            AddressEntity addressEntity = locationEntity.getAddress();
            if(!(addressRequestModel.getAddressLine1() == null || "".equals(addressRequestModel.getAddressLine1().trim())))
            {
                addressEntity.setAddressLine1(addressRequestModel.getAddressLine1().trim());
            }

            if(addressRequestModel.getAddressLine2() != null)
                addressEntity.setAddressLine2(addressRequestModel.getAddressLine2().trim());
            else
                addressEntity.setAddressLine2(addressRequestModel.getAddressLine2());
            if(addressRequestModel.getStreet() != null)
                addressEntity.setStreet(addressRequestModel.getStreet().trim());
            else
                addressEntity.setStreet(addressRequestModel.getStreet());

            if(!(addressRequestModel.getCity() == null || "".equals(addressRequestModel.getCity().trim())))
            {
                addressEntity.setCity(addressRequestModel.getCity().trim());
            }
            if(!(addressRequestModel.getState() == null || "".equals(addressRequestModel.getState().trim())))
            {
                addressEntity.setState(addressRequestModel.getState().trim());
            }
            if(!(addressRequestModel.getPincode() == null || "".equals(addressRequestModel.getPincode().trim())))
            {
                addressEntity.setPincode(addressRequestModel.getPincode().trim());
            }
            locationEntity.setAddress(addressEntity);
        }

        if(locationRequestModel.getUserid() != null)
        {
            locationEntity.setUserid(locationRequestModel.getUserid());
        }

        if(!(locationRequestModel.getStatus() == null || "".equals(locationRequestModel.getStatus().trim())))
        {
            locationEntity.setStatus(locationRequestModel.getStatus().trim());
        }

        if(locationRequestModel.getTimings() != null)
        {
            TimingsRequestModel timingsRequestModel = locationRequestModel.getTimings();
            TimingsEntity timingsEntity = locationEntity.getTimings();
            if(timingsRequestModel.getMonToFriOpenTime() != null)
            {
                timingsEntity.setMonToFriOpenTime(timingsRequestModel.getMonToFriOpenTime());
            }
            if(timingsRequestModel.getMonToFriCloseTime() != null)
            {
                timingsEntity.setMonToFriCloseTime(timingsRequestModel.getMonToFriCloseTime());
            }
            if(timingsRequestModel.getSatOpenTime() == null ^ timingsRequestModel.getSatCloseTime() == null)
            {
                errorModel = new ErrorModel("UPDATE_004", "SatOpenTime provided without SatCloseTime or vice versa");
                errorModelList.add(errorModel);
            }
            else
            {
                timingsEntity.setSatOpenTime(timingsRequestModel.getSatOpenTime());
                timingsEntity.setSatCloseTime(timingsRequestModel.getSatCloseTime());
            }
            if(timingsRequestModel.getSunOpenTime() == null ^ timingsRequestModel.getSunCloseTime() == null)
            {
                errorModel = new ErrorModel("UPDATE_005", "SunOpenTime provided without SunCloseTime or vice versa");
                errorModelList.add(errorModel);
            }
            else
            {
                timingsEntity.setSunOpenTime(timingsRequestModel.getSunOpenTime());
                timingsEntity.setSunCloseTime(timingsRequestModel.getSunCloseTime());
            }
            locationEntity.setTimings(timingsEntity);
        }

        List<Integer> serviceIds = new ArrayList<>();
        List<ServiceEntity> serviceEntityList = serviceRepository.findByLocationId(locationId);

        for(ServiceEntity serviceEntity: serviceEntityList)
        {
            serviceIds.add(serviceEntity.getServiceId());
        }

        List<Integer> serviceIds1;
        List<ServiceModel> serviceModelList1 = new ArrayList<>();
        List<ServiceEntity> serviceEntityList1 = new ArrayList<>();
        if(!(locationRequestModel.getServiceIds() == null || locationRequestModel.getServiceIds().isEmpty()))
        {
            serviceIds1 = new ArrayList<>(locationRequestModel.getServiceIds());
            Collections.sort(serviceIds1);
            Collections.sort(serviceIds);
            if(!(serviceIds1.equals(serviceIds)))
            {
                serviceModelList1 = getServiceModelList(serviceIds1);
                for(Integer serviceId1: serviceIds1)
                {
                    ServiceEntity serviceEntity1 = new ServiceEntity();
                    serviceEntity1.setLocationId(locationId);
                    serviceEntity1.setServiceId(serviceId1);
                    serviceEntityList1.add(serviceEntity1);
                }
            }
        }

        ResponseEntity<String> validUserResponse = checkValidUser(locationEntity.getUserid());

        if(errorModelList.isEmpty() && validUserResponse.getStatusCode()==HttpStatus.OK)
        {
            Optional<LocationEntity> locationEntityOpt1 =
                    locationRepository.findByLocationTypeAndAddress_AddressLine1AndAddressCityAndAddressStateAndAddressPincodeAllIgnoreCase(
                            locationEntity.getLocationType(), locationEntity.getAddress().getAddressLine1(), locationEntity.getAddress().getCity(), locationEntity.getAddress().getState(), locationEntity.getAddress().getPincode());

            if(locationEntityOpt1.isPresent())
            {
                if(locationEntity.getId() != locationEntityOpt1.get().getId())
                {
                    errorModel =  new ErrorModel("UPDATE_006","Already Location Id: "+ locationEntityOpt1.get().getId() +" found with same Address based on AddressLine1, City, State, Pincode and Location Type Entered!");
                    errorModelList.add(errorModel);
                    throw new BusinessException(errorModelList);
                }
            }

            List<ResultModel> resultModelList = new ArrayList<>();
            ResultModel resultModel = null;

            String query = locationEntity.getAddress().getAddressLine1().trim()+ ", "
                    +locationEntity.getAddress().getCity().trim() + ", " + locationEntity.getAddress().getState().trim();

            ResponseEntity<PositionStackResponseModel> res = restTemplate.getForEntity(positionApiBaseUrl + "&query={query}", PositionStackResponseModel.class, query);

            if(res.getStatusCode() == HttpStatus.OK && res.getBody() != null)
            {
                PositionStackResponseModel positionStackResponseModel = res.getBody();
                if(positionStackResponseModel != null)
                {
                    resultModelList = positionStackResponseModel.getData();
                    if(resultModelList == null || resultModelList.isEmpty())
                    {
                        errorModel = new ErrorModel("UPDATE_007", "Latitude and Longitude Information could not be fetched properly!");
                        errorModelList.add(errorModel);
                        throw new BusinessException(errorModelList);
                    }
                }
            }

            resultModel = resultModelList.get(0);

            locationEntity.getAddress().setLatitude(resultModel.getLatitude().toString());
            locationEntity.getAddress().setLongitude(resultModel.getLongitude().toString());
            locationEntity = locationRepository.save(locationEntity);

            if(!(serviceModelList1 == null || serviceModelList1.isEmpty()))
            {
                serviceRepository.deleteAll(serviceEntityList);
                serviceRepository.saveAll(serviceEntityList1);
                return locationConverter.entityToModel(locationEntity, serviceModelList1);
            }
            else
            {
                serviceModelList = getServiceModelList(serviceIds);
                return locationConverter.entityToModel(locationEntity, serviceModelList);
            }

        }
        throw new BusinessException(errorModelList);
    }

    /*
    @Override
    public LocationResponseModel createLocation(LocationRequestModel locationRequestModel) throws BusinessException {

        List<ErrorModel> errorModelList = new ArrayList<>();
        ErrorModel errorModel = null;
        List<ServiceRequestModel> serviceRequestModelList = new ArrayList<>();

        if(!locationRequestModel.getServiceIds().isEmpty())
        {
            List<Integer> serviceIds = new ArrayList<>(locationRequestModel.getServiceIds());
            for(int i = 0; i<locationRequestModel.getServiceIds().size(); i++)
            {
                ServiceRequestModel serviceRequestModel = new ServiceRequestModel();
                ResponseEntity<ServiceRequestModel> res = restTemplate.getForEntity(servicesApiBaseUrl+"/{id}", ServiceRequestModel.class, serviceIds.get(i));
                if(res.getStatusCode()== HttpStatus.OK)
                {
                    serviceRequestModel = res.getBody();
                    serviceRequestModelList.add(serviceRequestModel);
                }
                else
                {
                    errorModel = new ErrorModel("CREATE_002","Error fetching details of Service ID " + serviceRequestModel.getId() + " !");
                    errorModelList.add(errorModel);
                }
            }
            if(errorModelList.isEmpty())
            {
                locationRequestModel.setServices(serviceRequestModelList);
                LocationEntity locationEntity = locationConverter.modelToEntity(locationRequestModel);
                locationEntity = locationRepository.save(locationEntity);
                return locationConverter.entityToModel(locationEntity);
            }
        }
        else
        {
            errorModel = new ErrorModel("CREATE_003","Please provide at least one service available at this location!");
            errorModelList.add(errorModel);
        }
        throw new BusinessException(errorModelList);
    }
    */

    /*
    @Override
    public void getAllLocations() {
        String msg1 = restTemplate.getForObject(userApiBaseUrl+"/checkToken/"+"{id}", String.class, 4);
        //http://localhost:8080/api/v1/users/checkToken/1
        System.out.println(msg1);

        String query = "1600 Pennsylvania Ave NW, Washington DC";
        String msg2 = restTemplate.getForObject(positionApiBaseUrl+"&query={query}", String.class, query);
        //http://api.positionstack.com/v1/forward?access_key=1b8722899cdae4f1d8a351878fa81dd5&query=1600 Pennsylvania Ave NW, Washington DC
        System.out.println(msg2);

        /*
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ServicesRawModel servicesRawModel = null;
        HttpEntity<ServicesRawModel> httpEntity = new HttpEntity<>(httpHeaders);
        //ServicesRawModel servicesRawModel = restTemplate.getForObject(servicesApiBaseUrl1+"?serviceId={id}", ServicesRawModel.class, 112);
        //ResponseEntity<ServicesRawModel> res = restTemplate.exchange(servicesApiBaseUrl1+"?serviceId={id}", HttpMethod.GET, httpEntity, ServicesRawModel.class, 112);
        ResponseEntity<ServicesRawModel> res = null;
        try {
            res = restTemplate.exchange(servicesApiBaseUrl1, HttpMethod.GET, httpEntity, ServicesRawModel.class);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //http://training-resource.epizy.com/services.php?serviceId=112
        System.out.println(res.getBody().getStatus());
        System.out.println(res.getBody().getService().getId());
        System.out.println(res.getBody().getService().getTitle());

        String msg4 = restTemplate.getForObject(servicesApiBaseUrl1, String.class);
        //String msg4 = restTemplate.getForObject(servicesApiBaseUrl1+"?serviceId={id}", String.class, 112);
            //http://businessoffering.iceiy.com/services.php?serviceId=111
        System.out.println(msg4);

    }

     */
}

            /*if(res.getStatusCode()== HttpStatus.OK)
                {
                    serviceRequestModel = res.getBody();
                    serviceRequestModelList.add(serviceRequestModel);
                }
                else
                {
                    errorModel = new ErrorModel("CREATE_002","Error fetching details of Service ID " + serviceRequestModel.getId() + " !");
                    errorModelList.add(errorModel);
                }*/

        /*if(locationRequestModel.getTimings() == null)
        {
        errorModel = new ErrorModel("CREATE_009", "Timings is a mandatory Field for Creating a New Location!");
        errorModelList.add(errorModel);
        }
        else
        {
        if((locationRequestModel.getTimings().getMonToFriOpenTime() == null || locationRequestModel.getTimings().getMonToFriCloseTime() == null)
        && (locationRequestModel.getTimings().getSatOpenTime() == null || locationRequestModel.getTimings().getSatCloseTime() == null)
        && (locationRequestModel.getTimings().getSunOpenTime() == null || locationRequestModel.getTimings().getSunCloseTime() == null))
        {
        errorModel = new ErrorModel("CREATE_010", "At least one Opening & Closing Time Span in Timings should be provided for Creating a New Location!");
        errorModelList.add(errorModel);
        }
        if((locationRequestModel.getTimings().getMonToFriOpenTime() == null ^ locationRequestModel.getTimings().getMonToFriCloseTime() == null)
        || (locationRequestModel.getTimings().getSatOpenTime() == null ^ locationRequestModel.getTimings().getSatCloseTime() == null)
        || (locationRequestModel.getTimings().getSunOpenTime() == null ^ locationRequestModel.getTimings().getSunCloseTime() == null))
        {
        errorModel = new ErrorModel("CREATE_011", "Both Opening & Closing Time Span in Timings should be provided for Creating a New Location!");
        errorModelList.add(errorModel);
        }
        }*/