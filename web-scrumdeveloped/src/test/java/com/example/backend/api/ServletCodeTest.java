package com.example.backend.api;

import com.aliyuncs.exceptions.ClientException;
import com.example.backend.dao.CodeDAO;
import com.example.backend.entity.CodeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ServletCodeTest {

    private CodeDAO codeDAO;
    private ServletCode servletCode;

    @BeforeEach
    void setUp() {
        List<CodeEntity> cList=new ArrayList<>();
        List<CodeEntity> emptyList=new ArrayList<>();
        CodeEntity codeEntity=new CodeEntity();
        codeEntity.setCode("123456");
        codeEntity.setUserPhone("18721923502");
        cList.add(codeEntity);

        codeDAO=mock(CodeDAO.class);
        servletCode=new ServletCode(codeDAO);
        when(codeDAO.findByUserPhone("18721000001")).thenReturn(emptyList);
        when(codeDAO.findByUserPhone("18721000002")).thenReturn(cList);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCode() throws ClientException {
        //UT_TC_003_001_001
        Map<String, String> UT_TC_003_001_001 = new HashMap<>();
        UT_TC_003_001_001.put("user_phone","20200628");

        Map<String, Object> resp1 = servletCode.getCode(UT_TC_003_001_001);
        System.out.println("UT_TC_003_001_001:"+resp1.get("details"));
        Assertions.assertEquals("wrong",resp1.get("status"));

        //UT_TC_003_001_002
        Map<String, String> UT_TC_003_001_002 = new HashMap<>();
        UT_TC_003_001_002.put("user_phone","18721000001");

        Map<String, Object> resp2 = servletCode.getCode(UT_TC_003_001_002);
        System.out.println("UT_TC_003_001_002:"+resp2.get("details"));
        Assertions.assertEquals("right",resp2.get("status"));

        //UT_TC_003_001_003
        Map<String, String> UT_TC_003_001_003 = new HashMap<>();
        UT_TC_003_001_003.put("user_phone","18721000002");

        Map<String, Object> resp3 = servletCode.getCode(UT_TC_003_001_003);
        System.out.println("UT_TC_003_001_003:"+resp3.get("details"));
        Assertions.assertEquals("right",resp3.get("status"));
    }
}
