package orderscontrol.repository;

import java.util.Optional;
import java.util.List;
// Интерфейс для реализации CRUD для всех объектов, реализующий данный интерфейс
public interface CrudRepository<T> {
    void create(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    void deleteById(int id);
}
