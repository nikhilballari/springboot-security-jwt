package com.dashboardapi.demo.service;

import com.dashboardapi.demo.entity.Dashboard;
import com.dashboardapi.demo.error.DashboardRecordNotFoundException;
import com.dashboardapi.demo.repository.DashboardRepository;
import com.dashboardapi.demo.util.ApplicationConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    private DashboardService dashboardService;

    @MockBean
    private DashboardRepository dashboardRepository;

    @Test
    @DisplayName  ("Get all Dashboards from the database")
    public void whenDashboardsExist_thenReturnAllDashboards() {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title 5")
                .createdAt(LocalDateTime.of(2024,01,10,18,10,15))
                .updatedAt(LocalDateTime.of(2024,01,15,18,10,15))
                .build();

        Dashboard dashboard2 = Dashboard.builder()
                .title("Test Title 6")
                .createdAt(LocalDateTime.of(2024,02,20,18,10,15))
                .updatedAt(LocalDateTime.of(2024,03,23,18,10,15))
                .build();

        Mockito.when(dashboardRepository.findAll()).thenReturn(List.of(dashboard1, dashboard2));

        List<Dashboard> dashboardList = dashboardService.getAllDashboards();
        assertEquals((long) dashboardList.size(), 2);
        assertEquals(dashboardList.get(0).getTitle(), "Test Title 5");
        assertNotEquals(dashboardList.get(0).getCreatedAt(), dashboardList.get(1).getCreatedAt());
    }

    @Test
    @DisplayName("Get empty dashboard list when no data")
    public void whenNoDashboardData_thenReturnNothing() {
        List<Dashboard> emptyDashboardList = new ArrayList<>();
        Mockito.when(dashboardRepository.findAll()).thenReturn(emptyDashboardList);

        List<Dashboard> dashboardList = dashboardService.getAllDashboards();
        Assertions.assertThat(dashboardList.isEmpty());
    }

    @Test
    @DisplayName("Add new Dashboard Record")
    public void whenNewRecordAdded_thenReturnSuccess() {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title 5")
                .createdAt(LocalDateTime.of(2024,02,20,18,10,15))
                .updatedAt(LocalDateTime.of(2024,03,23,18,10,15))
                .build();

        Mockito.when(dashboardRepository.save(any())).thenReturn(dashboard1);

        Dashboard responseDashboard = dashboardService.saveDashboard(dashboard1);
        assertEquals(responseDashboard.getTitle(), dashboard1.getTitle());
        assertEquals(responseDashboard.getUpdatedAt(), dashboard1.getUpdatedAt());
    }

    @Test
    @DisplayName("Delete the dashboard when record not present in DB")
    public void whenRecordForDeletionNotFound_thenReturnMessage() throws DashboardRecordNotFoundException {
        Mockito.when(dashboardRepository.findById(any())).thenReturn(Optional.empty());
        DashboardRecordNotFoundException dashboardRecordNotFoundException = assertThrows(DashboardRecordNotFoundException.class,
                () -> dashboardService.deleteDashboardById(5L));
        assertEquals(dashboardRecordNotFoundException.getMessage(), ApplicationConstants.DASHBOARD_RECORD_NOT_PRESENT);
    }

    @Test
    @DisplayName("Delete the dashboard record when present in DB")
    public void whenRecordForDeletionFound_thenReturnSuccessMessage() throws DashboardRecordNotFoundException {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title 5")
                .createdAt(LocalDateTime.of(2024,01,10,18,10,15))
                .updatedAt(LocalDateTime.of(2024,01,15,18,10,15))
                .build();

        Mockito.when(dashboardRepository.findById(any())).thenReturn(Optional.of(dashboard1));
        /*
         1 way of implementation for dashboardService.deleteDashboardById(1L) which is of type void

        doAnswer(Answers.CALLS_REAL_METHODS).when(dashboardRepository).deleteById(any());
        assertEquals(dashboardService.deleteDashboardById(1L),
                ApplicationConstants.DASHBOARD_RECORD_DELETED_SUCCESSFULLY);
        */

        // 2nd way of implementation of test case for dashboardService.deleteDashboardById(1L)
        dashboardService.deleteDashboardById(1L);
        verify(dashboardRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Update Dashboard request when record not present in DB")
    public void whenRecordForUpdateNotFound_thenReturnMessage() {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title 5")
                .createdAt(LocalDateTime.of(2024,01,10,18,10,15))
                .updatedAt(LocalDateTime.of(2024,01,15,18,10,15))
                .build();
        DashboardRecordNotFoundException dashboardRecordNotFoundException = assertThrows(DashboardRecordNotFoundException.class,
                () -> dashboardService.updateDashboard(5L, dashboard1));
        assertEquals(dashboardRecordNotFoundException.getMessage(), ApplicationConstants.DASHBOARD_RECORD_NOT_PRESENT);
    }

    @Test
    @DisplayName("Update the Dashboard request when record present in DB")
    public void whenRecordForUpdateIsPresent_thenReturnSuccessMessage() throws DashboardRecordNotFoundException {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title 5")
                .createdAt(LocalDateTime.of(2024,01,10,18,10,15))
                .updatedAt(LocalDateTime.of(2024,01,15,18,10,15))
                .build();

        Mockito.when(dashboardRepository.findById(any())).thenReturn(Optional.of(dashboard1));
        Mockito.when(dashboardService.saveDashboard(any())).thenReturn(dashboard1);

        Dashboard responseDashboard = dashboardService.updateDashboard(1L, dashboard1);
        assertEquals(responseDashboard.getTitle(), dashboard1.getTitle());
        assertEquals(responseDashboard.getCreatedAt(), dashboard1.getCreatedAt());
    }

    @Test
    @DisplayName("Return the dashboard record when searched by ID and present in DB")
    public void whenRecordIdIsPresent_thenReturnSuccessMessage() throws DashboardRecordNotFoundException {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title 5")
                .createdAt(LocalDateTime.of(2024,01,10,18,10,15))
                .updatedAt(LocalDateTime.of(2024,01,15,18,10,15))
                .build();

        Mockito.when(dashboardRepository.findById(any())).thenReturn(Optional.of(dashboard1));

        Dashboard responseDashboard = dashboardService.getDashboardById(1L);
        assertEquals(responseDashboard.getTitle(), dashboard1.getTitle());
        assertEquals(responseDashboard.getCreatedAt(), dashboard1.getCreatedAt());
    }

    @Test
    @DisplayName("Return the error response when searched by ID and record not present in DB")
    public void whenRecordIdIsNotPresent_thenReturnErrorMessage() throws DashboardRecordNotFoundException {

        Mockito.when(dashboardRepository.findById(any())).thenReturn(Optional.empty());

        DashboardRecordNotFoundException dashboardRecordNotFoundException = assertThrows(DashboardRecordNotFoundException.class,
                () -> dashboardService.getDashboardById(1L));
        assertEquals(dashboardRecordNotFoundException.getMessage(), ApplicationConstants.DASHBOARD_RECORD_NOT_PRESENT);
    }
}
