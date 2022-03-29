package com.foodspider.model.request_model;

import java.util.UUID;

public class ChangeStatusToOrderRequest extends RequestBase{
    public UUID orderID;
    public Integer status;
}
