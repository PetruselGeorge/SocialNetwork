package com.example.controller;

import com.example.domain.FriendRequest;
import com.example.domain.Friendship;
import com.example.domain.Message;
import com.example.domain.User;
import com.example.domain.validators.*;
import com.example.georgel.HelloApplication;
import com.example.repository.Repository;
import com.example.repository.db.FriendRequestDbRepository;
import com.example.repository.db.FriendshipDbRepository;
import com.example.repository.db.MessageDbRepository;
import com.example.repository.db.UsersDbRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.service.Service;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;


import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RegisterController {
    Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final FriendshipDbRepository friendshipRepository1 = new FriendshipDbRepository(connection, new FriendshipValidator());
    private final Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository(connection, new FriendshipValidator());
    private final Repository<Long, User> userRepository = new UsersDbRepository(connection, new UserValidator(), friendshipRepository1);
    private final Repository<Long, Message> messageRepository = new MessageDbRepository(connection, new MessageValidator());
    private final Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDbRepository(connection, new FriendRequestValidator());
    private final Service service = new Service(userRepository, friendshipRepository, messageRepository, friendRequestRepository);

    @FXML
    private Button cancelRegistration;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private Label errorMessageLabel;

    public void cancelRegistrationButtonOnAction() {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) cancelRegistration.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 513, 386);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAccountButtonOnAction() {
        try {
            createAccoutValidation();
            service.saveUser(firstnameTextField.getText(), lastnameTextField.getText(), emailTextField.getText(), passwordPasswordField.getText());

        } catch (IllegalArgumentException | ValidationException  exception) {
            errorMessageLabel.setText(exception.getMessage());
        }
        if (errorMessageLabel.getText().isBlank()) {
            cancelRegistrationButtonOnAction();
        }
    }

    public void createAccoutValidation(){
        for (User user:userRepository.findAll()){
            if (user.getEmail().equals(emailTextField.getText()))
            {
                throw new IllegalArgumentException("This email already exists!");
            }
        }
    }
}
