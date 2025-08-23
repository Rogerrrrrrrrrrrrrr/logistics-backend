package com.logistics_backend.repository;

import com.logistics_backend.entity.TrackingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackingEventRepo extends JpaRepository<TrackingEvent,Long> {

    List<TrackingEvent> findByOrdersId(Long orderId);
    TrackingEvent findTopByOrdersIdOrderByEventTimeDesc(Long orderId);
    Optional<TrackingEvent> findTopByDriverIdOrderByEventTimeDesc(Long driverId);
}
