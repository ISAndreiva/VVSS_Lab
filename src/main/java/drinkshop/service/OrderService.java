package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.repository.Repository;
import drinkshop.service.validator.OrderItemValidator;
import drinkshop.service.validator.OrderValidator;
import drinkshop.service.validator.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages persisted orders and the in-progress "current order".
 * <ul>
 *   <li>defect A04 – validators are now called on every mutation.</li>
 *   <li>defect A04 – duplicate total-computation logic removed; only
 *       {@link Order#computeTotalPrice()} is used.</li>
 *   <li>defect A08 – current-order business state moved out of the UI
 *       controller into this service.</li>
 * </ul>
 */
public class OrderService {

    private final Repository<Integer, Order> orderRepo;
    private final OrderValidator orderValidator;
    private final OrderItemValidator orderItemValidator;

    private Order currentOrder;
    private int nextOrderId;

    public OrderService(Repository<Integer, Order> orderRepo,
                        OrderValidator orderValidator,
                        OrderItemValidator orderItemValidator) {
        this.orderRepo = orderRepo;
        this.orderValidator = orderValidator;
        this.orderItemValidator = orderItemValidator;
        this.nextOrderId = orderRepo.findAll().stream()
                .mapToInt(Order::getId).max().orElse(0) + 1;
        this.currentOrder = new Order(nextOrderId, LocalDate.now());
    }

    // ---- persisted orders ----

    public void addOrder(Order o) {
        orderValidator.validate(o);
        orderRepo.save(o);
    }

    public void updateOrder(Order o) {
        orderValidator.validate(o);
        orderRepo.update(o);
    }

    public void deleteOrder(int id) {
        orderRepo.delete(id);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order findById(int id) {
        return orderRepo.findOne(id);
    }

    // ---- in-progress current order ----

    public void addItemToCurrentOrder(OrderItem item) {
        orderItemValidator.validate(item);
        currentOrder.addItem(item);
    }

    public void removeItemFromCurrentOrder(OrderItem item) {
        currentOrder.removeItem(item);
    }

    public List<OrderItem> getCurrentOrderItems() {
        return new ArrayList<>(currentOrder.getItems());
    }

    /** Single source of truth: delegates to {@link Order#computeTotalPrice()}. */
    public double computeCurrentOrderTotal() {
        currentOrder.computeTotalPrice();
        return currentOrder.getTotal();
    }

    /**
     * Validates, persists the current order and starts a fresh one.
     * @return the finalized order (for receipt generation)
     */
    public Order finalizeCurrentOrder() {
        if (currentOrder.getItems().isEmpty())
            throw new ValidationException("Comanda nu contine produse!");
        currentOrder.computeTotalPrice();
        orderValidator.validate(currentOrder);
        orderRepo.save(currentOrder);
        Order finalized = currentOrder;
        nextOrderId = finalized.getId() + 1;
        currentOrder = new Order(nextOrderId, LocalDate.now());
        return finalized;
    }
}