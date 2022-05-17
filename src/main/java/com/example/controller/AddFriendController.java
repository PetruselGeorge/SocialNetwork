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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class AddFriendController implements Initializable {
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
    private final User user = LoggedUser.user;
    @FXML
    private TextField addFriendsSearchTextField;
    @FXML
    private TableView<User> addFriendTableView;
    @FXML
    private TableColumn<User, String> addFriendsFirstNameTableColumn;
    @FXML
    private TableColumn<User, String> addFriendsLastNameTableColumn;
    @FXML
    private TableColumn<User, String> addFriendsEmailTableColumn;
    @FXML
    private Label addfriendMessageLabel;
    @FXML
    private Button addFriendBackButton;
    private final ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users.addAll(appUsers());
        addFriendsFirstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addFriendsLastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addFriendsEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addFriendTableView.setItems(users);
        FilteredList<User> userFilteredList = new FilteredList<>(users, b -> true);
        addFriendsSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> userFilteredList.setPredicate(user -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            if (user.getFirstName().toLowerCase().contains(searchkeyword)) {
                return true;

            } else if (user.getLastName().toLowerCase().contains(searchkeyword)) {
                return true;
            } else return user.getEmail().toLowerCase().contains(searchkeyword);
        }));
        SortedList<User> sortedList = new SortedList<>(userFilteredList);
        sortedList.comparatorProperty().bind(addFriendTableView.comparatorProperty());
        addFriendTableView.setItems(sortedList);
    }

    public List<User> appUsers() {
        ArrayList<User> appUsers = new ArrayList<>();
        for (User user1 : service.printAll()) {
            if (!Objects.equals(user1.getId(), user.getId())) {
                appUsers.add(user1);
            }
        }
        return appUsers;
    }

    private User tableViewSelectedUser() {
        User selected = addFriendTableView.getSelectionModel().getSelectedItem();
        return userRepository.getByEmail(selected.getEmail());
    }

    public void sendFriendRequestButtonOnAction() {
        try {addfriendMessageLabel.setText("");
            sendFriendRequestValidator();
            service.sendFriendRequest(user.getId(), tableViewSelectedUser().getId(), tableViewSelectedUser().getEmail(),user.getEmail());
        } catch (IllegalArgumentException | ValidationException exception) {
            addfriendMessageLabel.setText(exception.getMessage());
        }
    }

    public void sendFriendRequestValidator(){
        for (FriendRequest friendRequest: service.printAllFriendRequests()){
            if (friendRequest.getId2().equals(tableViewSelectedUser().getId()) && friendRequest.getId1().equals(user.getId())){
                throw new IllegalArgumentException("The request was already send!");
            }
        }
    }

    public void addFriendBackButtonOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) addFriendBackButton.getScene().getWindow();
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