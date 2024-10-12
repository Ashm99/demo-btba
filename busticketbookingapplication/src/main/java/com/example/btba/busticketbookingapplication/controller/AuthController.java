package com.example.btba.busticketbookingapplication.controller;

import com.example.btba.busticketbookingapplication.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/auth")
@AllArgsConstructor
public class AuthController {
    private PassengerService passengerService;

    /**
     * A mvc method for the login page
     *
     * @return the login view.
     */
    // http://localhost:8081/auth/login
    @GetMapping(value = "/login")
    public String home() {
        System.out.println("Rendering login page...");
        return "login";
    }
}
