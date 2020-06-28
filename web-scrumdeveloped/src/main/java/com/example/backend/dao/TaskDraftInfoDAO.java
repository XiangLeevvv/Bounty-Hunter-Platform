package com.example.backend.dao;

import com.example.backend.entity.TaskDraftInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDraftInfoDAO extends JpaRepository<TaskDraftInfoEntity,Integer> {
    List<TaskDraftInfoEntity> findByCreator(String name);
    TaskDraftInfoEntity findById(int id);
    TaskDraftInfoEntity save(TaskDraftInfoEntity taskDraftInfoEntity);
}
