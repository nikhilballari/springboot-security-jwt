package com.dashboardapi.demo.repository;

import com.dashboardapi.demo.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Long> {

    Dashboard findByTitle(String Title);
}
