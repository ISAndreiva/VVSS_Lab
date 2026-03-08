package drinkshop.service;

import drinkshop.domain.Order;
import drinkshop.repository.Repository;

import java.util.List;

/**
 * Daily revenue reporting service. Moved from the {@code drinkshop.reports}
 * package into the service layer where it logically belongs (defect A02).
 */
public class DailyReportService {

    private final Repository<Integer, Order> repo;

    public DailyReportService(Repository<Integer, Order> repo) {
        this.repo = repo;
    }

    public double getTotalRevenue() {
        return repo.findAll().stream().mapToDouble(Order::getTotal).sum();
    }

    public int getTotalOrders() {
        return repo.findAll().size();
    }
}
