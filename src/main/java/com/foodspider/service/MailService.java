package com.foodspider.service;

import com.foodspider.model.Customer;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.model.request_model.AddOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The Mail Service
 */
public class MailService {

    @Autowired
    private FoodItemService foodItemService;

    /**
     * Send mail.
     *
     * @param request the order request
     * @param user the customer from whom to send the email
     * @param restaurant the admin's restaurant
     * @throws Exception in case the mail cannot be sent
     */
    public void sendMail(AddOrderRequest request,
                          Customer user,
                          Restaurant restaurant) throws MessagingException {
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
        var message = createMailContents(request, session, user, restaurant);
        Transport.send(message);
    }

    /**
     *
     * @param request       the order request
     * @param session       the mail session
     * @param user          the customer from whom to send the email
     * @param restaurant    the admin's restaurant
     * @return the MimeMessage to be sent
     * @throws Exception in case the smtp credentials are incorrect
     */
    private MimeMessage createMailContents(AddOrderRequest request,
                                      Session session,
                                      Customer user,
                                      Restaurant restaurant) throws MessagingException {
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

        return message;
    }
}
