//package com.example.propertyservice.controller;
//
//import com.example.propertyservice.business.interfaces.PropertyService;
//import com.example.propertyservice.domain.dto.PropertyDto;
//import com.example.propertyservice.domain.request.PropertyRequest;
//import com.example.propertyservice.domain.response.PropertyResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/properties")
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "*")
//public class PropertyController {
//
//    private final PropertyService propertyService;
//
//    // ========== PROPERTY MANAGER ONLY - CREATE, UPDATE, DELETE ==========
//
//    @PreAuthorize("hasRole('ADMIN') or hasRole('PROPERTY_MANAGER')")
//   // @PreAuthorize("isAuthenticated()")
//    @PostMapping
//    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
//        log.info("REST request to create property: {}", propertyRequest.getTitle());
//
//        PropertyResponse response = propertyService.createProperty(propertyRequest);
//        return ResponseEntity.ok(response);
//    }
//
//    @PreAuthorize("hasRole('PROPERTY_MANAGER') or hasRole('ADMIN')")
//    @PutMapping("/{id}")
//    public ResponseEntity<PropertyResponse> updateProperty(
//            @PathVariable Long id,
//            @Valid @RequestBody PropertyRequest propertyRequest) {
//        log.info("REST request to update property with ID: {}", id);
//
//        PropertyResponse response = propertyService.updateProperty(propertyRequest, id);
//        return ResponseEntity.ok(response);
//    }
//
//    @PreAuthorize("hasRole('PROPERTY_MANAGER') or hasRole('ADMIN')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<PropertyResponse> deleteProperty(@PathVariable Long id) {
//        log.info("REST request to delete property with ID: {}", id);
//
//        PropertyResponse response = propertyService.deleteProperty(id);
//        return ResponseEntity.ok(response);
//    }
//
//    // ========== PUBLIC / AUTHENTICATED - VIEW PROPERTIES ==========
//
//    @GetMapping("/{id}")
//    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
//        log.info("REST request to get property with ID: {}", id);
//
//        PropertyResponse response = propertyService.getPropertyById(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<PropertyDto>> getAllProperties() {
//        log.info("REST request to get all properties");
//
//        List<PropertyDto> properties = propertyService.getAllProperties();
//        return ResponseEntity.ok(properties);
//    }
//
//    // ========== SEARCH ENDPOINTS - PUBLIC ==========
//
//    @PostMapping("/search/location")
//    public ResponseEntity<List<PropertyDto>> searchPropertiesByLocation(
//            @RequestBody PropertyRequest request) {
//        log.info("REST request to search properties by location: {}", request.getLocationType());
//
//        List<PropertyDto> properties = propertyService.getPropertiesByLocation(String.valueOf(request.getLocationType()));
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/location/{location}")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByLocationPath(
//            @PathVariable String location) {
//        log.info("REST request to search properties by location path: {}", location);
//
//        List<PropertyDto> properties = propertyService.getPropertiesByLocation(location);
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/house-type/{houseType}")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByHouseTypePath(
//            @PathVariable String houseType) {
//        log.info("REST request to search properties by house type via path: {}", houseType);
//
//        List<PropertyDto> properties = propertyService.getPropertiesByHouseType(houseType);
//        return ResponseEntity.ok(properties);
//    }
//
//    @PostMapping("/search/house-type")
//    public ResponseEntity<List<PropertyDto>> searchPropertiesByHouseType(
//            @RequestBody PropertyRequest request) {
//        log.info("REST request to search properties by house type: {}", request.getPropertyType());
//
//        List<PropertyDto> properties = propertyService.getPropertiesByHouseType(String.valueOf(request.getPropertyType()));
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/surface-area")
//    public ResponseEntity<List<PropertyDto>> getPropertiesBySurfaceArea(
//            @RequestParam String surfaceArea) {
//        log.info("REST request to search properties by surface area: {}", surfaceArea);
//
//        List<PropertyDto> properties = propertyService.getPropertiesBySurfaceArea(surfaceArea);
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/rent-amount")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByRentAmount(
//            @RequestParam BigDecimal maxRentAmount) {
//        log.info("REST request to search properties by max rent amount: {}", maxRentAmount);
//
//        List<PropertyDto> properties = propertyService.getPropertiesByRentaAmount(maxRentAmount);
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/interior")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByInterior(
//            @RequestParam String interior) {
//        log.info("REST request to search properties by interior: {}", interior);
//
//        List<PropertyDto> properties = propertyService.getInteriorProperties(interior);
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/advanced")
//    public ResponseEntity<List<PropertyDto>> advancedSearch(
//            @RequestParam(required = false) String location,
//            @RequestParam(required = false) String houseType,
//            @RequestParam(required = false) String surfaceArea,
//            @RequestParam(required = false) BigDecimal maxRentAmount,
//            @RequestParam(required = false) String interior,
//            @RequestParam(required = false) Integer minBedrooms,
//            @RequestParam(required = false) Integer maxBedrooms) {
//
//        log.info("REST request for advanced property search with multiple criteria");
//
//        List<PropertyDto> properties = propertyService.getAllProperties();
//
//        properties = properties.stream()
//                .filter(property -> location == null ||
//                        (property.getLocationType() != null &&
//                                property.getLocationType().name().equalsIgnoreCase(location)))
//                .filter(property -> houseType == null ||
//                        (property.getHouseType() != null &&
//                                property.getHouseType().name().equalsIgnoreCase(houseType)))
//                .filter(property -> surfaceArea == null ||
//                        (property.getSurfaceArea() != null &&
//                                property.getSurfaceArea().toLowerCase().contains(surfaceArea.toLowerCase())))
//                .filter(property -> maxRentAmount == null ||
//                        (property.getRentAmount() != null &&
//                                property.getRentAmount().compareTo(maxRentAmount) <= 0))
//                .filter(property -> interior == null ||
//                        (property.getInterior() != null &&
//                                property.getInterior().toLowerCase().contains(interior.toLowerCase())))
//                .filter(property -> minBedrooms == null ||
//                        (property.getBedrooms() != null &&
//                                property.getBedrooms() >= minBedrooms))
//                .filter(property -> maxBedrooms == null ||
//                        (property.getBedrooms() != null &&
//                                property.getBedrooms() <= maxBedrooms))
//                .toList();
//
//        return ResponseEntity.ok(properties);
//    }
//
//    @GetMapping("/search/price-range")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByPriceRange(
//            @RequestParam BigDecimal minPrice,
//            @RequestParam BigDecimal maxPrice) {
//        log.info("REST request to search properties by price range: {} - {}", minPrice, maxPrice);
//
//        List<PropertyDto> properties = propertyService.getAllProperties();
//        List<PropertyDto> filteredProperties = properties.stream()
//                .filter(property -> property.getRentAmount() != null &&
//                        property.getRentAmount().compareTo(minPrice) >= 0 &&
//                        property.getRentAmount().compareTo(maxPrice) <= 0)
//                .toList();
//
//        return ResponseEntity.ok(filteredProperties);
//    }
//
//    @GetMapping("/search/bedrooms")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByBedrooms(
//            @RequestParam Integer bedrooms) {
//        log.info("REST request to search properties by bedroom count: {}", bedrooms);
//
//        List<PropertyDto> properties = propertyService.getAllProperties();
//        List<PropertyDto> filteredProperties = properties.stream()
//                .filter(property -> property.getBedrooms() != null &&
//                        property.getBedrooms().equals(bedrooms))
//                .toList();
//
//        return ResponseEntity.ok(filteredProperties);
//    }
//
//    @GetMapping("/available")
//    public ResponseEntity<List<PropertyDto>> getAvailableProperties() {
//        log.info("REST request to get available properties only");
//
//        List<PropertyDto> properties = propertyService.getAllProperties();
//        List<PropertyDto> availableProperties = properties.stream()
//                .filter(property -> property.getQuantity() != null && property.getQuantity() > 0)
//                .toList();
//
//        return ResponseEntity.ok(availableProperties);
//    }
//}


