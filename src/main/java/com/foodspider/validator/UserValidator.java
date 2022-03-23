package com.foodspider.validator;

import com.foodspider.exception.InvalidUserException;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressCriteria;
import org.hazlewood.connor.bottema.emailaddress.EmailAddressValidator;

import java.util.regex.Pattern;

public class UserValidator {
    public static void validateUser(String emailAddress, String firstName,
                                    String lastName, String username, String password) {
        if (emailAddress.equals("")) {
            throw new InvalidUserException("Empty Email Address");
        }
        if (!EmailAddressValidator.isValid(emailAddress, EmailAddressCriteria.RECOMMENDED)) {
            throw new InvalidUserException("Invalid Email Address");
        }
        if (emailAddress.length() > 255) {
            throw new InvalidUserException("Email Address exceeds 255 characters");
        }
        var pattern = Pattern.compile("[^\\sa-zAZ_-]", Pattern.CASE_INSENSITIVE);
        if (firstName.equals("")) {
            throw new InvalidUserException("Invalid First Name");
        }
        if (pattern.matcher(firstName).find()) {
            throw new InvalidUserException("Illegal characters found in First Name");
        }
        if (lastName.equals("")) {
            throw new InvalidUserException("Invalid Last Name");
        }
        if (pattern.matcher(lastName).find()) {
            throw new InvalidUserException("Illegal characters found in Last Name");
        }
        if (password.equals("")) {
            throw new InvalidUserException("Empty Password?");
        }
        if (password.length() < 6) {
            throw new InvalidUserException("Password must be at least 6 characters");
        }
        if (username.equals("")) {
            throw new InvalidUserException("Invalid Username");
        }

    }
}
