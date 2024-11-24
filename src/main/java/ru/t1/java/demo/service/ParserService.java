package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Transaction;

import java.io.IOException;
import java.util.List;

public interface ParserService<T> {
    List<T> parseJson() throws IOException;
}
