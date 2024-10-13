package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.dto.UserCredentialDto;
import com.example.btba.busticketbookingapplication.repo.PassengerRepo;
import com.example.btba.busticketbookingapplication.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepo passengerRepo;
    private final AuthenticationManager authManager;
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
}
