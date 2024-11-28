package ru.t1.java.demo.service;

import java.util.List;

public interface RegisterService<T> {
    List<T> register(Iterable<T> entities);
}
