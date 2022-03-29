package com.foodspider.model.response_model;

import com.foodspider.model.narrowed_model.NarrowedOrder;

import java.util.List;

public class GetOrdersByUserIDResponse extends ResponseBase{
    public List<NarrowedOrder> orders;
}
