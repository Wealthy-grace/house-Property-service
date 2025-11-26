package com.example.propertyservice.persistence.repository;

import com.example.propertyservice.persistence.model.HouseType;
import com.example.propertyservice.persistence.model.LocationType;
import com.example.propertyservice.persistence.model.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {


    // Find by location type
    List<PropertyEntity> findByLocationType(LocationType locationType);

    // Find by house type
    List<PropertyEntity> findByHouseType(HouseType houseType);

    // Find by surface area containing (case insensitive)
    List<PropertyEntity> findBySurfaceAreaContainingIgnoreCase(String surfaceArea);

    // Find by interior containing (case insensitive)
    List<PropertyEntity> findByInteriorContainingIgnoreCase(String interior);

    // Find properties with rent amount less than or equal to specified amount
    List<PropertyEntity> findByRentAmountLessThanEqual(BigDecimal rentAmount);

    boolean existsByLocationType(LocationType locationType);

    boolean existsByTitle(String title);



    boolean existsBySurfaceAreaContainingIgnoreCase(String surfaceArea);

    boolean existsByInteriorContainingIgnoreCase(String interior);

    boolean existsByDescription(String description);

    boolean existsByAddress(String address);

    boolean existsByPostalCode(String postalCode);

    boolean existsByRentAmountLessThanEqual(BigDecimal rentAmount);


    boolean existsByInterior(String interior);

    boolean existsBySurfaceArea(String surfaceArea);
}
