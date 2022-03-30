package com.foodspider.controller;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.exception.MissingCustomerException;
import com.foodspider.model.narrowed_model.NarrowedOrder;
import com.foodspider.model.request_model.AddOrderRequest;
import com.foodspider.model.request_model.ChangeStatusToOrderRequest;
import com.foodspider.model.response_model.GetOrdersByUserIDResponse;
import com.foodspider.model.response_model.GetOrdersCountByUserIDResponse;
import com.foodspider.model.response_model.ResponseBase;
import com.foodspider.service.OrderService;
import com.foodspider.service.UserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController extends ControllerBase {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserBaseService userBaseService;

    @GetMapping("/getOrdersCountByUserID/{id}/{isAdmin}")
    public ResponseEntity<ResponseBase> getOrdersCount(@PathVariable UUID id, @PathVariable Boolean isAdmin) {
        var response = new GetOrdersCountByUserIDResponse();
        try {
            response = userBaseService.getOrdersCountByUserID(id, isAdmin);
        } catch (InvalidUserException ex) {
          return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(response);
    }

    @GetMapping("/getOrdersByUserID/{id}/{isAdmin}")
    public ResponseEntity<ResponseBase> getOrders(@PathVariable UUID id, @PathVariable Boolean isAdmin) {
        List<NarrowedOrder> narrowedOrders;
        try {
            narrowedOrders = userBaseService.getOrdersByUserID(id, isAdmin, -1);
        } catch (InvalidUserException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new GetOrdersByUserIDResponse(){{
            orders = narrowedOrders;
        }});
    }

    @GetMapping("/getOrdersByUserIDFiltered/{id}/{isAdmin}/{filter}")
    public ResponseEntity<ResponseBase> getOrdersFiltered(@PathVariable UUID id,
                                                          @PathVariable Boolean isAdmin,
                                                          @PathVariable Integer filter) {
        List<NarrowedOrder> narrowedOrders;
        try {
            narrowedOrders = userBaseService.getOrdersByUserID(id, isAdmin, filter);
        } catch (InvalidUserException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createOKResponse(new GetOrdersByUserIDResponse(){{
            orders = narrowedOrders;
        }});
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseBase> placeOrder(@RequestBody AddOrderRequest request) {
        try {
            orderService.tryAddOrder(request.restaurantId, request.userId, request.foodItems);
        } catch (MissingCustomerException ex) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createEmptyResponse();
    }

    @PatchMapping("/changeStatusToOrder")
    public ResponseEntity<ResponseBase> changeStatusToOrder(@RequestBody ChangeStatusToOrderRequest changeStatusToOrderRequest) {
        try {
            orderService.changeStatusToOrder(changeStatusToOrderRequest.orderID, changeStatusToOrderRequest.status);
        } catch (Exception ex) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return createEmptyResponse();
    }
}
