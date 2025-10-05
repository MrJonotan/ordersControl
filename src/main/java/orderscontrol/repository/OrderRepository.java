package orderscontrol.repository;

import orderscontrol.model.Order;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order> {
    public List<Order> findByCustomer(String customerName);
    public List<Order> findByProduct(String productName);
    public List<Order> findByStatus(String statusName);
    public List<Order> findByOrderDate(LocalDateTime orderDate);
}
