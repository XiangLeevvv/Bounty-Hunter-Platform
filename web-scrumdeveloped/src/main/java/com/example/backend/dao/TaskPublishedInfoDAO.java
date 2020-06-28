package com.example.backend.dao;

import com.example.backend.entity.TaskEntity;
import com.example.backend.entity.TaskPublishedEntity;
import com.example.backend.entity.TaskPublishedInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskPublishedInfoDAO extends JpaRepository<TaskPublishedInfoEntity,Integer> {
    @Query(value = "select *" +
            " from taskPublishedInfo where publisher=?1",nativeQuery = true)
    List<TaskPublishedInfoEntity> findByTaskPunlisherNameOnOmit(String taskPN);

    @Query(value = "select *" +
            "from taskPublishedInfo as t where t.tags like %:tag%",nativeQuery = true)
    List<TaskPublishedInfoEntity> findByTagsonomit(@Param("tag") String tag);

    @Query(value = "select * " +
            " from taskPublishedInfo as t where t.tags like %?1% or t.title like %?1% or t.info like %?1%",nativeQuery = true)
    List<TaskPublishedInfoEntity> Search(String keyword);

    List<TaskPublishedInfoEntity> findById(int id);

    @Query(value = "select * from taskPublishedInfo ORDER BY publishtime DESC limit 8" ,nativeQuery = true)
    List<TaskPublishedInfoEntity> findLatest();
}
