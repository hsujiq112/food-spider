package com.foodspider.controller;

import com.foodspider.exception.InvalidOrderChangeException;
import com.foodspider.exception.InvalidUserException;
import com.foodspider.exception.MissingCustomerException;
import com.foodspider.exception.MissingFoodItemException;
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
    public ResponseEntity<ResponseBase> getOrdersCount(@PathVariable UUID id,
                                                       @PathVariable Boolean isAdmin) {
        var response = new GetOrdersCountByUserIDResponse();
        try {
            LOGGER.debug("counting all orders for userId " + id);
            response = userBaseService.getOrdersCountByUserID(id, isAdmin);
        } catch (InvalidUserException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully counted orders; pending orders: " + response.pendingOrders
                + " , placed orders: " + response.placedOrders);
        return createOKResponse(response);
    }

    @GetMapping("/getOrdersByUserID/{id}/{isAdmin}")
    public ResponseEntity<ResponseBase> getOrders(@PathVariable UUID id,
                                                  @PathVariable Boolean isAdmin) {
        List<NarrowedOrder> narrowedOrders;
        try {
            LOGGER.debug("getting all orders by userID... " + id);
            narrowedOrders = userBaseService.getOrdersByUserID(id, isAdmin, -1);
        } catch (InvalidUserException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully got all orders");
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
            LOGGER.debug("getting filtered orders with filter  " + filter);
            narrowedOrders = userBaseService.getOrdersByUserID(id, isAdmin, filter);
        } catch (InvalidUserException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully got all orders filtered with filter " + filter);
        return createOKResponse(new GetOrdersByUserIDResponse(){{
            orders = narrowedOrders;
        }});
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseBase> placeOrder(@RequestBody AddOrderRequest request) {
        try {
            LOGGER.debug("Placing order for user " + request.userId);
            orderService.tryAddOrder(request);
        } catch (MissingCustomerException | MissingFoodItemException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully placed order for user" + request.userId);
        return createEmptyResponse();
    }

    @PatchMapping("/changeStatusToOrder")
    public ResponseEntity<ResponseBase> changeStatusToOrder(@RequestBody ChangeStatusToOrderRequest changeStatusToOrderRequest) {
        try {
            LOGGER.debug("changing status to order " + changeStatusToOrderRequest.orderID
                    + " to " + changeStatusToOrderRequest.status + "...");
            orderService.changeStatusToOrder(changeStatusToOrderRequest.orderID, changeStatusToOrderRequest.status);
        } catch (InvalidOrderChangeException ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        LOGGER.info("Successfully changed status to order " + changeStatusToOrderRequest.status);
        return createEmptyResponse();
    }
}
