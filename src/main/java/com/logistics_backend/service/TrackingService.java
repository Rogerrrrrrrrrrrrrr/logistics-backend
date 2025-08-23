package com.logistics_backend.service;

import com.logistics_backend.entity.TrackingEvent;

import java.util.List;

public interface TrackingService {

    TrackingEvent addTrackingEvent(Long orderId, String status, String location);
    List<TrackingEvent> getHistory(Long orderId);
    TrackingEvent getLatest(Long orderId);
    TrackingEvent getDriverLatest(Long driverId);
}
