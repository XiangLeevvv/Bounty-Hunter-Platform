package com.example.backend.dao;

import com.example.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserDAO extends JpaRepository<UserEntity,Integer> {
    UserEntity save(UserEntity user);
    UserEntity findByUserId(int id);
    List<UserEntity> findByUserName(String name);
    List<UserEntity> findByUserPhone(String phone);
}
