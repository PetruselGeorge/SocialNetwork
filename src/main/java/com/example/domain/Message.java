package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Message extends Entity<Long> {
    private Long from;
    private Long to;
    private String message;
    private LocalDate date;
    public Long reply;
    private String to_email;
    private String from_email;

    public Message(Long from, Long to, String message, LocalDate date, Long reply,String to_email,String from_email) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = reply;
        this.to_email=to_email;
        this.from_email=from_email;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long aLong) {
        this.from = aLong;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long aLong1) {
        this.to = aLong1;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public String getTo_email(){return this.to_email;}

    public void setTo_email(String to_email){this.to_email=to_email;}

    public String getFrom_email() {
        return this.from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }


    public int compare(Message message) {
        if (this.getDate() == null || message.getDate() == null) {
            return 0;
        }
        return this.getDate().compareTo(message.getDate());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(message, message1.message) && Objects.equals(date, message1.date) && Objects.equals(reply, message1.reply) && Objects.equals(to_email, message1.to_email) && Objects.equals(from_email, message1.from_email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message, date, reply, to_email, from_email);
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", reply=" + reply +
                ", to_email='" + to_email + '\'' +
                ", from_email='" + from_email + '\'' +
                '}';
    }
}



