package orderscontrol.repository;

import java.util.Optional;
import java.util.List;

public interface CrudRepository<T> {
    T create(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    void deleteById(int id);
}
