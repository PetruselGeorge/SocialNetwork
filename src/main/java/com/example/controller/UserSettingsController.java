package com.example.controller;

import com.example.domain.*;
import com.example.validators.FriendRequestValidator;
import com.example.validators.FriendshipValidator;
import com.example.validators.MessageValidator;
import com.example.validators.UserValidator;
import com.example.georgel.HelloApplication;
import com.example.repository.Repository;
import com.example.repository.db.FriendRequestDbRepository;
import com.example.repository.db.FriendshipDbRepository;
import com.example.repository.db.MessageDbRepository;
import com.example.repository.db.UsersDbRepository;
import com.example.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserSettingsController {
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

    User user = LoggedUser.user;

    @FXML
    private Button updateAccountButton;

    @FXML
    private Button deleteAccountButton;

    @FXML
    private Button userSettingsBackButton;

    public void deleteAccountOnAction(){
        service.deleteUser(user.getId());
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) deleteAccountButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 513, 386);
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAccountOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("update_account.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) updateAccountButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 717, 526);
            stage.setTitle("Update Account");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backUserSettingsButtonOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) userSettingsBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 432, 564);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
