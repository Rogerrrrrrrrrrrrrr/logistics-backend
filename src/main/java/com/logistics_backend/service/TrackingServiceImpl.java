package com.logistics_backend.service;

import com.logistics_backend.entity.Orders;
import com.logistics_backend.entity.TrackingEvent;
import com.logistics_backend.exception.OrderNotFoundException;
import com.logistics_backend.repository.OrderRepo;
import com.logistics_backend.repository.TrackingEventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class TrackingServiceImpl implements TrackingService
{
    @Autowired
    private TrackingEventRepo trackingEventRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Override
    public TrackingEvent addTrackingEvent(Long orderId, String status, String location) {
        Orders orders = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order not found with id " + orderId));

        TrackingEvent event = new TrackingEvent();
        event.setOrders(orders);

        TrackingEvent.Events eventType;

        try {
            eventType = TrackingEvent.Events.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            eventType = TrackingEvent.Events.CREATED_UPDATE;
        }
        event.setEvent(eventType);
        event.setLocationText(location);
        event.setEventTime(LocalDateTime.now());
        event.setCreatedAt(LocalDateTime.now());

        return trackingEventRepo.save(event);
    }

    @Override
    public List<TrackingEvent> getHistory(Long orderId) {
        return trackingEventRepo.findByOrdersId(orderId);
    }

    @Override
    public TrackingEvent getLatest(Long orderId) {
        return trackingEventRepo.findTopByOrdersIdOrderByEventTimeDesc(orderId);
    }

    @Override
    public TrackingEvent getDriverLatest(Long driverId) {
        return trackingEventRepo.findTopByDriverIdOrderByEventTimeDesc(driverId)
                .orElseThrow(()->new RuntimeException("Doesn't Exists"));
    }
}
