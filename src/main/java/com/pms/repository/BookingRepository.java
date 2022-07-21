package com.pms.repository;


import com.pms.domain.booking.Booking;
import com.pms.domain.booking.BookingState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findByBookingId(String bookingId);

    List<Booking> findByRegistDtBetween(LocalDate startDate, LocalDate endDate);
}
