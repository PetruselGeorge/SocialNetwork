package com.example.repository.db;


import com.example.domain.Friendship;
import com.example.domain.User;
import com.example.validators.Validator;
import com.example.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsersDbRepository implements Repository<Long, User> {

    private final Connection connection;
    private final Validator<User> validator;
    private final FriendshipDbRepository friendshipDbRepository;
    private final static String FIRST_NAME = "first_name";
    private final static String LAST_NAME = "last_name";
    private final static String ID = "id";
    private final static String EMAIL="email";
    private final static String PASSWORD="password";

    public UsersDbRepository(Connection connection, Validator<User> validator, FriendshipDbRepository friendshipDbRepository) {
        this.connection = connection;
        this.validator = validator;
        this.friendshipDbRepository = friendshipDbRepository;
    }

    @Override
    public User findOne(Long id) {
        boolean ok = false;
        if (id == null) {
            throw new IllegalArgumentException("The id can't be null!");
        }
        for (User user : findAll()) {
            if (user.getId().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("The users doesn't exist!");
        }

        String sql = "select * from users where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String first_name = rs.getString(FIRST_NAME);
            String last_name = rs.getString(LAST_NAME);
            String email=rs.getString(EMAIL);
            String password=rs.getString(PASSWORD);
            User user= new User(first_name, last_name,email,password);
            user.setID(id);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(ID);
                String first_name = rs.getString(FIRST_NAME);
                String last_name = rs.getString(LAST_NAME);
                String email=rs.getString(EMAIL);
                String password=rs.getString(PASSWORD);
                User user = new User(first_name, last_name,email,password);
                user.setID(id);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can't be null!");
        }
        validator.validate(entity);
        String sql = "insert into users (first_name,last_name,email,password) values(?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3,entity.getEmail());
            ps.setString(4,entity.getPassword());
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The entity id is incorrect or it doesn't exist!");
        }
        boolean ok = false;
        for (User user : findAll()) {
            if (user.getId().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("The users doesn't exist!");
        }

        String sql = "delete from users where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (Friendship friendship : friendshipDbRepository.findAll()) {
            if (friendship.getId1().equals(id) || friendship.getId2().equals(id)) {
                friendshipDbRepository.delete(friendship.getId());
            }
        }
        return null;
    }

    @Override
    public void update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The entity doesn't exist");
        }
        validator.validate(entity);
        String sql = "update users set first_name=?, last_name=?, email=?,password=? where id=?";
        {
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, entity.getFirstName());
                ps.setString(2, entity.getLastName());
                ps.setString(3,entity.getEmail());
                ps.setString(4,entity.getPassword());
                ps.setLong(5,entity.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public List<User> getFriends(User user) {
        List<Friendship> friendships = friendshipDbRepository.findAll();
        List<User> users = new ArrayList<>();
        for (Friendship friendship : friendships) {
            if (friendship.getId1().equals(user.getId())) {
                User user1=findOne(friendship.getId2());
                user1.setID(friendship.getId2());
                users.add(user1);
            }
            if (friendship.getId2().equals(user.getId())){
                User user1=findOne(friendship.getId1());
                user1.setID(friendship.getId2());
                users.add(user1);
            }
        }
        return users;
    }
    @Override
    public User getByEmail(String email) {
        String sql = "select * from users where email = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String passwordHash = resultSet.getString("password");
            User user = new User(firstName, lastName, email, passwordHash);
            user.setID(id);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}