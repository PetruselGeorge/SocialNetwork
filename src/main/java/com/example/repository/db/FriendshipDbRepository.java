package com.example.repository.db;


import com.example.domain.Friendship;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private final Connection connection;
    private final Validator<Friendship> validator;
    private final static String ID1 = "id1";
    private final static String ID2 = "id2";
    private final static String EMAILID2="email_id2";
    private final static String EMAILID1="email_id1";
    private final static String FRIENDSHIPDATE = "friendshipdate";


    public FriendshipDbRepository(Connection connection, Validator<Friendship> validator) {
        this.connection = connection;
        this.validator = validator;
    }


    @Override
    public Friendship findOne(Long id) {
        boolean ok = false;
        if (id == null) {
            throw new IllegalArgumentException("The id can't be null!");
        }
        for (Friendship friendship : findAll()) {
            if (friendship.getId().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("The friendship doesn't exist!");
        }

        String sql = "select * from friendships where id=?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            Long id1 = rs.getLong(ID1);
            Long id2 = rs.getLong(ID2);
            String email_id2 = rs.getString(EMAILID2);
            String email_id1 = rs.getString(EMAILID1);
            LocalDate friendshipdate = LocalDate.parse(String.valueOf(rs.getDate(FRIENDSHIPDATE)));
            Friendship friendship= new Friendship(id1, id2, friendshipdate,email_id2,email_id1);
            friendship.setID(id);
            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "select * from friendships";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                Long id1 = rs.getLong(ID1);
                Long id2 = rs.getLong(ID2);
                String email_id2 = rs.getString(EMAILID2);
                String email_id1 = rs.getString(EMAILID1);
                LocalDate friendshipdate = LocalDate.parse(String.valueOf(rs.getDate(FRIENDSHIPDATE)));
                Friendship friendship = new Friendship(id1, id2, friendshipdate,email_id2,email_id1);
                friendship.setID(id);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null!");
        }

        for (Friendship friendship : findAll()) {
            if (friendship.equals(entity) || (friendship.getId1().equals(entity.getId2()) && friendship.getId2().equals(entity.getId1()))) {
                throw new IllegalArgumentException("You can't creat a friendship with yourself!");
            }
        }
        validator.validate(entity);
        String sql = "insert into friendships(id1,id2,friendshipdate,email_id2,email_id1) values (?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, entity.getId1());
            ps.setLong(2, entity.getId2());
            ps.setDate(3, Date.valueOf(entity.getFriendshipdate()));
            ps.setString(4,entity.getEmail_id2());
            ps.setString(5,entity.getEmail_id1());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can't be null!");
        }
        boolean ok = false;
        for (Friendship friendship : findAll()) {
            if (friendship.getId().equals(id)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("The friendship doesn't exist!");
        }

        String sql = "delete from friendships where id=?";
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
    public void update(Friendship entity) {
        boolean ok=false;
        if (entity==null){
            throw new IllegalArgumentException("The friendship can't be null!");
        }

        for (Friendship friendship:findAll()){
            if (friendship.equals(entity)) {
                ok = true;
                break;
            }
        }

        if (!ok){
            throw new IllegalArgumentException("The friendship doesn't exist!");
        }
        validator.validate(entity);
        String sql="update friendships set id1=?,id2=?,friendshipdate=?,email_id2=?,email_id1=?, where id=? ";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setLong(1,entity.getId1());
            ps.setLong(2,entity.getId2());
            ps.setDate(3, Date.valueOf(entity.getFriendshipdate()));
            ps.setString(4,entity.getEmail_id2());
            ps.setString(5,entity.getEmail_id1());
            ps.setLong(6,entity.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Friendship> getFriends(Friendship friendship) {
        return null;
    }

    @Override
    public Friendship getByEmail(String s) {
        return null;
    }
}
