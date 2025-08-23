package com.logistics_backend.service;

import com.logistics_backend.entity.Driver;
import com.logistics_backend.exception.DriverNotFoundException;
import com.logistics_backend.repository.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverImpl implements DriverService{

    @Autowired
    private DriverRepo driverRepo;

    @Override
    public Driver createDriver(Driver driver) {
        driver.setCreatedAt(LocalDateTime.now());
        //driver.setUpdatedAt(LocalDateTime.now());
        return this.driverRepo.save(driver);
    }

    @Override
    public Driver getDriverById(Long id) {
        return this.driverRepo.findById(id)
                .orElseThrow(()-> new DriverNotFoundException("Driver with id " + id + " not found"));
    }

    @Override
    public List<Driver> getAllDrivers() {

        return driverRepo.findAll();
    }

    @Override
    public Driver updateDriver(Driver driver, Long id) {
        Driver driver1 = getDriverById(id);

        driver1.setName(driver.getName());
        driver1.setPhoneNumber(driver.getPhoneNumber());
        driver1.setStatus(driver.getStatus());

        return driverRepo.save(driver1);
    }

    @Override
    public void deleteDriver(Long id) {
        if (!driverRepo.existsById(id)) {
            throw new DriverNotFoundException("Driver with id " + id + " not found");
        }
        driverRepo.deleteById(id);
    }

    @Override
    public Driver updateDriverStatus(Long id, Driver.DriverStatus status) {
        Driver driver = driverRepo.findById(id)
                .orElseThrow(()-> new DriverNotFoundException("Driver with id " + id + " not found"));
        driver.setStatus(status);

        return driverRepo.save(driver);
    }
}
