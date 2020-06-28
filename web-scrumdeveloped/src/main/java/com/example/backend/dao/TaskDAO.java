package com.example.backend.dao;

import com.example.backend.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
@Repository
public interface TaskDAO extends JpaRepository<TaskEntity,Integer> {

    List<TaskEntity> findById(int taskId);
    List<TaskEntity> findByCreateTime(Timestamp time);
}
