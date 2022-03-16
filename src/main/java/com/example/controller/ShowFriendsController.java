package com.example.controller;

import com.example.domain.*;
import com.example.domain.validators.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowFriendsController implements Initializable {
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
    private Button showFriendsDeleteButton;
    @FXML
    private Button showFriendsBackButton;
    @FXML
    private Label showFriendsErrorLabel;
    @FXML
    private Label showFriendsMessageLabel;
    @FXML
    private TableView<Friendship>showFriendsTableView;
    @FXML
    private TableColumn<Friendship,String>showFriendsEmailTableColumn;
    @FXML
    private TableColumn<Friendship,String>showFriendsEmailTableColumn2;
    @FXML
    private TableColumn<Friendship,String>showFriendsFriendshipDateTableColumn;
    @FXML
    private TextField showFriendsSendMessageTextField;
    @FXML
    private TextField showFriendsSearchTextField;
    private final ObservableList<Friendship> friendships = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        friendships.addAll(friendshipsUser());
        showFriendsEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email_id2"));
        showFriendsFriendshipDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("friendshipdate"));
        showFriendsEmailTableColumn2.setCellValueFactory(new PropertyValueFactory<>("email_id1"));
        showFriendsTableView.setItems(friendships);
        FilteredList<Friendship> friendshipFilteredList = new FilteredList<>(friendships, b -> true);
        showFriendsSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> friendshipFilteredList.setPredicate(friendship -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            if (friendship.getEmail_id2().toLowerCase().contains(searchkeyword)) {
                return true;
            }
             else return friendship.getFriendshipdate().toString().toLowerCase().contains(searchkeyword);
        }));
        SortedList<Friendship> sortedList = new SortedList<>(friendshipFilteredList);
        sortedList.comparatorProperty().bind(showFriendsTableView.comparatorProperty());
        showFriendsTableView.setItems(sortedList);
    }

    public void showFriendsDeleteButtonOnAction(){
        try {
            showFriendsErrorLabel.setText("");
            showFriendsMessageLabel.setText("");
            service.deleteFriendship(tableViewSelectedUser().getId());
            showFriendsMessageLabel.setText("You deleted a friend!");
        }catch (IllegalArgumentException | ValidationException exception){
            showFriendsErrorLabel.setText(String.valueOf(exception));
        }
    }

    public void showFriendsBackButtonOnAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) showFriendsBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 600, 546);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Friendship> friendshipsUser() {
        ArrayList<Friendship> friendships = new ArrayList<>();
        for (Friendship friendship : service.printAllFriendships()) {
            if (friendship.getId1().equals(user.getId()) || friendship.getId2().equals(user.getId())) {
                friendships.add(friendship);
            }
        }
        return friendships;
    }

    private Friendship tableViewSelectedUser() {
        Friendship selected = showFriendsTableView.getSelectionModel().getSelectedItem();
        return friendshipRepository.findOne(selected.getId());
    }

    public void showFriendsSendMessageOnAction(){
        try {
            showFriendsMessageLabel.setText("");
            showFriendsErrorLabel.setText("");
            service.sendMessage(user.getId(), tableViewSelectedUser().getId1(), showFriendsSendMessageTextField.getText(), tableViewSelectedUser().getEmail_id2(), tableViewSelectedUser().getEmail_id1());
            showFriendsMessageLabel.setText("You send a message!");
        }catch (IllegalArgumentException | ValidationException exception) {
            showFriendsErrorLabel.setText(String.valueOf(exception));

            if (showFriendsErrorLabel.getText().equals(String.valueOf(exception))) {
                try {
                    showFriendsMessageLabel.setText("");
                    showFriendsErrorLabel.setText("");
                    service.sendMessage(user.getId(), tableViewSelectedUser().getId2(), showFriendsSendMessageTextField.getText(), tableViewSelectedUser().getEmail_id2(), tableViewSelectedUser().getEmail_id1());
                    showFriendsMessageLabel.setText("You send a message!");
                } catch (IllegalArgumentException | ValidationException exception1) {
                    showFriendsErrorLabel.setText(String.valueOf(exception1));
                }
            }
        }
    }
}
