package com.foodspider.repository;

import com.foodspider.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends RepositoryBase<Order>{

}
