package com.example.btba.busticketbookingapplication.repo;

import com.example.btba.busticketbookingapplication.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepo extends JpaRepository<Route, Long> {

}
