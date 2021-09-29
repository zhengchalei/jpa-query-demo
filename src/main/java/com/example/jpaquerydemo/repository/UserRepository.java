package com.example.jpaquerydemo.repository;

import com.example.jpaquerydemo.entity.User;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

public interface UserRepository extends JRepository<User, String> {

    @EntityGraph(attributePaths = {"role", "dept"})
    @Override
    Page<User> findAll(Predicate predicate, Pageable pageable);

}