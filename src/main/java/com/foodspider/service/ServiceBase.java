package com.foodspider.service;

import com.foodspider.model.BaseModel;
import com.foodspider.repository.RepositoryBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The Service base.
 *
 * @param <T> the model type
 */
public abstract class ServiceBase<T extends BaseModel> {

    @Autowired
    private RepositoryBase<T> repoBase;

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> modelClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];


    /**
     * Sets repo base.
     *
     * @param repoBase the repo base
     */
    public void setRepoBase(RepositoryBase<T> repoBase) {
        this.repoBase = repoBase;
    }

    /**
     * Adds model to db.
     *
     * @param model the model
     * @return the t
     */
    public T add(T model) {
        LOGGER.info("saving model... " + modelClass);
        return repoBase.save(model);
    }

    /**
     * Get the dbSet.
     *
     * @return the database table entries as an array list
     */
    public ArrayList<T> dbSet() {
        LOGGER.info("getting all rows from db... " + modelClass);
        return new ArrayList<>(repoBase.findAll());
    }

    /**
     * Update model.
     *
     * @param model the model
     * @param id    its id
     * @return the newly updated model
     */
    public T update(T model, UUID id) {
        LOGGER.info("updating model... " + modelClass);
        var modelDB = repoBase.findById(id);
        if (modelDB.isEmpty()) {
            return null;
        }
        return repoBase.save(model);
    }

    /**
     * Force update if needed.
     *
     * @param model the model
     * @param id    the id
     */
    @Transactional
    public void forceUpdate(T model, UUID id) {
        LOGGER.info("forcing update... " + modelClass);
        var modelDB = repoBase.findById(id);
        if (modelDB.isEmpty()) {
            return;
        }
        entityManager.merge(model);
    }


    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    public T getByID(UUID id) {
        LOGGER.info("getting by id... " + modelClass);
        return repoBase.getById(id);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteByID(UUID id) {
        LOGGER.info("deleting by id... " + modelClass);
        repoBase.deleteById(id);
    }

    /**
     * Gets repo when needed to do some more complex operations.
     *
     * @return the repositoryBase
     */
    public RepositoryBase<T> getRepo() {
        LOGGER.info("getting whole repository for additional operations... " + modelClass);
        return repoBase;
    }

    /**
     * The constant LOGGER.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ServiceBase.class);
}
