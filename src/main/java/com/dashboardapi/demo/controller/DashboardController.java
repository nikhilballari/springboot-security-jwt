package com.dashboardapi.demo.controller;

import com.dashboardapi.demo.dto.AuthRequest;
import com.dashboardapi.demo.entity.Dashboard;
import com.dashboardapi.demo.error.DashboardRecordNotFoundException;
import com.dashboardapi.demo.service.DashboardService;
import com.dashboardapi.demo.service.JwtService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/dashboards")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Dashboard> getAllDashboards() {
        log.info("Inside DashboardController.getAllDashboards() method");
        return dashboardService.getAllDashboards();
    }

    @GetMapping("/dashboards/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Dashboard getDashboardById(@PathVariable("id") Long dashboardId) throws DashboardRecordNotFoundException {
        log.info("Got id {} for fetching the details", dashboardId);
        /*return ResponseHandler.responseBuilder(ApplicationConstants.REQUESTED_DASHBOARD_DETAILS_ARE_GIVEN_HERE,
                HttpStatus.OK, dashboardService.getDashboardById(dashboardId));*/
        return dashboardService.getDashboardById(dashboardId);
    }

    @PostMapping("/dashboards")
    public Dashboard saveDashboard(@RequestBody @Valid Dashboard dashboard) {
        log.info("Inside DashboardController.saveDashboard() method");
        return dashboardService.saveDashboard(dashboard);
    }

    @DeleteMapping("/dashboards/{id}")
    public String deleteDashboardById(@PathVariable("id") Long dashboardId) throws DashboardRecordNotFoundException {
        log.info("Received id {} for deletion", dashboardId);
        return dashboardService.deleteDashboardById(dashboardId);
    }

    @PutMapping("/dashboards/{id}")
    public Dashboard updateDashboard(@PathVariable("id") Long dashboardId, @RequestBody @Valid Dashboard dashboard) throws DashboardRecordNotFoundException {
        log.info("Received id {} for update", dashboardId);
        return dashboardService.updateDashboard(dashboardId, dashboard);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUserName(),
                                                        authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            log.info("User Authentication is successful for username: {} password: {}",
                            authRequest.getUserName(), authRequest.getPassword());
            return jwtService.generateToken(authRequest.getUserName());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
