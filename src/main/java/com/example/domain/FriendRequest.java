package com.example.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendRequest extends Entity<Long> {

    private Long id1;
    private Long id2;
    private String status;
    private String email_id2;
    private String email_id1;
    private LocalDate friendRequestDate;

    public FriendRequest(Long id1, Long id2, String status, LocalDate friendRequestDate, String email_id2,String email_id1) {
        this.id1 = id1;
        this.id2 = id2;
        this.status = status;
        this.friendRequestDate = friendRequestDate;
        this.email_id2 = email_id2;
        this.email_id1=email_id1;
    }

    public Long getId1() {
        return this.id1;
    }

    public Long getId2() {
        return this.id2;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailId2() {
        return this.email_id2;
    }

    public void setEmailId2(String email_id2) {
        this.email_id2 = email_id2;
    }

    public String getEmail_id1(){
        return this.email_id1;
    }

    public void setEmail_id1(String email_id1){
        this.email_id1=email_id1;
    }

    public LocalDate getFriendRequestDate() {
        return this.friendRequestDate;
    }

    public void setFriendRequestDate(LocalDate friendRequestDate) {
        this.friendRequestDate = friendRequestDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(id1, that.id1) && Objects.equals(id2, that.id2) && Objects.equals(status, that.status) && Objects.equals(email_id2, that.email_id2) && Objects.equals(email_id1, that.email_id1) && Objects.equals(friendRequestDate, that.friendRequestDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2, status, email_id2, email_id1, friendRequestDate);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id1=" + id1 +
                ", id2=" + id2 +
                ", status='" + status + '\'' +
                ", email_id2='" + email_id2 + '\'' +
                ", email_id1='" + email_id1 + '\'' +
                ", friendRequestDate=" + friendRequestDate +
                '}';
    }
}

