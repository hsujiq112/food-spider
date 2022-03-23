package com.foodspider.service;

import java.util.ArrayList;
import java.util.UUID;

public interface ServiceI<T> {
    public T add(T model);

    public ArrayList<T> dbSet();

    public T update(T model, UUID id);

    void deleteByID(UUID id);
}
