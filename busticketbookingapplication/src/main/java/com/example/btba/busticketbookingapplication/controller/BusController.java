package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.dto.BusTravelDto;
import com.example.btba.busticketbookingapplication.service.BusService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
