package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.BusTravelDto;

import java.time.LocalDate;
import java.util.List;

public interface BusService {
    List<BusTravelDto> getBusesBetweenSourceAndDestination(String passengerStartPoint, String passengerEndPoint, LocalDate travelDate);

    List<String> getAllRoutes();

    List<String> getAllStops();
}
