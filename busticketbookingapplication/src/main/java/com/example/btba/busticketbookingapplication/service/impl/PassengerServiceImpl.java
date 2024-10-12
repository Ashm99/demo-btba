package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.repo.PassengerRepo;
import com.example.btba.busticketbookingapplication.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private PassengerRepo passengerRepo;
}
