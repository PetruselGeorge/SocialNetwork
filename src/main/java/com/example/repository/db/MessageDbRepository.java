package com.example.repository.db;

import com.example.domain.Message;
import com.example.domain.validators.Validator;
import com.example.repository.Repository;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageDbRepository implements Repository<Long, Message> {
    private final Connection connection;
    private final Validator<Message> validator;
    private final static String TO_EMAIL="to_email";
    private final static String FROM_EMAIL="from_email";
    public MessageDbRepository(Connection connection, Validator<Message> validator) {
        this.connection = connection;
        this.validator = validator;
    }

    @Override
    public Message findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("ID must be not null");
        String sql = "select * from messages where id = ? ";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();

            Long from = resultSet.getLong("from_user");
            Long to = resultSet.getLong("to_user");
            String message = resultSet.getString("message");
            String date1 = String.valueOf(resultSet.getDate("message_date"));
            LocalDate date = LocalDate.parse(date1);
            Long reply = resultSet.getLong("reply");
            String to_email = resultSet.getString(TO_EMAIL);
            String from_email = resultSet.getString(FROM_EMAIL);

            Message message1= new Message(from, to, message,date,reply,to_email,from_email);
            message1.setID(id);
            return message1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("from_user");
                Long to = resultSet.getLong("to_user");
                String message = resultSet.getString("message");
                String date1 = String.valueOf(resultSet.getDate("message_date"));
                LocalDate date = LocalDate.parse(date1);
                Long reply = resultSet.getLong("reply");
                String to_email = resultSet.getString(TO_EMAIL);
                String from_email = resultSet.getString(FROM_EMAIL);
                Message message1 = new Message(from, to,message, date, reply,to_email,from_email);
                message1.setID(id);
                messages.add(message1);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        String sql = "insert into messages ( from_user,to_user, message,message_date, reply,to_email,from_email ) values (?, ?, ?, ?, ?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, entity.getFrom());
            ps.setLong(2, entity.getTo());
            ps.setString(3,entity.getMessage());
            ps.setDate(4, Date.valueOf(String.valueOf(entity.getDate())));
            ps.setLong(5, entity.getReply());
            ps.setString(6,entity.getTo_email());
            ps.setString(7, entity.getFrom_email());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    }

    @Override
    public Message delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        String sql = "delete from messages where id = ?";
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
    public void update(Message entity) {

    }

    @Override
    public List<Message> getFriends(Message user) {
        return null;
    }

    @Override
    public Message getByEmail(String s) {
        return null;
    }
}
