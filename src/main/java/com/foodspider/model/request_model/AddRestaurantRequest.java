package com.foodspider.model.request_model;

import java.util.List;
import java.util.UUID;

public class AddRestaurantRequest extends RequestBase {
    public UUID adminID;
    public String name;
    public String location;
    public String deliveryZones;
    public List<Integer> categories;
}
