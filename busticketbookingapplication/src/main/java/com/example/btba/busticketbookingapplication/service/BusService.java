package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.BusDto;

import java.util.List;

public interface BusService {
    List<BusDto> getBusesBetweenSourceAndDestination(String source, String destination);
}
