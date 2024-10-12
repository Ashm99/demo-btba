package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/passenger")
@AllArgsConstructor
public class PassengerController {
    private PassengerService passengerService;

    /**
     * A mvc method for rendering the home page
     *
     * @param model Model interface object,
     * @param authentication Authentication interface object.
     * @return Passenger home page or error page based on the outcome.
     */
    // http://localhost:8081/passenger/home
    @GetMapping(value = "/home")
    public String goHome(Model model, Authentication authentication) {
        System.out.println("Rendering passenger homepage...");
//        String passengerName = "";
//        try{
//            passengerName = passengerService.getUserFirstnameByUsername(authentication.getName());
//        } catch(NoSuchElementException exception){
//            String message = exception.getMessage();
//
//            model.addAttribute("message", message);
//            return "error";
//        }
//        model.addAttribute("passengerName", passengerName);
        return "home";
    }

    /**
     * A mvc method for the logout confirmation page
     *
     * @return Logout confirmation view.
     */
    // http://localhost:8081/passenger/confirm-logout
    @GetMapping(value = "/confirm-logout")
    public String confirmLogout() {
        System.out.println("Rendering logout confirmation page...");
        return "confirm-logout";
    }
}
