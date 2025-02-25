package be.cepseudo.first_plugin.storage;

import java.util.Optional;
import java.util.UUID;

public interface Storage<T> {
    Optional<T> getById(String id);
    Optional<T> getByName(String name);
    Optional<T> getByUUID(UUID uuid);
    void save(T entity);
    void delete(String id);
}
