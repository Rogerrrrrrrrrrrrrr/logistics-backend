package com.logistics_backend.repository;

import com.logistics_backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orders,Long> {

    List<Orders> findByUserId(Long userId);
    List<Orders> findByDriverId(Long driverId);
}
