package com.example.domain.validators;

import com.example.domain.Friendship;


public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String messages = "";
        if (entity.getId1().equals(entity.getId2()))
            messages += "A friendship can't be created with the same user!";
        if (entity.getId1() <0L || entity.getId2() <0L )
            messages += "An ID can't be negative!";
        if (entity.getEmail_id2()==null){
            messages+="Email can't be null!";
        }
        if (messages.length() != 0) {
            throw new ValidationException(messages);
        }
    }
}
