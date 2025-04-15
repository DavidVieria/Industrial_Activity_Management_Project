package project.domain.model;

import project.domain.enums.Priority;

import java.util.Objects;

public class Order {

    private String orderID;
    private Item item;
    private Priority priority;
    private double quantity;

    public Order(String orderID, Item item, Priority priority, double quantity) {
        this.orderID = orderID;
        this.item = item;
        this.priority = priority;
        this.quantity = quantity;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(quantity, order.quantity) == 0 && Objects.equals(orderID, order.orderID) && Objects.equals(item, order.item) && priority == order.priority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, item, priority, quantity);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID='" + orderID + '\'' +
                ", item=" + item +
                ", priority=" + priority +
                ", quantity=" + quantity +
                '}';
    }

}
