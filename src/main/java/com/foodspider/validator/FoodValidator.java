package com.foodspider.validator;

import com.foodspider.exception.InvalidFoodItemException;
import com.foodspider.model.CategoryEnum;

import java.util.regex.Pattern;

public class FoodValidator {

    public static void validateFood(String name, String description, Double price,
                                    CategoryEnum category, String imageLink) {
        if (name.equals("")) {
            throw new InvalidFoodItemException("Empty food item name");
        }
        if (name.length() > 256) {
            throw new InvalidFoodItemException("Food Item name exceeds 256 characters");
        }
        if (description.equals("")) {
            throw new InvalidFoodItemException("Empty food item description");
        }
        if (description.length() > 256) {
            throw new InvalidFoodItemException("Food Item description exceeds 256 characters");
        }
        if (price <= 0) {
            throw new InvalidFoodItemException("Price cannot be 0 or negative");
        }
        if (category == null) {
            throw new InvalidFoodItemException("Invalid category");
        }
        var pattern = Pattern.compile("(https?:\\/\\/)?([\\w\\-])+\\.([a-zA-Z]{2,63})([\\/\\w-]*)*\\/?\\??([^#\\n\\r]*)?#?([^\\n\\r]*)");
        if (imageLink.equals("")) {
            throw new InvalidFoodItemException("Empty image link... c'mon... get a Shutterstock image of the food");
        }
        if (!pattern.matcher(imageLink).matches()) {
            throw new InvalidFoodItemException("Invalid image link URL");
        }
        if (imageLink.length() > 256) {
            throw new InvalidFoodItemException("Image Link too long... find a shorter image URL lol");
        }
    }
}
