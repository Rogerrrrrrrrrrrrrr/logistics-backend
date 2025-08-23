package com.logistics_backend.controller;

import com.logistics_backend.entity.TrackingEvent;
import com.logistics_backend.service.TrackingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingServiceImpl trackingService;

    @PostMapping("/{orderId}")
    public ResponseEntity<TrackingEvent> addTrackingEvent(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam String location) {
        TrackingEvent event = trackingService.addTrackingEvent(orderId, status, location);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<TrackingEvent>> getTrackingHistory(@PathVariable Long orderId) {
        List<TrackingEvent> history = trackingService.getHistory(orderId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{orderId}/latest")
    public ResponseEntity<TrackingEvent> getLatestTracking(@PathVariable Long orderId) {
        TrackingEvent latestEvent = trackingService.getLatest(orderId);
        return ResponseEntity.ok(latestEvent);
    }

}
