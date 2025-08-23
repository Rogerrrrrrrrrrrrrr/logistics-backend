package com.logistics_backend.service;

import com.logistics_backend.entity.Orders;

import java.util.List;

public interface OrderService {

    Orders createOrder(Orders orders,Long id);
    Orders updateOrder(Orders orders,Long id);
    Orders assignDriver(Long orderId,Long driverId);
    Orders updateStatus(Long orderId, Orders.OrderStatus status);
    Orders updateDeliveryStatus(Long orderId, Orders.DeliveryStatus deliveryStatus);
    Orders updatePaymentStatus(Long orderId, Orders.PaymentStatus paymentStatus);
    Orders getOrderById(Long id);
    List<Orders> getOrdersByUserId(Long userId);
    List<Orders> getOrdersByDriverId(Long driverId);
    List<Orders> getAllOrders();
    void deleteOrder(Long id);
    Orders autoAssignDriver(Long id);

}
