package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.mapper.BusMapper;
import com.example.btba.busticketbookingapplication.model.Bus;
import com.example.btba.busticketbookingapplication.repo.BusRepo;
import com.example.btba.busticketbookingapplication.repo.RouteRepo;
import com.example.btba.busticketbookingapplication.repo.StopRepo;
import com.example.btba.busticketbookingapplication.service.BusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class BusServiceImpl implements BusService {
    private final BusRepo busRepo;
    private final RouteRepo routeRepo;
    private final StopRepo stopRepo;

    /**
     * A service method for getting a list of buses for given source and destination locations.
     *
     * @param source Source location,
     * @param destination Destination location.
     * @return A list of buses running between given stops.
     */
    @Override
    public List<BusDto> getBusesBetweenSourceAndDestination(String source, String destination) {
        List<Bus> busList = new ArrayList<>();
        List<BusDto> busDtoList = new ArrayList<>();
        List<Long> routeIds = stopRepo.findRouteIdsByStops(source, destination);
        System.out.println("Route ids: " + routeIds.toString());
        routeIds.forEach( id -> busList.addAll(busRepo.findByRouteId(id)));
        AtomicInteger i = new AtomicInteger(0);
        busList.forEach(bus -> {
            System.out.println("Bus "+ i.incrementAndGet() + ": " + bus);
            busDtoList.add(BusMapper.mapToBusDto(bus));
        });
        System.out.println("Bus Dtos: " + busDtoList.toString());
        if(busDtoList.isEmpty()) System.out.println("No buses found.");
        return busDtoList;
    }
}
