package com.example.backend.dao;

import com.example.backend.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryDAO extends JpaRepository<HistoryEntity,Integer> {
    HistoryEntity save(HistoryEntity historyEntity);
}
