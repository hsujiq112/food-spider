package com.foodspider.service;

import com.foodspider.model.BaseModel;
import com.foodspider.repository.RepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ServiceBase<T extends BaseModel> {

    @Autowired
    private RepositoryBase<T> repoBase;

    public T add(T model) {
        return repoBase.save(model);
    }

    public ArrayList<T> dbSet() {
        return new ArrayList<>(repoBase.findAll());
    }

    public T update(T userBase, UUID id) {
        var userDB = repoBase.findById(id);
        if (userDB.isEmpty()) {
            return null;
        }
        return repoBase.save(userBase);
    }

    public void deleteByID(UUID id) {
        repoBase.deleteById(id);
    }

    public RepositoryBase<T> getRepo() {
        return repoBase;
    }
}
