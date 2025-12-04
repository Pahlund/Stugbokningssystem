package com.example.stugbokningssystem.service;

import com.example.stugbokningssystem.dto.BookingRequest;
import com.example.stugbokningssystem.dto.BookingResponse;
import com.example.stugbokningssystem.entity.Booking;
import com.example.stugbokningssystem.entity.Cabin;
import com.example.stugbokningssystem.repository.BookingRepository;
import com.example.stugbokningssystem.repository.CabinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CabinRepository cabinRepository;

    public BookingResponse createBooking(BookingRequest request) {
        // Validera att stugan finns
        Cabin cabin = cabinRepository.findById(request.getCabinId())
                .orElseThrow(() -> new RuntimeException("Stuga med ID " + request.getCabinId() + " finns inte"));

        // Validera antal gäster
        if (request.getNumberOfGuests() > cabin.getCapacity()) {
            throw new RuntimeException("Antal gäster (" + request.getNumberOfGuests() +
                    ") överstiger stugens kapacitet (" + cabin.getCapacity() + ")");
        }

        // Validera datum
        if (request.getCheckInDate().isAfter(request.getCheckOutDate())) {
            throw new RuntimeException("Incheckningsdatum måste vara före utcheckningsdatum");
        }

        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Incheckningsdatum kan inte vara i det förflutna");
        }

        // Kontrollera om stugan är ledig för de valda datumen
        if (!isCabinAvailable(request.getCabinId(), request.getCheckInDate(), request.getCheckOutDate())) {
            throw new RuntimeException("Stugan är redan bokad för de valda datumen");
        }

        // Beräkna totalpris
        long numberOfNights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        double totalPrice = numberOfNights * cabin.getPricePerNight();

        // Skapa bokningsreferens
        String bookingReference = "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Skapa bokning
        Booking booking = new Booking();
        booking.setCabin(cabin);
        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setTotalPrice(totalPrice);
        booking.setBookingDateTime(LocalDateTime.now());
        booking.setBookingReference(bookingReference);

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                savedBooking.getId(),
                savedBooking.getBookingReference(),
                cabin.getName(),
                savedBooking.getGuestName(),
                savedBooking.getGuestEmail(),
                savedBooking.getCheckInDate(),
                savedBooking.getCheckOutDate(),
                savedBooking.getNumberOfGuests(),
                savedBooking.getTotalPrice(),
                savedBooking.getBookingDateTime(),
                "Bokning bekräftad! Din bokningsreferens är: " + bookingReference
        );
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Kontrollerar om en stuga är tillgänglig för de angivna datumen.
     * En stuga är INTE tillgänglig om det finns en befintlig bokning där datumen överlappar.
     */
    private boolean isCabinAvailable(Long cabinId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> existingBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getCabin().getId().equals(cabinId))
                .filter(b -> !(checkOut.isBefore(b.getCheckInDate()) ||
                        checkOut.equals(b.getCheckInDate()) ||
                        checkIn.isAfter(b.getCheckOutDate()) ||
                        checkIn.equals(b.getCheckOutDate())))
                .toList();
        return existingBookings.isEmpty();
    }
}
