package orderscontrol.repository;

import orderscontrol.model.Product;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product> {
    public Product findByCategory(String category);
    public void updatePrice(Product product, float price);
    public void updateQuantity(Product product, int quantity);
}
