package com.foodspider.controller;

import com.foodspider.exception.MissingCustomerException;
import com.foodspider.model.request_model.AddOrderRequest;
import com.foodspider.model.response_model.ResponseBase;
import com.foodspider.service.OrderService;
import com.foodspider.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class OrderController extends ControllerBase {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserBaseService userBaseService;

    @GetMapping("/getOrdersCountByUserID/{id}")
    public ResponseEntity<ResponseBase> getOrdersCount(@RequestParam UUID id) {
        try {
            userBaseService.getOrdersByUserID(id);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(null);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseBase> placeOrder(@RequestBody AddOrderRequest request) {
        try {
            orderService.tryAddOrder(request.userId, request.foodItems);
        } catch (MissingCustomerException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createEmptyResponse();
    }
}
