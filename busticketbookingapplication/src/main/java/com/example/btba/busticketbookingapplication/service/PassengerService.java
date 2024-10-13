package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.PassengerDto;
import com.example.btba.busticketbookingapplication.dto.SavePassengerDto;
import com.example.btba.busticketbookingapplication.dto.UserCredentialDto;

public interface PassengerService {
    boolean verify(UserCredentialDto user);
    PassengerDto addPassenger(SavePassengerDto newPassenger);

}
