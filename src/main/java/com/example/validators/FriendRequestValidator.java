package com.example.validators;

import com.example.domain.FriendRequest;


public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String messages = "";
        if (entity.getId1().equals(entity.getId2()))
            messages += "A friendship can't be created with the same user!";
        if (entity.getId1() == null || entity.getId2() == null)
            messages += "ID's can't be null!";
        if (entity.getEmailId2()==null){
            messages+="Email can't be null!";
        }
        if (entity.getEmail_id1()==null){
            messages+="Email can't be null!";
        }
        if (messages.length() != 0) {
            throw new ValidationException(messages);
        }
    }
}

