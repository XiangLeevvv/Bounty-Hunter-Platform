package com.example.backend.dao;

import com.example.backend.entity.TaskEntity;
import com.example.backend.entity.TaskOngoingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskOngoingInfoDAO extends JpaRepository<TaskOngoingInfoEntity,Integer> {
    @Query(value = "select * " +
            "from taskOngoingInfo as t where t.publisher=?1",nativeQuery = true)
    List<TaskOngoingInfoEntity> findByTaskPunlisherNameOnOmit(String taskPN);

    @Query(value = "select * " +
            "from taskOngoingInfo as t where t.receiver=?1",nativeQuery = true)
    List<TaskOngoingInfoEntity> findByTaskExecutorNameOnOmit(String taskEN);

    List<TaskOngoingInfoEntity> findById(int id);
}
