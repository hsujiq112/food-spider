package com.foodspider.model.request_model;

import java.util.List;
import java.util.UUID;

public class AddOrderRequest extends RequestBase{
    public List<UUID> foodItems;
    public UUID userId;
    public UUID restaurantId;
}
