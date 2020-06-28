package com.example.backend.dao;
import com.example.backend.entity.TaskOngoingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskOngoingDAO extends JpaRepository<TaskOngoingEntity,Integer> {
    TaskOngoingEntity findByTaskId(int id);
}
