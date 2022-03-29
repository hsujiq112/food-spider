package com.foodspider.service;

import com.foodspider.model.BaseModel;
import com.foodspider.repository.RepositoryBase;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.UUID;

public abstract class ServiceBase<T extends BaseModel> {

    @Autowired
    private RepositoryBase<T> repoBase;

    @PersistenceContext
    private EntityManager entityManager;

    public T add(T model) {
        return repoBase.save(model);
    }

    public ArrayList<T> dbSet() {
        return new ArrayList<>(repoBase.findAll());
    }

    public T update(T model, UUID id) {
        var modelDB = repoBase.findById(id);
        if (modelDB.isEmpty()) {
            return null;
        }
        return repoBase.save(model);
    }

    @Transactional
    public void forceUpdate(T model, UUID id) {
        var modelDB = repoBase.findById(id);
        if (modelDB.isEmpty()) {
            return;
        }
        entityManager.merge(model);
    }


    public T getByID(UUID id) {
        return repoBase.getById(id);
    }

    public void deleteByID(UUID id) {
        repoBase.deleteById(id);
    }

    public RepositoryBase<T> getRepo() {
        return repoBase;
    }
}
