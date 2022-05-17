package com.example.controller;

import com.example.domain.*;
import com.example.validators.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UpdateAccountController {
    Connection connection;
    User user = LoggedUser.user;
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
    private Button finishUpdateAccountButton;
    @FXML
    private Button backUserUpdateButton;
    @FXML
    private Label errorMessageUserUpdateLabel;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField newEmailTextField;
    @FXML
    private TextField newFirstNameTextField;
    @FXML
    private TextField newLastNameTextField;

    public void finishUpdateAccountButtonOnAction(){
        try {
            service.updateUser(user.getId(),newFirstNameTextField.getText(), newLastNameTextField.getText(), newEmailTextField.getText(), newPasswordTextField.getText());
        } catch (IllegalArgumentException | ValidationException exception) {
            errorMessageUserUpdateLabel.setText(exception.getMessage());
        }
        if (errorMessageUserUpdateLabel.getText().isBlank()) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
            Scene scene;
            try {
                Stage stage = (Stage) finishUpdateAccountButton.getScene().getWindow();
                stage.close();
                scene = new Scene(fxmlLoader.load(), 170, 145);
                stage.setTitle("Update Account");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateAccountBackButtonOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("UserSettings.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) backUserUpdateButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 170, 145);
            stage.setTitle("Update Account");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
