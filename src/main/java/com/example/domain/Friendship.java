package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private final Long id1;
    private final Long id2;
    private final String email_id2;
    private final String email_id1;
    private LocalDate friendshipdate;

    public Friendship(Long id1, Long id2, LocalDate friendshipdate, String email_id2,String email_id1) {
        this.id1 = id1;
        this.id2 = id2;
        this.friendshipdate = friendshipdate;
        this.email_id2 = email_id2;
        this.email_id1=email_id1;
    }

    public Long getId1() {
        return this.id1;
    }

    public Long getId2() {
        return this.id2;
    }

    public LocalDate getFriendshipdate() {
        return this.friendshipdate;
    }

    public String getEmail_id2() {
        return this.email_id2;
    }
    public String getEmail_id1() {
        return this.email_id1;
    }

    public void setFriendshipdate(LocalDate friendshipdate) {
        this.friendshipdate = friendshipdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(email_id2, that.email_id2) && Objects.equals(friendshipdate, that.friendshipdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, email_id2, friendshipdate);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", email_id2='" + email_id2 + '\'' +
                ", friendshipdate=" + friendshipdate +
                '}';
    }

}