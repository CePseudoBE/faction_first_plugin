package be.cepseudo.first_plugin.storage;

import java.util.Optional;

public interface Storage<T> {
    Optional<T> getById(String id);
    void save(T entity);
    void delete(String id);
}
