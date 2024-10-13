package com.example.btba.busticketbookingapplication.service;

import com.example.btba.busticketbookingapplication.dto.UserCredentialDto;

public interface PassengerService {
    boolean verify(UserCredentialDto user);
}
