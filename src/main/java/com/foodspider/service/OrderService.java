package com.foodspider.service;

import com.foodspider.exception.InvalidOrderChangeException;
import com.foodspider.exception.MissingCustomerException;
import com.foodspider.exception.MissingFoodItemException;
import com.foodspider.model.*;
import com.foodspider.model.request_model.AddOrderRequest;
import com.foodspider.model.request_model.RequestBase;
import com.itextpdf.text.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class OrderService extends ServiceBase<Order> {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private RestaurantService restaurantService;

    public void tryAddOrder(AddOrderRequest request) throws Exception {
        var user = customerService.getByID(request.userId);
        if (user == null) {
            throw new MissingCustomerException("Customer not found");
        }
        if (request.foodItems == null || request.foodItems.isEmpty()) {
            throw new MissingFoodItemException("An order cannot have 0 food items!");
        }
        var fullFoodItems = request.foodItems.stream()
                .map(i -> foodItemService.getByID(i)).toList();
        var restaurant = restaurantService.getByID(request.restaurantId);
        var order = new Order(restaurant, user, fullFoodItems);
        user.getOrders().add(order);
        forceUpdate(order, order.getId());
        customerService.forceUpdate(user, request.userId);
        sendMail(request, user, restaurant);
    }

    private void sendMail(AddOrderRequest request, Customer user, Restaurant restaurant) throws Exception {
        var properties = new Properties();
        properties.put("mail.smtp.host", "smtp.mailtrap.io");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        var session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("f084ff6ee14578", "f70ca03ebc31ec");
            }
        });
        var ref = new Object() {
            String msg = "<h1>Hey! Gimme your food now!</h1>" +
                    "<br>" +
                    "Details:" +
                    "<br>";
        };
        var message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user.getEmailAddress()));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(restaurant.getAdministrator().getEmailAddress()));
        message.setSubject("New Order Placed!");

        Supplier<Stream<FoodItem>> fullFoodItems = () -> request.foodItems.stream().map(i -> foodItemService.getByID(i));
        fullFoodItems.get().forEach(i -> {
            ref.msg += i.toString();
            ref.msg += "<br>";
        });
        ref.msg += "<br>" +
                "Total price: " + fullFoodItems.get().mapToDouble(FoodItem::getPrice).sum();
        ref.msg += "<br>" +
                "Address: " + request.address;
        ref.msg += request.specialDetails.equals("") ? "" : "<br>" +
                "Special details: " + request.specialDetails;

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(ref.msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public void changeStatusToOrder(UUID orderID, Integer status) {
        var order = getByID(orderID);
        if (order.getOrderStatus().equals(OrderStatusEnum.DECLINED)) {
            throw new InvalidOrderChangeException("Cannot change from Declined Order to something else!");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED)) {
            throw new InvalidOrderChangeException("Cannot change from Delivered Order to something else!");
        }
        order.setOrderStatus(OrderStatusEnum.values()[status]);
        update(order, orderID);
    }
}
