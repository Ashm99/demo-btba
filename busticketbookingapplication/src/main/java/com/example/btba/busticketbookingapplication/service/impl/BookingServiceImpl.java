package com.example.btba.busticketbookingapplication.service.impl;

import com.example.btba.busticketbookingapplication.model.BusBooking;
import com.example.btba.busticketbookingapplication.repo.BusBookingRepo;
import com.example.btba.busticketbookingapplication.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BusBookingRepo busBookingRepo;

    /**
     * A service method to show the bookings done by a user.
     *
     * @param username email id of the user
     * @return the list of bookings made by the user
     */
    @Override
    public List<List<BusBooking>> getBusBookingListByUsername(String username) {
        List<BusBooking> busBookingList = busBookingRepo.findAllByBookedBy(username);
        List<List<BusBooking>> seperatedBookingList = new ArrayList<>();
        List<BusBooking> upcomingBookingList = new ArrayList<>();
        List<BusBooking> completedBookingList = new ArrayList<>();
        busBookingList.forEach( busBooking -> {
            if(!busBooking.isCancelled() && LocalDateTime.of(busBooking.getPickupDate(), busBooking.getPickupTime()).isAfter(LocalDateTime.now())) {
                upcomingBookingList.add(busBooking);
            } else {
                completedBookingList.add(busBooking);
            }
        });
        seperatedBookingList.add(upcomingBookingList);
        seperatedBookingList.add(completedBookingList);
        return seperatedBookingList;
    }
}
