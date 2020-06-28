package com.example.backend.dao;

import com.example.backend.entity.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CodeDAO extends JpaRepository<CodeEntity,Integer> {
    List<CodeEntity> findByUserPhone(String userPhone);
    CodeEntity save(CodeEntity codeEntity);
}
