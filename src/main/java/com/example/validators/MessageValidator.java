package com.example.validators;

import com.example.domain.Message;


public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String message = "";
        if (entity.getMessage().length() == 0) {
            message += "The mesasge can't be null!";
        }
        if (entity.getTo().equals(entity.getFrom())) {
            message += "You can't send a message to you!";
        }
        if (entity.getTo() ==null || entity.getFrom() ==null) {
            message += "The email can't be null!";
        }
        if (entity.getTo_email()==null){
            message+="Email can't be null!";
        }
        if (entity.getFrom_email()==null){
            message+="Email can't be null!";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }

}
