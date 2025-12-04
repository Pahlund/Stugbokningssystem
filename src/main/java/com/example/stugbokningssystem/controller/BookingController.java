package com.example.stugbokningssystem.controller;

import com.example.stugbokningssystem.dto.BookingRequest;
import com.example.stugbokningssystem.dto.BookingResponse;
import com.example.stugbokningssystem.entity.Cabin;
import com.example.stugbokningssystem.repository.CabinRepository;
import com.example.stugbokningssystem.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final CabinRepository cabinRepository;

    @GetMapping("/cabins")
    public ResponseEntity<List<Cabin>> getAllCabins() {
        return ResponseEntity.ok(cabinRepository.findAll());
    }

    @GetMapping("/cabins/{id}")
    public ResponseEntity<Cabin> getCabinById(@PathVariable Long id) {
        return cabinRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new BookingResponse(null, null, null, null, null, null, null, 0, 0.0, null,
                            "Fel vid bokning: " + e.getMessage())
            );
        }
    }
}
