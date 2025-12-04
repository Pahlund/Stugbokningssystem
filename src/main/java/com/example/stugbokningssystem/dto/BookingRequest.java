package com.example.stugbokningssystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private Long cabinId;
    private String guestName;
    private String guestEmail;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
}
