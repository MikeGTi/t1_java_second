package ru.t1.java.demo.service;

public interface HandleService<T> {
    void handle(Iterable<T> entities);
}
