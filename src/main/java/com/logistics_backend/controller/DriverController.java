package com.logistics_backend.controller;

import com.logistics_backend.entity.Driver;
import com.logistics_backend.entity.Orders;
import com.logistics_backend.service.DriverImpl;
import com.logistics_backend.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DriverController {

    @Autowired
    private DriverImpl driverImpl;
    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping(value = "/api/drivers")
    public ResponseEntity<List<Driver>> getAllDrivers(){
        List<Driver> driverList = driverImpl.getAllDrivers();
        try {
            if (driverList.size() <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.of(Optional.of(driverList));
    }

    @GetMapping(value = "/api/driver/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable("id") Long id){
        Driver d = driverImpl.getDriverById(id);
        if (d==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(d));
    }

    @PostMapping("/api/driver")
    public ResponseEntity<Driver> createDrivers(@RequestBody Driver driver){
        Driver driver1 = null;
        try {
            driver1 = driverImpl.createDriver(driver);
            return ResponseEntity.status(HttpStatus.CREATED).body(driver1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/api/driver/{id}")
    public ResponseEntity<Driver> updateDriver(@RequestBody Driver driver,@PathVariable("id") Long id){
        try {
            driverImpl.updateDriver(driver,id);
            return ResponseEntity.status(HttpStatus.OK).body(driver);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/api/driver/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id){
        try {
            driverImpl.deleteDriver(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/api/driver/{id}/status")
    public ResponseEntity<Driver> updateDriverStatus(@PathVariable("id") Long id,
                                                     @RequestParam("status") Driver.DriverStatus status) {
        try {
            Driver updatedDriver = driverImpl.updateDriverStatus(id, status);
            return ResponseEntity.ok(updatedDriver);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PatchMapping("/{orderId}/auto-assign")
    public ResponseEntity<Orders> autoAssignDriver(@PathVariable Long orderId) {
        Orders order = orderService.autoAssignDriver(orderId);
        return ResponseEntity.ok(order);
    }



}
