package com.logistics_backend.controller;

import com.logistics_backend.entity.Orders;
import com.logistics_backend.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping("/orders/{userId}")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order, @PathVariable Long userId) {
        return ResponseEntity.ok(orderService.createOrder(order, userId));
    }
    @PatchMapping("/{orderId}/assign/{driverId}")
    public ResponseEntity<Orders> assignDriver(@PathVariable Long orderId, @PathVariable Long driverId) {
        return ResponseEntity.ok(orderService.assignDriver(orderId, driverId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Orders> updateOrderStatus(@PathVariable Long orderId,
                                                   @RequestParam Orders.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
       Orders orders = orderService.getOrderById(id);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable Long id, @RequestBody Orders orders) {
        Orders updated = orderService.updateOrder(orders, id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/delivery-status")
    public ResponseEntity<Orders> updateDeliveryStatus(@PathVariable Long id,
                                                       @RequestParam("status") Orders.DeliveryStatus status) {
        Orders updated = orderService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<Orders> updatePaymentStatus(@PathVariable Long id,
                                                      @RequestParam("status") Orders.PaymentStatus status) {
        Orders updated = orderService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Orders>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/orders/driver/{driverId}")
    public ResponseEntity<List<Orders>> getOrdersByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(orderService.getOrdersByDriverId(driverId));
    }

    @PatchMapping("/{orderId}/status-flow")
    public ResponseEntity<Orders> updateOrderStatusFlow(
            @PathVariable Long orderId,
            @RequestParam Orders.OrderStatus newStatus) {
        Orders order = orderService.updateStatus(orderId, newStatus);
        return ResponseEntity.ok(order);
    }

}

