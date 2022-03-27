package com.foodspider.model.response_model;

import com.foodspider.model.narrowed_model.NarrowedRestaurant;

import java.util.ArrayList;

public class GetRestaurantsResponse extends ResponseBase{
    public ArrayList<NarrowedRestaurant> restaurants;
}
