package com.mycompany.location.repository;

import com.mycompany.location.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    //Optional<LocationEntity> findByLocationTypeIgnoreCaseAndAddressId(String locationType, Long id);

    Optional<LocationEntity> findByLocationTypeAndAddress_AddressLine1AndAddressCityAndAddressStateAndAddressPincodeAllIgnoreCase(String locationType, String addressLine1, String city, String state, String pincode);

    List<LocationEntity> findByLocationTypeIgnoreCase(String locationType);

}