// todo add pagination

package com.example.propertyservice.controller;

import com.example.propertyservice.business.interfaces.PropertyService;
import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.request.UpdatePropertyStatusRequest;
import com.example.propertyservice.domain.response.PropertyResponse;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyService propertyService;

    // ========== ADMIN + PROPERTY MANAGER ONLY - CREATE, UPDATE, DELETE ==========


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROPERTY_MANAGER')")
    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
        log.info("REST request to create property: {}", propertyRequest.getTitle());

        PropertyResponse response = propertyService.createProperty(propertyRequest);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROPERTY_MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest propertyRequest) {
        log.info("REST request to update property with ID: {}", id);

        PropertyResponse response = propertyService.updateProperty(propertyRequest, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/property/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPERTY_MANAGER')")
    public ResponseEntity<PropertyResponse> updatePropertyStatus(
            @PathVariable Long id,
            @RequestBody UpdatePropertyStatusRequest request) {

        log.info("REST request to update property status if property is rented: {}", request.getPropertyIsRented());

        PropertyResponse response = propertyService.updatePropertyStatus(id, request.getPropertyIsRented());
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PROPERTY_MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<PropertyResponse> deleteProperty(@PathVariable Long id) {
        log.info("REST request to delete property with ID: {}", id);

        PropertyResponse response = propertyService.deleteProperty(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        log.info("REST request to get property with ID: {}", id);

        PropertyResponse response = propertyService.getPropertyById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperties() {
        log.info("REST request to get all properties");

        List<PropertyDto> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Search endpoint for available properties
    @GetMapping("/available")
    public ResponseEntity<List<PropertyDto>> getAvailableProperties() {
        log.info("REST request to get available properties only");

        List<PropertyDto> properties = propertyService.getAllProperties();
        List<PropertyDto> availableProperties = properties.stream()
                .filter(property -> property.getQuantity() != null && property.getQuantity() > 0)
                .toList();

        return ResponseEntity.ok(availableProperties);
    }

    // ========== SEARCH ENDPOINTS - PUBLIC ==========


    @PostMapping("/search/location")
    public ResponseEntity<List<PropertyDto>> searchPropertiesByLocation(
            @RequestBody PropertyRequest request) {
        log.info("REST request to search properties by location: {}", request.getLocationType());

        List<PropertyDto> properties = propertyService.getPropertiesByLocation(
                String.valueOf(request.getLocationType()));
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/location/{location}")
    public ResponseEntity<List<PropertyDto>> getPropertiesByLocationPath(
            @PathVariable String location) {
        log.info("REST request to search properties by location path: {}", location);

        List<PropertyDto> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/house-type/{houseType}")
    public ResponseEntity<List<PropertyDto>> getPropertiesByHouseTypePath(
            @PathVariable String houseType) {
        log.info("REST request to search properties by house type via path: {}", houseType);

        List<PropertyDto> properties = propertyService.getPropertiesByHouseType(houseType);
        return ResponseEntity.ok(properties);
    }


    @PostMapping("/search/house-type")
    public ResponseEntity<List<PropertyDto>> searchPropertiesByHouseType(
            @RequestBody PropertyRequest request) {
        log.info("REST request to search properties by house type: {}", request.getPropertyType());

        List<PropertyDto> properties = propertyService.getPropertiesByHouseType(
                String.valueOf(request.getPropertyType()));
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/surface-area")
    public ResponseEntity<List<PropertyDto>> getPropertiesBySurfaceArea(
            @RequestParam String surfaceArea) {
        log.info("REST request to search properties by surface area: {}", surfaceArea);

        List<PropertyDto> properties = propertyService.getPropertiesBySurfaceArea(surfaceArea);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/rent-amount")
    public ResponseEntity<List<PropertyDto>> getPropertiesByRentAmount(
            @RequestParam BigDecimal maxRentAmount) {
        log.info("REST request to search properties by max rent amount: {}", maxRentAmount);

        List<PropertyDto> properties = propertyService.getPropertiesByRentaAmount(maxRentAmount);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/interior")
    public ResponseEntity<List<PropertyDto>> getPropertiesByInterior(
            @RequestParam String interior) {
        log.info("REST request to search properties by interior: {}", interior);

        List<PropertyDto> properties = propertyService.getInteriorProperties(interior);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/bedrooms")
    public ResponseEntity<List<PropertyDto>> getPropertiesByBedrooms(
            @RequestParam Integer bedrooms) {
        log.info("REST request to search properties by bedroom count: {}", bedrooms);

        List<PropertyDto> properties = propertyService.getAllProperties();
        List<PropertyDto> filteredProperties = properties.stream()
                .filter(property -> property.getBedrooms() != null &&
                        property.getBedrooms().equals(bedrooms))
                .toList();

        return ResponseEntity.ok(filteredProperties);
    }


    @GetMapping("/search/price-range")
    public ResponseEntity<List<PropertyDto>> getPropertiesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("REST request to search properties by price range: {} - {}", minPrice, maxPrice);

        List<PropertyDto> properties = propertyService.getAllProperties();
        List<PropertyDto> filteredProperties = properties.stream()
                .filter(property -> property.getRentAmount() != null &&
                        property.getRentAmount().compareTo(minPrice) >= 0 &&
                        property.getRentAmount().compareTo(maxPrice) <= 0)
                .toList();

        return ResponseEntity.ok(filteredProperties);
    }


    @GetMapping("/search/advanced")
    public ResponseEntity<List<PropertyDto>> advancedSearch(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String houseType,
            @RequestParam(required = false) String surfaceArea,
            @RequestParam(required = false) BigDecimal maxRentAmount,
            @RequestParam(required = false) String interior,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms) {

        log.info("REST request for advanced property search with multiple criteria");

        List<PropertyDto> properties = propertyService.getAllProperties();

        properties = properties.stream()
                .filter(property -> location == null ||
                        (property.getLocationType() != null &&
                                property.getLocationType().name().equalsIgnoreCase(location)))
                .filter(property -> houseType == null ||
                        (property.getHouseType() != null &&
                                property.getHouseType().name().equalsIgnoreCase(houseType)))
                .filter(property -> surfaceArea == null ||
                        (property.getSurfaceArea() != null &&
                                property.getSurfaceArea().toLowerCase().contains(surfaceArea.toLowerCase())))
                .filter(property -> maxRentAmount == null ||
                        (property.getRentAmount() != null &&
                                property.getRentAmount().compareTo(maxRentAmount) <= 0))
                .filter(property -> interior == null ||
                        (property.getInterior() != null &&
                                property.getInterior().toLowerCase().contains(interior.toLowerCase())))
                .filter(property -> minBedrooms == null ||
                        (property.getBedrooms() != null &&
                                property.getBedrooms() >= minBedrooms))
                .filter(property -> maxBedrooms == null ||
                        (property.getBedrooms() != null &&
                                property.getBedrooms() <= maxBedrooms))
                .toList();

        return ResponseEntity.ok(properties);
    }
}












