package com.example.propertyservice.business;

import static org.junit.jupiter.api.Assertions.*;


import com.example.propertyservice.business.impl.PropertyServiceImpl;
import com.example.propertyservice.business.mapper.PropertyMapperDto;
import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.response.PropertyResponse;
import com.example.propertyservice.exception.PropertyAlreadyExistsException;
import com.example.propertyservice.exception.PropertyNotFoundException;
import com.example.propertyservice.persistence.model.HouseType;
import com.example.propertyservice.persistence.model.LocationType;
import com.example.propertyservice.persistence.model.PropertyEntity;
import com.example.propertyservice.persistence.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private PropertyRequest propertyRequest;
    private PropertyEntity propertyEntity;
    private PropertyDto propertyDto;

    @BeforeEach
    void setUp() {
        propertyRequest = PropertyRequest.builder()
                .title("Test Property")
                .description("Test Description")
                .propertyType(HouseType.Apartment)
                .quantity(1)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("Test Street 123")
                .rentalcondition("No pets allowed")
                .surfaceArea("80 m²")
                .postalCode("5012AB")
                .interior("Fully furnished")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .image("image1.jpg")
                .image2("image2.jpg")
                .image3("image3.jpg")
                .image4("image4.jpg")
                .build();

        propertyEntity = PropertyEntity.builder()
                .id(1L)
                .title("Test Property")
                .description("Test Description")
                .houseType(HouseType.Apartment)
                .quantity(1)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .address("Test Street 123")
                .rentalcondition("No pets allowed")
                .surfaceArea("80 m²")
                .postalCode("5012AB")
                .interior("Fully furnished")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .image("image1.jpg")
                .image2("image2.jpg")
                .image3("image3.jpg")
                .image4("image4.jpg")
                .build();

        propertyDto = PropertyDto.builder()
                .id(1L)
                .title("Test Property")
                .description("Test Description")
                .houseType(HouseType.Apartment)
                .quantity(1)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .address("Test Street 123")
                .rentalcondition("No pets allowed")
                .surfaceArea("80 m²")
                .postalCode("5012AB")
                .interior("Fully furnished")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .image("image1.jpg")
                .image2("image2.jpg")
                .image3("image3.jpg")
                .image4("image4.jpg")
                .build();
    }

    @Test
    void createProperty_Success() {
        // Arrange
        when(propertyRepository.existsByTitle(anyString())).thenReturn(false);
        when(propertyRepository.existsByPostalCode(anyString())).thenReturn(false);
        when(propertyRepository.existsByAddress(anyString())).thenReturn(false);
        when(propertyRepository.existsByInterior(anyString())).thenReturn(false);
        when(propertyRepository.existsBySurfaceArea(anyString())).thenReturn(false);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapPropertyEntity(propertyRequest))
                    .thenReturn(propertyEntity);
            when(propertyRepository.save(propertyEntity)).thenReturn(propertyEntity);

            // Act
            PropertyResponse response = propertyService.createProperty(propertyRequest);

            // Assert
            assertNotNull(response);
            assertEquals("Property created successfully", response.getMessage());
            assertEquals(propertyEntity.getId(), response.getPropertyId());
            assertEquals(propertyEntity.getTitle(), response.getTitle());
            verify(propertyRepository).save(propertyEntity);
        }
    }

    @Test
    void createProperty_TitleAlreadyExists_ThrowsException() {
        // Arrange
        when(propertyRepository.existsByTitle("Test Property")).thenReturn(true);

        // Act & Assert
        PropertyAlreadyExistsException exception = assertThrows(
                PropertyAlreadyExistsException.class,
                () -> propertyService.createProperty(propertyRequest)
        );
        assertEquals("Property already exists with :Test Property", exception.getMessage());
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void createProperty_PostalCodeAlreadyExists_ThrowsException() {
        // Arrange
        when(propertyRepository.existsByTitle(anyString())).thenReturn(false);
        when(propertyRepository.existsByPostalCode("5012AB")).thenReturn(true);

        // Act & Assert
        PropertyAlreadyExistsException exception = assertThrows(
                PropertyAlreadyExistsException.class,
                () -> propertyService.createProperty(propertyRequest)
        );
        assertEquals("Property already exists with :5012AB", exception.getMessage());
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void createProperty_AddressAlreadyExists_ThrowsException() {
        // Arrange
        when(propertyRepository.existsByTitle(anyString())).thenReturn(false);
        when(propertyRepository.existsByPostalCode(anyString())).thenReturn(false);
        when(propertyRepository.existsByAddress("Test Street 123")).thenReturn(true);

        // Act & Assert
        PropertyAlreadyExistsException exception = assertThrows(
                PropertyAlreadyExistsException.class,
                () -> propertyService.createProperty(propertyRequest)
        );
        assertEquals("Property already exists with :Test Street 123", exception.getMessage());
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void updateProperty_Success() {
        // Arrange
        when(propertyRepository.existsById(1L)).thenReturn(true);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapPropertyEntity(propertyRequest))
                    .thenReturn(propertyEntity);
            when(propertyRepository.save(propertyEntity)).thenReturn(propertyEntity);

            // Act
            PropertyResponse response = propertyService.updateProperty(propertyRequest, 1L);

            // Assert
            assertNotNull(response);
            assertEquals("Property updated successfully", response.getMessage());
            assertTrue(response.isSuccess());
            assertEquals(1L, response.getPropertyId());
            verify(propertyRepository).save(propertyEntity);
        }
    }

    @Test
    void updateProperty_PropertyNotFound_ThrowsException() {
        // Arrange
        when(propertyRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        PropertyNotFoundException exception = assertThrows(
                PropertyNotFoundException.class,
                () -> propertyService.updateProperty(propertyRequest, 999L)
        );
        assertEquals("Property not found with ID: 999", exception.getMessage());
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void deleteProperty_Success() {
        // Arrange
        when(propertyRepository.existsById(1L)).thenReturn(true);

        // Act
        PropertyResponse response = propertyService.deleteProperty(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Property deleted successfully", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getPropertyId());
        verify(propertyRepository).deleteById(1L);
    }

    @Test
    void deleteProperty_PropertyNotFound_ThrowsException() {
        // Arrange
        when(propertyRepository.existsById(999l)).thenReturn(false);

        // Act & Assert
        PropertyNotFoundException exception = assertThrows(
                PropertyNotFoundException.class,
                () -> propertyService.deleteProperty(999L)
        );
        assertEquals("Property not found with ID: 999", exception.getMessage());
        verify(propertyRepository, never()).deleteById(any());
    }

    @Test
    void getPropertyById_Success() {
        // Arrange
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(propertyEntity));

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            PropertyResponse response = propertyService.getPropertyById(1L);

            // Assert
            assertNotNull(response);
            assertEquals("Property retrieved successfully", response.getMessage());
            assertTrue(response.isSuccess());
            assertEquals(1L, response.getPropertyId());
            assertEquals("Test Property", response.getTitle());
        }
    }

    @Test
    void getPropertyById_PropertyNotFound_ThrowsException() {
        // Arrange
        when(propertyRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        PropertyNotFoundException exception = assertThrows(
                PropertyNotFoundException.class,
                () -> propertyService.getPropertyById(999L)
        );
        assertEquals("Property not found with ID: 999", exception.getMessage());
    }

    @Test
    void getPropertiesByLocation_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByLocationType(LocationType.TILBURG)).thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getPropertiesByLocation("TILBURG");

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }
    }

    @Test
    void getPropertiesByLocation_InvalidLocation_ReturnsEmptyList() {
        // Act
        List<PropertyDto> result = propertyService.getPropertiesByLocation("INVALID_LOCATION");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPropertiesByHouseType_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByHouseType(HouseType.Apartment)).thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getPropertiesByHouseType("Apartment");

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }
    }


    @Test
    void getPropertiesByLocationType_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByLocationType(LocationType.TILBURG)).thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getPropertiesByLocation("TILBURG");

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }


    }

    @Test
    void getPropertiesByHouseType_InvalidHouseType_ReturnsEmptyList() {
        // Act
        List<PropertyDto> result = propertyService.getPropertiesByHouseType("INVALID_TYPE");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPropertiesBySurfaceArea_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findBySurfaceAreaContainingIgnoreCase("80")).thenReturn(properties);

        // Act
        List<PropertyDto> result = propertyService.getPropertiesBySurfaceArea("80");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPropertiesByRentAmount_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByRentAmountLessThanEqual(BigDecimal.valueOf(2000.00)))
                .thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getPropertiesByRentaAmount(BigDecimal.valueOf(2000.00));

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }
    }

    @Test
    void getInteriorProperties_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByInteriorContainingIgnoreCase("furnished")).thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getInteriorProperties("furnished");

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }
    }

    @Test
    void getAllProperties_Success() {
        // Arrange
        PropertyEntity property1 = PropertyEntity.builder()
                .id(1L)
                .title("Property 1")
                .description("Description 1")
                .houseType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .quantity(1)
                .bedrooms(2)
                .build();

        PropertyEntity property2 = PropertyEntity.builder()
                .id(2L)
                .title("Property 2")
                .description("Description 2")
                .houseType(HouseType.Residential_House)
                .locationType(LocationType.VELDHOVEN)
                .rentAmount(BigDecimal.valueOf(2000.00))
                .securityDeposit(BigDecimal.valueOf(4000.00))
                .quantity(1)
                .bedrooms(3)
                .build();

        List<PropertyEntity> properties = Arrays.asList(property1, property2);
        when(propertyRepository.findAll()).thenReturn(properties);

        PropertyDto dto1 = PropertyDto.builder()
                .id(1L)
                .title("Property 1")
                .houseType(HouseType.Apartment)
                .build();

        PropertyDto dto2 = PropertyDto.builder()
                .id(2L)
                .title("Property 2")
                .houseType(HouseType.Studio)
                .build();

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(property1)).thenReturn(dto1);
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(property2)).thenReturn(dto2);

            // Act
            List<PropertyDto> result = propertyService.getAllProperties();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Property 1", result.get(0).getTitle());
            assertEquals("Property 2", result.get(1).getTitle());
        }
    }

    @Test
    void getAllProperties_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(propertyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<PropertyDto> result = propertyService.getAllProperties();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void NewProperty_createProperty_Success() {
        // Arrange
        PropertyRequest propertyRequest = PropertyRequest.builder()
                .title("Test Property")
                .description("Test Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .build();

        PropertyEntity propertyEntity = PropertyEntity.builder()
                .title("Test Property")
                .houseType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .build();

        when(propertyRepository.save(propertyEntity)).thenReturn(propertyEntity);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapPropertyEntity(propertyRequest))
                    .thenReturn(propertyEntity);

            // Act
            PropertyResponse result = propertyService.createProperty(propertyRequest);

            // Assert
            assertNotNull(result);
            assertEquals("Test Property", result.getTitle());
        }
    }

    // get properties by house type

    @Test
    void searchPropertiesByHouseType_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByHouseType(HouseType.Apartment)).thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getPropertiesByHouseType(String.valueOf(HouseType.Apartment));

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }


    }

    @Test
    void searchPropertiesByHouseType_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(propertyRepository.findByHouseType(HouseType.Apartment)).thenReturn(Collections.emptyList());

        // Act
        List<PropertyDto> result = propertyService.getPropertiesByHouseType(String.valueOf(HouseType.Apartment));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchPropertiesByLocation_Success() {
        // Arrange
        List<PropertyEntity> properties = Arrays.asList(propertyEntity);
        when(propertyRepository.findByLocationType(LocationType.TILBURG)).thenReturn(properties);

        try (MockedStatic<PropertyMapperDto> mockedMapper = mockStatic(PropertyMapperDto.class)) {
            mockedMapper.when(() -> PropertyMapperDto.MapoPropertyDto(propertyEntity))
                    .thenReturn(propertyDto);

            // Act
            List<PropertyDto> result = propertyService.getPropertiesByLocation(String.valueOf(LocationType.TILBURG));

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Property", result.get(0).getTitle());
        }
    }
}