package com.logistics_backend.service;

import com.logistics_backend.entity.Driver;
import com.logistics_backend.entity.Orders;
import com.logistics_backend.entity.TrackingEvent;
import com.logistics_backend.entity.User;
import com.logistics_backend.exception.DriverNotFoundException;
import com.logistics_backend.exception.InvalidOperationException;
import com.logistics_backend.exception.OrderNotFoundException;
import com.logistics_backend.repository.DriverRepo;
import com.logistics_backend.repository.OrderRepo;
import com.logistics_backend.repository.TrackingEventRepo;
import com.logistics_backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DriverRepo driverRepo;
    @Autowired
    TrackingEventRepo trackingEventRepo;

    @Override
    public Orders createOrder(Orders orders, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        orders.setUser(user);
        orders.setStatus(Orders.OrderStatus.CREATED);
        orders.setDeliveryStatus(Orders.DeliveryStatus.PENDING);
        orders.setPaymentStatus(Orders.PaymentStatus.PENDING);
        orders.setCreatedAt(LocalDateTime.now());
        return orderRepo.save(orders);
    }

    @Override
    public Orders updateOrder(Orders orders, Long id) {
        Orders existingOrder = orderRepo.findById(id).orElseThrow(() ->
                new OrderNotFoundException("Order not found with id " + id));

                if (orders.getDeliveryStatus() != null) {
            existingOrder.setDeliveryStatus(orders.getDeliveryStatus());
        }

        if (orders.getPaymentStatus() != null) {
            existingOrder.setPaymentStatus(orders.getPaymentStatus());
        }

        if (orders.getTotalAmount() != null) {
            existingOrder.setTotalAmount(orders.getTotalAmount());
        }

        if (orders.getDriver() != null) {
            existingOrder.setDriver(orders.getDriver());
        }
        if (orders.getUser() != null) {
            existingOrder.setUser(orders.getUser());
        }
        existingOrder.setUpdatedAt(LocalDateTime.now());
        return orderRepo.save(existingOrder);
    }


    @Override
    public Orders assignDriver(Long orderId, Long driverId) {
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + orderId));

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver with id " + driverId + " is inactive."));

        if (driver.getStatus() != Driver.DriverStatus.AVAILABLE) {
            throw new InvalidOperationException("Driver with id " + driverId + " is not available.");
        }
        order.setDriver(driver);
        order.setStatus(Orders.OrderStatus.ASSIGNED);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepo.save(order);
    }
    private boolean isValidTransition(Orders.OrderStatus current, Orders.OrderStatus next) {
        if (current == Orders.OrderStatus.CREATED) {
            return next == Orders.OrderStatus.ASSIGNED || next == Orders.OrderStatus.CANCELLED;
        } else if (current == Orders.OrderStatus.ASSIGNED) {
            return next == Orders.OrderStatus.ACCEPTED || next == Orders.OrderStatus.CANCELLED;
        } else if (current == Orders.OrderStatus.ACCEPTED) {
            return next == Orders.OrderStatus.PICKED_UP;
        } else if (current == Orders.OrderStatus.PICKED_UP) {
            return next == Orders.OrderStatus.IN_PROGRESS || next == Orders.OrderStatus.RETURNED;
        } else if (current == Orders.OrderStatus.IN_PROGRESS) {
            return next == Orders.OrderStatus.DELIVERED || next == Orders.OrderStatus.RETURNED;
        } else if (current == Orders.OrderStatus.DELIVERED) {
            return next == Orders.OrderStatus.RETURNED;
        }
        return false;
    }


    @Override
    public Orders updateStatus(Long orderId, Orders.OrderStatus status) {
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        Orders.OrderStatus currentStatus = order.getStatus();

        if (!isValidTransition(currentStatus, status)) {
            throw new InvalidOperationException("Invalid status transition from " + currentStatus + " to " + status);
        }
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        TrackingEvent event = new TrackingEvent();
        event.setOrders(order);
        event.setEvent(TrackingEvent.Events.valueOf(status.name()));
        event.setEventTime(LocalDateTime.now());
        trackingEventRepo.save(event);

        return orderRepo.save(order);

        }

    @Override
    public Orders updateDeliveryStatus(Long orderId, Orders.DeliveryStatus deliveryStatus) {
        Orders orders = orderRepo.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order with " + orderId + " not found"));

        if (orders.getPaymentStatus()== Orders.PaymentStatus.NOT_PAID
        && deliveryStatus== Orders.DeliveryStatus.DELIVERED){
            throw new InvalidOperationException("Cannot mark order as delivered without payment");
        }

        orders.setDeliveryStatus(deliveryStatus);
        return orderRepo.save(orders);
    }

    @Override
    public Orders updatePaymentStatus(Long orderId, Orders.PaymentStatus paymentStatus) {
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with " + orderId + " not found"));

        if (paymentStatus == Orders.PaymentStatus.REFUNDED &&
                order.getPaymentStatus() != Orders.PaymentStatus.PAID) {
            throw new InvalidOperationException("Cannot refund an unpaid order.");
        }

        order.setPaymentStatus(paymentStatus);
        return orderRepo.save(order);
    }


    @Override
    public Orders getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }

    @Override
    public List<Orders> getOrdersByUserId(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<Orders> getOrdersByDriverId(Long driverId) {
        return orderRepo.findByDriverId(driverId);
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }

    @Override
    public Orders autoAssignDriver(Long id) {
        Orders orders = orderRepo.findById(id)
                .orElseThrow(()-> new OrderNotFoundException("Order doesn't found"));
        List<Driver> allAvailableDrivers = driverRepo.findByStatus(Driver.DriverStatus.AVAILABLE);
        if (allAvailableDrivers.isEmpty()){
            throw  new InvalidOperationException("No available drivers right now");
        }
        Driver driver = allAvailableDrivers.get(0);
        driver.setStatus(Driver.DriverStatus.BUSY);
        orders.setDriver(driver);
        driverRepo.save(driver);
        return orderRepo.save(orders);
    }
}
