package com.example.domain;

import java.io.Serial;
import java.io.Serializable;

public class Entity<ID> implements Serializable {
    protected ID id;

    public ID getId() {
        return this.id;
    }

    public void setID(ID id) {
        this.id = id;
    }


}
