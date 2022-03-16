package com.example.controller;

import com.example.georgel.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML
    private Button userSettingsButton;

    @FXML
    private Button accountControllerLogoutButton;

    @FXML
    private Button accountControllerExitButton;

    @FXML
    private ImageView logo2ImageView;

    @FXML
    private Button accountControllerAddFriend;
    @FXML
    private Button accountControllerFriendRequest;
    @FXML
    private Button showFriendsAccountController;
    @FXML
    private Button showMessagesccountController;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("Images/logo2.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        logo2ImageView.setImage(brandingImage);
    }

    public void userSettingsButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("UserSettings.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) userSettingsButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 170, 145);
            stage.setTitle("User Settings");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountControllerLogoutButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) accountControllerLogoutButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 513, 386);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountControllerExitButtonOnAction() {
        Stage stage = (Stage) accountControllerExitButton.getScene().getWindow();
        stage.close();
    }

    public void accountControllerAddFriendOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add_friend.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) accountControllerAddFriend.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 783, 386);
            stage.setTitle("Add Friend");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accountControllerFriendRequestOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friend_request.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) accountControllerFriendRequest.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 588, 489);
            stage.setTitle("Friend Requests");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFriendsAccountControllerOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("show_friends.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) showFriendsAccountController.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 665, 489);
            stage.setTitle("Friend Requests");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessagesAccountControllerOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("show_messages.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) showMessagesccountController.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 666, 470);
            stage.setTitle("Messages");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
