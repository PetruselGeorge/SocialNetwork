package com.example.repository;

import com.example.domain.Entity;


import java.util.List;

public interface Repository<Long, E extends Entity<Long>> {
    E findOne(Long id);

    List<E> findAll();

    E save(E entity);

    E delete(Long id);

    void update(E entity);

    List<E> getFriends(E user);

    E getByEmail(String s);
}
