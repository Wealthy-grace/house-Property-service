package com.example.propertyservice.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Create a simple DTO class

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePropertyStatusRequest {
    @NotNull(message = "Property rented status is required")
    private Boolean propertyIsRented;

    // Getters and Setters
    public Boolean getPropertyIsRented() {
        return propertyIsRented;
    }

//    //public void setPropertyIsRented(Boolean propertyIsRented) {
//        this.propertyIsRented = propertyIsRented;
//    }
}