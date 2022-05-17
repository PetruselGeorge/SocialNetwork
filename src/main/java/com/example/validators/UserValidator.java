package com.example.validators;


import com.example.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String message = "";
        if (entity.getLastName().length() == 0) {
            message += "Last name can't be an empty string!";
        }
        if (entity.getFirstName().length() == 0) {
            message += "First name can't be an empty string!";
        }
        if (entity.getEmail().length() == 0) {
            message += "Email can't be an empty string!";
        }
        if (entity.getPassword().length() == 0) {
            message += "Password can't be an empty string!";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }
}

