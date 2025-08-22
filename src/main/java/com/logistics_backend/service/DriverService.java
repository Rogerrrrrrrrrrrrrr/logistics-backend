package com.logistics_backend.service;

import com.logistics_backend.entity.Driver;

import java.util.List;

public interface DriverService {

  Driver createDriver(Driver driver);
   Driver getDriverById(Long id);
   List<Driver> getAllDrivers();
   Driver updateDriver(Driver driver,Long id);
   void deleteDriver(Long id);
   Driver updateDriverStatus(Long id, Driver.DriverStatus status);
}
