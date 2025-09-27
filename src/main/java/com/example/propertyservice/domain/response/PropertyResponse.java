package com.example.propertyservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyResponse {

    private Long propertyId;

    private String message;

    private boolean success;

    private String title;

    private String description;

    private BigDecimal rentAmount;
    private String image;
    private String image4;
    private String image2;

    private String image3;

}
