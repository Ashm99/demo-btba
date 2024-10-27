package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.mapper.BusMapper;
import com.example.btba.busticketbookingapplication.model.Bus;
import com.example.btba.busticketbookingapplication.model.SeatType;
import com.example.btba.busticketbookingapplication.repo.BusRepo;
import com.example.btba.busticketbookingapplication.repo.RouteRepo;
import com.example.btba.busticketbookingapplication.repo.StopRepo;
import com.example.btba.busticketbookingapplication.service.BusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class BusServiceImpl implements BusService { // 5 non-overridden methods exist
    // After including the booking Dto, include logic for correcting the available seats inside setAvailableSeats() method.
    private final BusRepo busRepo;
    private final RouteRepo routeRepo;
    private final StopRepo stopRepo;

    /**
     * A service method for getting a list of buses for given source and destination locations.
     *
     * @param passengerStartPoint passenger service startpoint,
     * @param passengerEndPoint   passenger service endpoint,
     * @param travelDate          date of travel,
     * @return A list of buses running between given stops.
     */
    @Override
    public List<BusTravelDto> getBusesBetweenSourceAndDestination(String passengerStartPoint, String passengerEndPoint, LocalDate travelDate) {
        List<Bus> busList = new ArrayList<>();
        List<BusDto> busDtoList = new ArrayList<>();

        // Finding bus routes
        List<Long> routeIds = stopRepo.findRouteIdsByStops(passengerStartPoint, passengerEndPoint);
        System.out.println("Route ids: " + routeIds.toString());

        // Finding buses on the found routes
        routeIds.forEach( id -> busList.addAll(busRepo.findByRouteId(id)));

        List<BusTravelDto> busTravelDtoList = this.setBusTravelDto(passengerStartPoint, passengerEndPoint, travelDate, busList);

        // Creating Dto list
        AtomicInteger i = new AtomicInteger(0);
        busList.forEach(bus -> {
            System.out.println("Bus "+ i.incrementAndGet() + ": " + bus);
            busDtoList.add(BusMapper.mapToBusDto(bus));
        });
        System.out.println("Bus Dtos: " + busDtoList.toString());
        System.out.println("busList: " + busList.toString());
        System.out.println("busTravelDtoList: " + busTravelDtoList.toString());
        if(busDtoList.isEmpty()) System.out.println("No buses found.");

        return busTravelDtoList;
    }

    /**
     * A non-overridden service method to set bus traveling details between given source and destination.
     *
     * @param passengerStartPoint      Source location,
     * @param passengerEndPoint Destination location,
     * @param travelDate travel date,
     * @param busList     List of Buses between given source and destination,
     * @return A list of Dtos having bus traveling details.
     */
    private List<BusTravelDto> setBusTravelDto(String passengerStartPoint, String passengerEndPoint, LocalDate travelDate, List<Bus> busList) {
        System.out.println("Setting busTravelDtoList from BusServiceImpl...");
        List<BusTravelDto> busTravelDtoList = new ArrayList<>();
        busList.forEach(bus -> {
            int availableSeats = this.setAvailableSeats(bus.getId(), bus.getSeatType(), bus.getNoOfRows(), travelDate);
            if (availableSeats > 0) {
                BusTravelDto busTravelDto = BusTravelDto.builder()
                        .busName(bus.getBusName())
                        .busType(this.setBusType(bus.getAirConditioned(), bus.getSeatType()))
                        .availableSeats(availableSeats)
                        .source(passengerStartPoint) // source location for passenger
                        .destination(passengerEndPoint) // destination location for passenger
                        .sourceDepartureDate(null)
                        .sourceDepartureTime(null)
                        .destinationArrivalDate(null)
                        .destinationArrivalTime(null)
                        .duration("0h 0m")
                        .baseFare(0D)
                        .build();
                Map<String, LocalDateTime> departureAndArrivalMap = this.setDepartureAndArrivalData(bus.getRoute().getId(), bus.getSourceDepartureTime(), travelDate, passengerStartPoint, passengerEndPoint);
                busTravelDto.setSourceDepartureDate(departureAndArrivalMap.get("departureDateTime").toLocalDate());
                busTravelDto.setSourceDepartureTime(departureAndArrivalMap.get("departureDateTime").toLocalTime());
                busTravelDto.setDestinationArrivalDate(departureAndArrivalMap.get("arrivalDateTime").toLocalDate());
                busTravelDto.setDestinationArrivalTime(departureAndArrivalMap.get("arrivalDateTime").toLocalTime());
                busTravelDto.setDuration(this.setBusDuration(departureAndArrivalMap.get("departureDateTime"), departureAndArrivalMap.get("arrivalDateTime")));
                busTravelDto.setBaseFare(bus.getPricePerKm() * stopRepo.findDistanceBetweenLocations(passengerStartPoint, passengerEndPoint, bus.getRoute().getId()));
                busTravelDtoList.add(busTravelDto);
            }
        });
        System.out.println("busTravelDtoList set.");
        return busTravelDtoList;
    }

    /**
     * A non-overridden service method to set the duration string between locations for a bus.
     *
     * @param departureDateTime LocalDateTime variable having the time of departure,
     * @param arrivalDateTime   LocalDateTime variable having the time of arrival,
     * @return a String value mentioning the duration between the departure and arrival point.
     */
    private String setBusDuration(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        String s = "";
        Duration duration = Duration.between(departureDateTime, arrivalDateTime);
        if(duration.toDays() > 0) s = s + duration.toDays() + "d ";
        s = s + duration.toHours() + "h " + (duration.toMinutes() % 60) + "m";
        return s;
    }

    /**
     * A non-overridden service method to assign the bus type e.g. AC Sleeper, Non-AC Sleeper.
     *
     * @param airConditioned boolean value mentioning if bus is air-conditioned,
     * @param seatType bus seat type.
     * @return String value mentioning the bus type
     */
    private String setBusType(Boolean airConditioned, SeatType seatType) {
        return ((airConditioned) ? "AC" : "Non-AC")+ " " + seatType.toString();
    }

    /**
     * A non-overridden service method to get the number of available seats in a bus.
     *
     * @param busId    Bus id in the database,
     * @param seatType type of bus seat,
     * @param noOfRows no. of rows in the bus,
     * @param date travel date,
     * @return the number of available seats.
     */
    private int setAvailableSeats(Long busId, SeatType seatType, int noOfRows, LocalDate date)  {
        int noOfColumns = switch (seatType) {
            case SLEEPER_2_1 -> 6; // 3 from each upper and lower deck
            case SEMI_SLEEPER_2_2 -> 4;
            default -> 5;
        };
        int totalSeats = noOfRows * noOfColumns;
        int bookedSeats = 0; // Add logic to include the booked seats
        return (totalSeats-bookedSeats);
    }

    /**
     * A non-overridden service method to set departure and arrival data for the travel
     *
     * @param routeId                route id,
     * @param busSourceDepartureTime departure time of the bus from its service starting point,
     * @param travelDate             date of travel
     * @param passengerStartPoint    source location for passenger
     * @param passengerEndPoint      destination location for passenger
     * @return a map with departure and arrival data for the travel.
     */
    private Map<String, LocalDateTime> setDepartureAndArrivalData(Long routeId,
                                                                  LocalTime busSourceDepartureTime,
                                                                  LocalDate travelDate,
                                                                  String passengerStartPoint,
                                                                  String passengerEndPoint) {
        Map<String, LocalDateTime> map = new HashMap<>();

        // Finding the bus service source location
        String busSourceDepartureLocation = stopRepo.findBusSourceLocation(routeId);
        System.out.println("busSourceDepartureLocation: " + busSourceDepartureLocation);

        // Defining departure date and time from passenger start point
        LocalDateTime departureDateTime = LocalDateTime.of(travelDate, busSourceDepartureTime);
        int travelTimeInMinutes = 0;
        if(!busSourceDepartureLocation.equals(passengerStartPoint)){
            travelTimeInMinutes = this.getTravelTimeBetweenStopsInMinutes(routeId, busSourceDepartureLocation, passengerStartPoint);
            departureDateTime = departureDateTime.plusMinutes(travelTimeInMinutes);
        }
        map.put("departureDateTime", departureDateTime);

        // Defining arrival date and time from passenger start point
        travelTimeInMinutes = this.getTravelTimeBetweenStopsInMinutes(routeId, passengerStartPoint, passengerEndPoint);
        LocalDateTime arrivalDateTime = departureDateTime.plusMinutes(travelTimeInMinutes);
        map.put("arrivalDateTime", arrivalDateTime);
        return map;
    }

    /**
     * A non-overridden service method to find the travel time between two locations. By default, considers a travel time of 1 hour for 45 km.
     *
     * @param routeId             Route Id,
     * @param passengerStartPoint Passenger service start point,
     * @param passengerEndPoint   Passenger Service end point,
     * @return The travel duration in minutes.
     */
    private int getTravelTimeBetweenStopsInMinutes(Long routeId, String passengerStartPoint, String passengerEndPoint) {
        Double distanceInKmBetweenStops = stopRepo.findDistanceBetweenLocations(passengerStartPoint, passengerEndPoint, routeId);
        return (int)Math.ceil(distanceInKmBetweenStops*60/45);
    }

    /**
     * A Service method to get all the routes from the database.
     *
     * @return A list of all routes.
     */
    @Override
    public List<String> getAllRoutes() {
        List<String> routes = routeRepo.findDistinctRoutes();
        if (routes.isEmpty()) {
            System.out.println("No routes found from the database. Check if route table is empty.");
            return List.of("None");
        }
        return routes;
    }

    /**
     * A Service method to get all the stops from the database.
     *
     * @return A list of all stops.
     */
    @Override
    public List<String> getAllStops() {
        List<String> locations = stopRepo.findDistinctLocations();
        if (locations.isEmpty()) {
            System.out.println("No locations found from the database. Check if stop table is empty.");
            return List.of("No locations found.");
        }
        return locations;
    }
}
