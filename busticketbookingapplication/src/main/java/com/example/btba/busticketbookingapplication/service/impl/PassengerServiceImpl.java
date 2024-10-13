package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.dto.PassengerDto;
import com.example.btba.busticketbookingapplication.dto.SavePassengerDto;
import com.example.btba.busticketbookingapplication.dto.UserCredentialDto;
import com.example.btba.busticketbookingapplication.mapper.PassengerMapper;
import com.example.btba.busticketbookingapplication.model.Passenger;
import com.example.btba.busticketbookingapplication.repo.PassengerRepo;
import com.example.btba.busticketbookingapplication.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepo passengerRepo;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;

    /**
     * A Service method to verify passenger credentials.
     *
     * @param user Object having username and password for validation.
     * @return True if valid user and vice versa.
     */
    @Override
    public boolean verify(UserCredentialDto user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication.isAuthenticated();
        } catch(Exception e){
            return false;
        }
    }

    /**
     * A Service method to add a passenger.
     *
     * @param newPassenger A new passenger object.
     * @return Saved passenger dto object.
     */
    @Override
    public PassengerDto addPassenger(SavePassengerDto newPassenger) {
        Optional<Passenger> optionalPassenger = passengerRepo.findByEmail(newPassenger.getEmail());
        if(optionalPassenger.isPresent()) {
            throw new RuntimeException("Passenger already exists with given email.");
        }
        System.out.println("newPassenger before encoding: " + newPassenger);
        newPassenger.setPassword(encoder.encode(newPassenger.getPassword()));
        System.out.println("newPassenger after encoding: " + newPassenger);
        Passenger savedPassenger = passengerRepo.save(PassengerMapper.mapToPassengerFromSavePassengerDto(newPassenger));
        return PassengerMapper.mapToPassengerDto(savedPassenger);
    }
}
