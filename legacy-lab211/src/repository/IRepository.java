package repository;

import model.BaseEntity;
import java.util.List;

public interface IRepository<T extends BaseEntity> {
    List<T> findAll();
    T findById(String id);
    void save(T entity);
    void saveAll(List<T> entities);
    void deleteById(String id);
}
