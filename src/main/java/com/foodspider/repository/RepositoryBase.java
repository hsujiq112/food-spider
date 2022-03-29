package com.foodspider.repository;

import com.foodspider.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RepositoryBase<T extends BaseModel> extends JpaRepository<T, UUID> {
    
}
