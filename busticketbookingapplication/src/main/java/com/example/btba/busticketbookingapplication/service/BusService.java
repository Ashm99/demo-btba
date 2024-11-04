package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.model.Seat;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    BusDto getBusById(long busId);
    List<BusTravelDto> getBusesBetweenSourceAndDestination(String passengerStartPoint, String passengerEndPoint, LocalDate sourceDepartureDate);

    List<String> getAllRoutes();

    List<String> getAllStops();

    List<Seat> getSeatDetails(String busId, String sourceDepartureDate);

    void saveSeatNumbersToBusBookingObject(List<String> seatNumbers);
}
