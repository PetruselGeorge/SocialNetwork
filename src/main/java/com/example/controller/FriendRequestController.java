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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FriendRequestController implements Initializable {
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
    private TableView<FriendRequest> friendRequestTableView;
    @FXML
    private TableColumn<FriendRequest, String> friendRequestEmailTableColumn;
    @FXML
    private TableColumn<FriendRequest, String> friendRequestStatusTableColumn;
    @FXML
    private TableColumn<FriendRequest, String> friendrequestDateTableColumn;
    @FXML
    private Button friendRequestAcceptButton;
    @FXML
    private Button friendRequestDeclineButton;
    @FXML
    private Button friendRequestBackButton;
    @FXML
    private Label friendRequestMessageLabel;
    @FXML
    private Label FriendRequestAcceptLabel;
    @FXML
    private TextField friendRequestSearchTextField;
    private final ObservableList<FriendRequest> friendRequests = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        friendRequests.addAll(friendRequestsUser());
        friendRequestEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email_id1"));
        friendRequestStatusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        friendrequestDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("friendRequestDate"));
        friendRequestTableView.setItems(friendRequests);
        FilteredList<FriendRequest> friendRequestFilteredList = new FilteredList<>(friendRequests, b -> true);
        friendRequestSearchTextField.textProperty().addListener((observable, oldValue, newValue) -> friendRequestFilteredList.setPredicate(friendRequest -> {
            if (newValue.isBlank() || newValue.isBlank()) {
                return true;
            }
            String searchkeyword = newValue.toLowerCase();
            User userFriendRequest = service.findOne(friendRequest.getId2());
            if (userFriendRequest.getEmail().toLowerCase().contains(searchkeyword)) {
                return true;

            } else if (friendRequest.getStatus().toLowerCase().contains(searchkeyword)) {
                return true;
            } else return friendRequest.getFriendRequestDate().toString().toLowerCase().contains(searchkeyword);
        }));
        SortedList<FriendRequest> sortedList = new SortedList<>(friendRequestFilteredList);
        sortedList.comparatorProperty().bind(friendRequestTableView.comparatorProperty());
        friendRequestTableView.setItems(sortedList);
    }

    public void friendRequestAcceptButtonOnAction() {
        try {friendRequestMessageLabel.setText("");
        service.updateFriendRequest(tableViewSelectedUser().getId(), Status.APPROVED);
        FriendRequestAcceptLabel.setText("You Accepted the friend request");
        }catch (IllegalArgumentException | ValidationException exception){
            friendRequestMessageLabel.setText(String.valueOf(exception));
        }
    }

    public void friendRequestDeclineButtonOnAction() {
        try {friendRequestMessageLabel.setText("");
            FriendRequestAcceptLabel.setText("");
            service.updateFriendRequest(tableViewSelectedUser().getId(), Status.REJECTED);
            FriendRequestAcceptLabel.setText("You rejected the friend request");
        }catch (IllegalArgumentException | ValidationException exception){
            friendRequestMessageLabel.setText(String.valueOf(exception));
        }
    }

    public void friendRequestBackButtonOnAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account_controller.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) friendRequestBackButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 432, 546);
            stage.setTitle("Social Network");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FriendRequest> friendRequestsUser() {
        ArrayList<FriendRequest> friendRequests = new ArrayList<>();
        for (FriendRequest friendRequest : service.printAllFriendRequests()) {
            if (friendRequest.getId2().equals(user.getId())) {
                friendRequests.add(friendRequest);
            }
        }
        return friendRequests;
    }

    private FriendRequest tableViewSelectedUser() {
        FriendRequest selected = friendRequestTableView.getSelectionModel().getSelectedItem();
        return friendRequestRepository.findOne(selected.getId());
    }
}
