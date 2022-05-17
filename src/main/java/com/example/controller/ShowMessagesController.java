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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowMessagesController implements Initializable {
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
    private TextField showMessagesSearchTextField;
    @FXML
    private TableView<Message> showMessagesTableView;
    @FXML
    private Button showMessagesBackButton;
    @FXML
    private TableColumn<Message, String> showMessageEmailFrom;
    @FXML
    private TableColumn<Message, String> showMessagesMessage;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messages.addAll(messagesUser());
        showMessageEmailFrom.setCellValueFactory(new PropertyValueFactory<>("from_email"));
        showMessagesMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        showMessagesTableView.setItems(messages);
        FilteredList<Message> messageFilteredList = new FilteredList<>(messages, b -> true);
        showMessagesSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> messageFilteredList.setPredicate(message -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            if (message.getFrom_email().toLowerCase().contains(searchkeyword)) {
                return true;
            } else return message.getMessage().toString().toLowerCase().contains(searchkeyword);
        }));
        SortedList<Message> sortedList = new SortedList<>(messageFilteredList);
        sortedList.comparatorProperty().bind(showMessagesTableView.comparatorProperty());
        showMessagesTableView.setItems(sortedList);

    }

    public List<Message> messagesUser() {
        ArrayList<Message> messages = new ArrayList<>();
        for (Message message : service.printAllMessages()) {
            if (message.getTo().equals(user.getId())) {
                messages.add(message);
            }
        }
        return messages;
    }

    public void showMessagesBackButtonOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) showMessagesBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 432, 546);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
