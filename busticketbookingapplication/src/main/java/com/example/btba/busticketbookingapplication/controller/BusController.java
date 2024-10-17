package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.dto.BusDto;
import com.example.btba.busticketbookingapplication.service.BusService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
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
    @GetMapping(value = "/search")
    @ResponseBody
    public List<BusDto> goHome(@RequestParam String from, @RequestParam String to, @RequestParam LocalDate date, Model model, Authentication authentication) {
        System.out.println("Rendering bus search page...");
        System.out.println(from);
        System.out.println(to);
        System.out.println(date);
//        busService.getRouteBySourceAndDestination(from, to);
        List<BusDto> fetchedBusList = busService.getBusesBetweenSourceAndDestination(from, to);
        // returns a list of buses
//        List<BusDto> buses = busService.getBusesByRoute(routeId);
        return fetchedBusList;
    }
}
