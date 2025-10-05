package orderscontrol.repository;

import orderscontrol.model.Customer;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer> {
    public List<Customer> findByPhone(String phone);
    public List<Customer> findByEmail(String email);
}
