package com.mycompany.location.repository;

import com.mycompany.location.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    //List<AddressEntity> findByAddressLine1AndCityAndStateAndPincodeAllIgnoreCase(String addressLine1, String city, String state, String pincode);

}
