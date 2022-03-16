package com.example.controller;

import com.example.domain.*;
import com.example.domain.validators.FriendRequestValidator;
import com.example.domain.validators.FriendshipValidator;
import com.example.domain.validators.MessageValidator;
import com.example.domain.validators.UserValidator;
import com.example.georgel.HelloApplication;
import com.example.repository.Repository;
import com.example.repository.db.FriendRequestDbRepository;
import com.example.repository.db.FriendshipDbRepository;
import com.example.repository.db.MessageDbRepository;
import com.example.repository.db.UsersDbRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.service.Service;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private final FriendshipDbRepository friendshipRepository1 = new FriendshipDbRepository(connection, new FriendshipValidator());

    private final Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository(connection, new FriendshipValidator());
    private final Repository<Long, User> userRepository = new UsersDbRepository(connection, new UserValidator(), friendshipRepository1);
    private final Repository<Long, Message> messageRepository = new MessageDbRepository(connection, new MessageValidator());
    private final Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDbRepository(connection, new FriendRequestValidator());
    private final Service service=new Service(userRepository,friendshipRepository,messageRepository,friendRequestRepository);

    @FXML
    private Button cancelButton;
    @FXML
    public Button registerButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Button LoginButton;
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("Images/logo.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

    }

    public void cancelButtonOnAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction(){
        if (!usernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()){
            validateLogin();
        }
        else{
            loginMessageLabel.setText("Enter an email or password");
        }
    }

    public void validateLogin(){
        boolean ok=false;
        for (User user:service.printAll()){
            if (user.getEmail().equals(usernameTextField.getText()) && user.getPassword().equals(enterPasswordField.getText())){
                loginMessageLabel.setText("Congratulations!");
                ok=true;
                User user2 = userRepository.findOne(user.getId());
                LoggedUser.setUser(user2);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
                Scene scene;
                try {
                    Stage stage = (Stage) LoginButton.getScene().getWindow();
                    stage.close();
                    scene = new Scene(fxmlLoader.load(), 600, 546);
                    stage.setTitle("Your Account");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!ok){
            loginMessageLabel.setText("Invalid login!");
        }
    }
    public void registerButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 717, 653);
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

