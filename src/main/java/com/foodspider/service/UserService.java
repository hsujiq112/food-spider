package com.foodspider.service;

import com.foodspider.exception.InvalidUserException;
import com.foodspider.model.User;
import com.foodspider.repository.UserRepository;
import com.foodspider.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService implements ServiceI<User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    public ArrayList<User> dbSet() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public User update(User user, UUID id) {
        var userDB = userRepository.findById(id);
        if (userDB.isEmpty()) {
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteByID(UUID id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        var test = userRepository.findAll();
        return userRepository.findAll().stream()
                .filter(i -> i.getUsername().equals(username)).findFirst();
    }

    public User tryLogin(String username, String password) throws Exception {
        var user = findByUsername(username);
        if (user.isEmpty()) {
            return null;
        }
        var passwordDecrypted = Encryptor.decrypt(user.get().getUserId(), user.get().getPassword());
        if (!password.equals(passwordDecrypted)) {
            return null;
        }
        return user.get();
    }

    public User tryRegister(String emailAddress, String firstName, String lastName,
                            String username, String password) throws InvalidUserException, Exception {
        UserValidator.validateUser(emailAddress, firstName, lastName, username, password);
        var user = new User(emailAddress, firstName, lastName, username, password);
        return add(user);
    }

}
