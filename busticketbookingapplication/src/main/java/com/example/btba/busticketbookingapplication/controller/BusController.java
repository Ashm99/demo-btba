package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.model.Seat;
import com.example.btba.busticketbookingapplication.service.BusService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/bus")
public class BusController {
    private final BusService busService;

    /**
     * A mvc method for rendering the bus list page
     *
     * @param model Model interface object,
     * @param authentication Authentication interface object.
     * @return Passenger home page or error page based on the outcome.
     */
    // http://localhost:8081/bus/search
    // This api is hit by 2 forms in->
    // 1. home.html
    // 2. bus-search-result.html
    @GetMapping(value = "/search")
    public String goHome(@RequestParam String from, @RequestParam String to, @RequestParam LocalDate date, Model model, Authentication authentication) {
        System.out.println("Searching for buses from "+ from + " to " + to + "...");
        System.out.println(from);
        System.out.println(to);
        System.out.println(date);
        List<BusTravelDto> fetchedBusList = busService.getBusesBetweenSourceAndDestination(from, to, date); // returns a list of buses
        if(fetchedBusList.isEmpty()) {
            model.addAttribute("busListIsEmpty", true);
        } else {
            model.addAttribute("busListIsEmpty", false);
            model.addAttribute("busList", fetchedBusList);
        }

        model.addAttribute("routes", busService.getAllRoutes());
        model.addAttribute("locations", busService.getAllStops());
        model.addAttribute("source", from);
        model.addAttribute("destination", to);
        model.addAttribute("date", date);

        return "bus-search/bus-search-result";
    }

    /**
     * An api (mvc method) for seat selection while booking a bus ticket.
     *
     * @param busId               id of the bus,
     * @param from                passenger service start point,
     * @param sourceDepartureDate date of departure from passenger service start point,
     * @param sourceDepartureTime time of departure from passenger service start point,
     * @param to                  passenger service end point,
     * @return                    A webpage with seat arrangements for selection.
     */
    @GetMapping(value = "/seats")
    public String renderSeatSelectionPage(
            @RequestParam String busId,
            @RequestParam String from,
            @RequestParam String sourceDepartureDate,
            @RequestParam String sourceDepartureTime,
            @RequestParam String to,
            Model model) {
        System.out.println("Rendering seat selection page...");
        System.out.println(busId);
        System.out.println(from);
        System.out.println(sourceDepartureDate);
        System.out.println(sourceDepartureTime);
        System.out.println(to);
        busService.initializeBusBookingObject(from, to, sourceDepartureDate, sourceDepartureTime);
        BusDto busDto = busService.getBusById(Long.parseLong(busId));
        List<Seat> seatList = busService.getSeatDetails(busId, sourceDepartureDate);
        System.out.println("seat list: " + seatList);
        model.addAttribute("busType", busDto.getSeatType().toString());
        model.addAttribute("rows", busDto.getNoOfRows());
        model.addAttribute("busName", busDto.getBusName());
        model.addAttribute("seatList", seatList);
        return "bus-booking/seat-selection";
    }

    /**
     * An api (mvc method) for getting the selected seats from the front-end and save to back-end.
     *
     * @param seatNumbers A List of seat Numbers as a String
     * @param model       A Model object
     * @return            The Boarding Point view
     */
    @PostMapping(value = "/getSeats")
    public String getSelectedSeats(
            @RequestBody List<String> seatNumbers,
            Model model) {
        System.out.println(seatNumbers);
        System.out.println("Sending seat list to service layer");
        busService.saveSeatNumbersToBusBookingObject(seatNumbers);
        System.out.println("Sent seat list to service layer");
        return "redirect:/bus/boardingPoint";
    }

    /**
     * An api (mvc method) for rendering the boarding point view.
     *
     * @param model A Model object
     * @return      The Boarding Point view
     */
    @GetMapping(value = "/boardingPoint")
    public String renderBoardingAndDroppingPointPage(Model model) {
        BusBooking busBooking = busService.getBoardingAndDroppingSummary();
        model.addAttribute("busBooking", busBooking);
        String duration = busService.getBusDuration(LocalDateTime.of(busBooking.getPickupDate(), busBooking.getPickupTime()), LocalDateTime.of(busBooking.getDropDate(), busBooking.getDropTime()));
        model.addAttribute("duration", duration);
        return "bus-booking/boarding-dropping-point-summary";
    }

    /**
     * An api (mvc method) for getting the passenger details for the trip.
     *
     * @return
     */
    @GetMapping(value = "/passengerDetails")
    public String renderPassengerDetailsPage (Model model) {
        int passengerCount = busService.getPassengerCountFromBusBookingObject();
        model.addAttribute("passengerCount", passengerCount);
        return "bus-booking/passenger-details";
    }

    @GetMapping(value = "/savePassengerDetails")
    @ResponseBody
    public String savePassengerDetailsAndRenderPaymentPage(
            @RequestParam String passenger1Name,
            @RequestParam String passenger1Age,
            @RequestParam String passenger1Gender,
            @RequestParam(required = false) String passenger2Name,
            @RequestParam(required = false) String passenger2Age,
            @RequestParam(required = false) String passenger2Gender,
            @RequestParam(required = false) String passenger3Name,
            @RequestParam(required = false) String passenger3Age,
            @RequestParam(required = false) String passenger3Gender,
            @RequestParam(required = false) String passenger4Name,
            @RequestParam(required = false) String passenger4Age,
            @RequestParam(required = false) String passenger4Gender,
            @RequestParam String passengerEmail,
            @RequestParam String passengerMobile,
            Model model) {
        System.out.print("passenger1: " + passenger1Name);
        System.out.print(" " + passenger1Age + " ");
        System.out.println(passenger1Gender);
        System.out.print("passenger2: " + passenger2Name);
        System.out.print(" " + passenger2Age + " ");
        System.out.println(passenger2Gender);
        System.out.print("passenger3: " + passenger3Name);
        System.out.print(" " + passenger3Age + " ");
        System.out.println(passenger3Gender);
        System.out.print("passenger4: " + passenger4Name);
        System.out.print(" " + passenger4Age + " ");
        System.out.println(passenger4Gender);
        System.out.println("Email: " + passengerEmail);
        System.out.println("Mobile: " + passengerMobile);
        return "success";
    }
}
