package project.repository;

import project.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private List<Order> orders;

    public OrderRepository(List<Order> orders) {
        this.orders = orders;
    }

    public OrderRepository() {
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addOrders(List<Order> orders) {
        this.orders.addAll(orders);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public void clearOrders() {
        orders.clear();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public int getOrderCount() {
        return orders.size();
    }

    public Order getOrderByID(String id) {
        for (Order order : orders) {
            if (order.getOrderID().equals(id)) {
                return order;
            }
        }
        return null;
    }
}
