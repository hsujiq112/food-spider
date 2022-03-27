package com.foodspider.validator;

import com.foodspider.exception.InvalidRestaurantException;
import com.foodspider.model.CategoryEnum;

import java.util.regex.Pattern;

public class FoodValidator {

    public static void validateFood(String name, String description, Double price,
                                    CategoryEnum category, String imageLink) {
        if (name.equals("")) {
            throw new InvalidRestaurantException("Empty food item name");
        }
        if (name.length() > 256) {
            throw new InvalidRestaurantException("Food Item name exceeds 256 characters");
        }
        if (description.equals("")) {
            throw new InvalidRestaurantException("Empty food item description");
        }
        if (description.length() > 256) {
            throw new InvalidRestaurantException("Food Item description exceeds 256 characters");
        }
        if (price <= 0) {
            throw new InvalidRestaurantException("Price cannot be 0 or negative");
        }
        if (category == null) {
            throw new InvalidRestaurantException("Invalid category");
        }
        var pattern = Pattern.compile("(http(s)?:/\\/.)?(www\\.)?" +
                "[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\." +
                "[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&/=]*)");
        if (!pattern.matcher(imageLink).matches()) {
            throw new InvalidRestaurantException("Invalid image link URL");
        }
        if (imageLink.equals("")) {
            throw new InvalidRestaurantException("Empty image link... c'mon... get a Shutterstock image of the food");
        }
        if (imageLink.length() > 256) {
            throw new InvalidRestaurantException("Image Link too long... find a shorter image URL lol");
        }
    }
}
