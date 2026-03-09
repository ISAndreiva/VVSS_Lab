package drinkshop.repository.file;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.repository.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists orders with an ISO-8601 date column (defect A03 - Order needs a
 * date for daily revenue filtering).
 * Format: id,date,productId:qty|productId:qty,total
 */
public class FileOrderRepository
        extends FileAbstractRepository<Integer, Order> {

    private final Repository<Integer, Product> productRepository;

    public FileOrderRepository(String fileName,
                                Repository<Integer, Product> productRepository) {
        super(fileName);
        this.productRepository = productRepository;
        loadFromFile();
    }

    @Override
    protected Integer getId(Order entity) {
        return entity.getId();
    }

    @Override
    protected Order extractEntity(String line) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        LocalDate date = LocalDate.parse(parts[1]);

        List<OrderItem> items = new ArrayList<>();
        for (String product : parts[2].split("\\|")) {
            String[] prodParts = product.split(":");
            int productId = Integer.parseInt(prodParts[0]);
            int quantity = Integer.parseInt(prodParts[1]);
            items.add(new OrderItem(productRepository.findOne(productId), quantity));
        }

        double totalPrice = Double.parseDouble(parts[3]);
        return new Order(id, date, items, totalPrice);
    }

    @Override
    protected String createEntityAsString(Order entity) {
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : entity.getItems()) {
            if (!sb.isEmpty())
                sb.append("|");
            sb.append(item.getProduct().getId()).append(":").append(item.getQuantity());
        }
        LocalDate date = entity.getData() != null ? entity.getData() : LocalDate.now();
        return entity.getId() + "," + date + "," + sb + "," + entity.getTotalPrice();
    }
}
