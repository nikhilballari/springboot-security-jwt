package com.dashboardapi.demo.service;

import com.dashboardapi.demo.entity.Dashboard;
import com.dashboardapi.demo.error.DashboardRecordNotFoundException;

import java.util.List;

public interface DashboardService {

    List<Dashboard> getAllDashboards();

    Dashboard saveDashboard(Dashboard dashboard);

    String deleteDashboardById(Long dashboardId) throws DashboardRecordNotFoundException;

    Dashboard updateDashboard(Long dashboardId, Dashboard dashboard) throws DashboardRecordNotFoundException;

    Dashboard getDashboardById(Long dashboardId) throws DashboardRecordNotFoundException;
}
