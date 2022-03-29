package com.foodspider.model.narrowed_model;

import com.foodspider.model.OrderStatusEnum;

import java.util.List;
import java.util.UUID;

public class NarrowedOrder {

    public UUID id;
    public List<NarrowedFoodItem> foodItems;
    public OrderStatusEnum status;
    public String clientFirstName;
    public String clientLastName;
}
