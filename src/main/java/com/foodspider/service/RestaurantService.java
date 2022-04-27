package com.foodspider.service;

import com.foodspider.model.CategoryEnum;
import com.foodspider.model.FoodItem;
import com.foodspider.model.Restaurant;
import com.foodspider.model.narrowed_model.NarrowedRestaurant;
import com.foodspider.validator.FoodValidator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.hibernate.Hibernate;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class RestaurantService extends ServiceBase<Restaurant> {

    public ArrayList<Restaurant> dbSet() {
        return new ArrayList<>(getRepo().findAll());
    }

    public ArrayList<FoodItem> getFoodItemsByRestaurantID(UUID id) {
        return new ArrayList<>(getRepo().getById(id).getFoodItems());
    }

    public ArrayList<Integer> getCategoriesByRestaurantID(UUID id) {
        return new ArrayList<>(getRepo().getById(id).getCategories());
    }

    public void addFoodToRestaurant(UUID restaurantID, String foodName, String foodDescription, Double price,
                                    CategoryEnum categoryEnum, String foodImageLink) {
        var restaurant = getRepo().getById(restaurantID);
        FoodValidator.validateFood(foodName, foodDescription, price, categoryEnum, foodImageLink);
        var foodItem = new FoodItem(foodName, foodDescription, price, categoryEnum, foodImageLink);
        restaurant.getFoodItems().add(foodItem);
        foodItem.setRestaurant(restaurant);
        getRepo().save(restaurant);
    }

    public ArrayList<NarrowedRestaurant> getNarrowedRestaurants(String filter) {
        var restaurants = dbSet().stream().map(i -> new NarrowedRestaurant(){{
            id = i.getId();
            name = i.getName();
            location = i.getLocation();
            deliveryZones = i.getDeliveryZones();
            categories = i.getCategories();
        }}).toList();
        if (filter != null) {
            restaurants = restaurants.stream().filter(i -> i.name.toLowerCase().contains(filter)).toList();
        }
        restaurants = restaurants.stream().sorted(Comparator.comparing(i -> i.name)).toList();
        return new ArrayList<>(restaurants);
    }

    public Map.Entry<ByteArrayOutputStream, String> createPDF(UUID restaurantID) throws Exception {
        var restaurant = (Restaurant) Hibernate.unproxy(getByID(restaurantID));
        var document = new PDDocument();
        var page = new PDPage();
        document.addPage(page);
        var contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 700);
        var restaurantString = restaurant.toString();
        var restaurantStrings = restaurantString.split("\r");
        for (var string : restaurantStrings) {
            contentStream.showText(string);
            contentStream.newLineAtOffset(0, -15);
        }
        var bOutStream = new ByteArrayOutputStream();
        contentStream.endText();
        contentStream.close();
        document.save(bOutStream);
        document.close();
        return new AbstractMap.SimpleEntry<>(bOutStream, restaurant.getName());
    }
}
