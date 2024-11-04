package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.model.Seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BusService {
    BusDto getBusById(long busId);
    List<BusTravelDto> getBusesBetweenSourceAndDestination(String passengerStartPoint, String passengerEndPoint, LocalDate sourceDepartureDate);

    String getBusDuration(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);

    List<String> getAllRoutes();

    List<String> getAllStops();

    void initializeBusBookingObject(String from, String to, String sourceDepartureDate, String sourceDepartureTime);

    List<Seat> getSeatDetails(String busId, String sourceDepartureDate);

    void saveSeatNumbersToBusBookingObject(List<String> seatNumbers);

    BusBooking getBoardingAndDroppingSummary();
}
