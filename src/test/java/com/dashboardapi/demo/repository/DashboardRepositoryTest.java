package com.dashboardapi.demo.repository;

import com.dashboardapi.demo.entity.Dashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DashboardRepositoryTest {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void setUp() {
        Dashboard dashboard1 = Dashboard.builder()
                .title("Test Title")
                .createdAt(LocalDateTime.of(2024,01,10,18,10,15))
                .updatedAt(LocalDateTime.of(2024,01,15,18,10,15))
                .build();
        testEntityManager.persist(dashboard1);
    }

    //@Disabled
    @Test
    public void whenFindByTitle_thenReturnDashboard() {

        Dashboard dashboard = dashboardRepository.findByTitle("Test Title");
        assertEquals(dashboard.getTitle(), "Test Title");
    }
}
