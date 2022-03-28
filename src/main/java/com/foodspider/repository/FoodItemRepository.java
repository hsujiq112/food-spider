package com.foodspider.repository;

import com.foodspider.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FoodItemRepository extends RepositoryBase<FoodItem>{

}
