package ru.t1.java.demo.service;

import java.util.List;
import java.util.UUID;

public interface GenericService<T> {

    T create(T entity);

    T findById(Long id);

    T findByUuid(UUID uuid);

    List<T> findAll();

    T update(UUID uuid, T entity);

    void delete(UUID uuid);
}
