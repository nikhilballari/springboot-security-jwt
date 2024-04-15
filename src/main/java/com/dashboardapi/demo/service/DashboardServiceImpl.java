package com.dashboardapi.demo.service;

import com.dashboardapi.demo.entity.Dashboard;
import com.dashboardapi.demo.error.DashboardRecordNotFoundException;
import com.dashboardapi.demo.repository.DashboardRepository;
import com.dashboardapi.demo.util.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public List<Dashboard> getAllDashboards() {
        log.info("Inside getAllDashboards() method");
        return dashboardRepository.findAll();
    }

    @Override
    public Dashboard saveDashboard(Dashboard dashboard) {
        log.info("Inside saveDashboard() method");
        return dashboardRepository.save(dashboard);
    }

    @Override
    public String deleteDashboardById(Long dashboardId) throws DashboardRecordNotFoundException {
        log.info("Inside deleteDashboardById() method");
        if (dashboardRepository.findById(dashboardId).isEmpty()) {
            throw new DashboardRecordNotFoundException(ApplicationConstants.DASHBOARD_RECORD_NOT_PRESENT);
        }
        dashboardRepository.deleteById(dashboardId);
        return ApplicationConstants.DASHBOARD_RECORD_DELETED_SUCCESSFULLY;
    }

    @Override
    public Dashboard updateDashboard(Long dashboardId, Dashboard dashboard) throws DashboardRecordNotFoundException {
        log.info("Inside updateDashboard() method");

        Dashboard existingDashboard = dashboardRepository.findById(dashboardId).orElse(null);
        if (Objects.isNull(existingDashboard)) {
            throw new DashboardRecordNotFoundException(ApplicationConstants.DASHBOARD_RECORD_NOT_PRESENT);
        }

        if (Objects.nonNull(dashboard.getTitle()) && !"".equalsIgnoreCase(dashboard.getTitle())) {
            existingDashboard.setTitle(dashboard.getTitle());
        }
        if (Objects.nonNull(dashboard.getCreatedAt())) {
            existingDashboard.setCreatedAt(dashboard.getCreatedAt());
        }
        if (Objects.nonNull(dashboard.getUpdatedAt())) {
            existingDashboard.setUpdatedAt(dashboard.getUpdatedAt());
        }
        return dashboardRepository.save(existingDashboard);
    }

    @Override
    public Dashboard getDashboardById(Long dashboardId) throws DashboardRecordNotFoundException {
        log.info("Inside getDashboardById() method");
        if (dashboardRepository.findById(dashboardId).isEmpty()) {
            throw new DashboardRecordNotFoundException(ApplicationConstants.DASHBOARD_RECORD_NOT_PRESENT);
        }
        return dashboardRepository.findById(dashboardId).get();
    }
}
