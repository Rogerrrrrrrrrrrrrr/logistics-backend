package com.logistics_backend.repository;

import com.logistics_backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepo extends JpaRepository<Driver,Long> {

    List<Driver> findByStatus(Driver.DriverStatus status);
}
