package com.example.domain;

public class LoggedUser {
    public static User user;

    public static void setUser(User user){
        LoggedUser.user=user;
    }
}
