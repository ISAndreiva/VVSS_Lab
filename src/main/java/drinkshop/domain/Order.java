package drinkshop.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A customer order. Tracks the order date (defect A03) and no longer
 * implements Serializable which was redundant for file-based persistence
 * (defect A08).
 */
public class Order {

    private int id;
    private LocalDate data;
    private List<OrderItem> items;
    private double totalPrice;

    public Order(int id, LocalDate data) {
        this.id = id;
        this.data = data;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Order(int id, LocalDate data, List<OrderItem> items, double totalPrice) {
        this.id = id;
        this.data = data;
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
    }

    public int getId() { return id; }

    public LocalDate getData() { return data; }

    public void setData(LocalDate data) { this.data = data; }

    public List<OrderItem> getItems() { return items; }

    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalPrice() { return totalPrice; }

    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public void addItem(OrderItem item) { this.items.add(item); }

    public void removeItem(OrderItem item) { this.items.remove(item); }

    /** Recomputes totalPrice from the current items list. Single source of truth. */
    public void computeTotalPrice() {
        this.totalPrice = items.stream().mapToDouble(OrderItem::getTotal).sum();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", data=" + data + ", items=" + items + ", totalPrice=" + totalPrice + '}';
    }
}