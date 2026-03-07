package drinkshop.domain;

/**
 * One line in an order. Serializable removed – persistence is file-based
 * (defect A08).
 */
public class OrderItem {

    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotal() { return product.getPret() * quantity; }

    @Override
    public String toString() {
        return "OrderItem{product=" + product + ", quantity=" + quantity + '}';
    }
}
