package com.example.repository.db;

import com.example.domain.FriendRequest;
import com.example.validators.Validator;
import com.example.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDbRepository implements Repository<Long, FriendRequest> {

    private final Connection connection;
    private final Validator<FriendRequest> validator;
    private final static String ID1 = "id1";
    private final static String ID2 = "id2";
    private final static String STATUS = "status";
    private final static String EMAILID2="email_id2";
    private final static String EMAILID1="email_id1";
    private final static String FRIENDREQUESTDATE = "friend_request_date";

    public FriendRequestDbRepository(Connection connection, Validator<FriendRequest> validator) {
        this.connection = connection;
        this.validator = validator;
    }

    @Override
    public FriendRequest findOne(Long id) {
        boolean ok = false;
        if (id == null) {
            throw new IllegalArgumentException("The id can't be null!");
        }
        for (FriendRequest friendRequest : findAll()) {
            if (friendRequest.getId().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("The friend request doesn't exist!");
        }

        String sql = "select * from friend_requests where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Long id1 = rs.getLong(ID1);
            Long id2 = rs.getLong(ID2);
            String status = rs.getString(STATUS);
            String email_id2 = rs.getString(EMAILID2);
            String email_id1 = rs.getString(EMAILID1);
            LocalDate friend_request_date = LocalDate.parse(String.valueOf(rs.getDate(FRIENDREQUESTDATE)));
            FriendRequest friendRequest = new FriendRequest(id1, id2, status, friend_request_date,email_id2,email_id1);
            friendRequest.setID(id);
            return friendRequest;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FriendRequest> findAll() {
        List<FriendRequest> friendRequests = new ArrayList<>();
        String sql = "select * from friend_requests";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long id1 = rs.getLong(ID1);
                Long id2 = rs.getLong(ID2);
                String status = rs.getString(STATUS);
                String email_id2 = rs.getString(EMAILID2);
                String email_id1 = rs.getString(EMAILID1);
                LocalDate friend_request_date = LocalDate.parse(String.valueOf(rs.getDate(FRIENDREQUESTDATE)));
                FriendRequest friendRequest = new FriendRequest(id1, id2, status, friend_request_date,email_id2,email_id1);
                friendRequest.setID(id);
                friendRequests.add(friendRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public FriendRequest save(FriendRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity can't be null!");
        }
        validator.validate(entity);
        String sql = "insert into friend_requests(id1,id2,status,friend_request_date,email_id2,email_id1) values (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, entity.getId1());
            ps.setLong(2, entity.getId2());
            ps.setString(3, entity.getStatus());
            ps.setDate(4, Date.valueOf(entity.getFriendRequestDate()));
            ps.setString(5,entity.getEmailId2());
            ps.setString(6,entity.getEmail_id1());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null!");
        }
        boolean ok = false;
        for (FriendRequest friendRequest : findAll()) {
            if (friendRequest.getId().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("The friend request doesn't exist!");
        }
        String sql = "delete from friend_requests where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(FriendRequest entity) {
        boolean ok = false;
        if (entity == null) {
            throw new IllegalArgumentException("The friendship can't be null!");
        }
        for (FriendRequest friendRequest : findAll()) {
            if (friendRequest.equals(entity)) {
                ok = true;
                break;
            }
        }
        validator.validate(entity);
        if (!ok) {
            throw new IllegalArgumentException("The friend request doesn't exist!");
        }
        String sql = "update friend_requests set id1=?,id2=?,status=?,friend_request_date=?,email_id2=?,email_id1=?, where id=? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, entity.getId1());
            ps.setLong(2, entity.getId2());
            ps.setString(3, entity.getStatus());
            ps.setDate(4, Date.valueOf(entity.getFriendRequestDate()));
            ps.setString(5,entity.getEmailId2());
            ps.setString(6,entity.getEmail_id1());
            ps.setLong(7, entity.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<FriendRequest> getFriends(FriendRequest friendRequest) {
        return null;
    }

    @Override
    public FriendRequest getByEmail(String s) {
        return null;
    }
}
