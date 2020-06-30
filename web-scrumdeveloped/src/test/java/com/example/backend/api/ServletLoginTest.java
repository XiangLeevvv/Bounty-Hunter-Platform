package com.example.backend.api;

import com.example.backend.dao.UserDAO;
import com.example.backend.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServletLoginTest {


    private UserDAO userDAO;
    private ServletLogin servletLogin;

    @BeforeEach
    void setUp() {
        //打桩
        List<UserEntity> userList= new ArrayList<UserEntity>();
        List<UserEntity> emptyList= new ArrayList<UserEntity>();
        UserEntity user=new UserEntity();
        user.setUserName("622");
        user.setUserPassword("622");
        userList.add(user);
        userDAO= mock(UserDAO.class);
        when(userDAO.findByUserName("622")).thenReturn(userList);
        when(userDAO.findByUserName("")).thenReturn(emptyList);
        when(userDAO.findByUserName("asd")).thenReturn(emptyList);
        servletLogin=new ServletLogin(userDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void doLogin() {
        //UT_TC_001_001_001
        Map<String, String> UT_TC_001_001_001_001 = new HashMap<>();
        UT_TC_001_001_001_001.put("user_name","");
        UT_TC_001_001_001_001.put("user_password","622");

        Map<String, Object> resp1 = servletLogin.doLogin(UT_TC_001_001_001_001);
        System.out.println("UT_TC_001_001_001_001:"+resp1.get("details"));
        verify(userDAO, times(1)).findByUserName(eq(""));
        Assertions.assertEquals("wrong",resp1.get("status"));

        Map<String, String> UT_TC_001_001_001_002 = new HashMap<>();
        UT_TC_001_001_001_002.put("user_name","622");
        UT_TC_001_001_001_002.put("user_password","");

        Map<String, Object> resp2 = servletLogin.doLogin(UT_TC_001_001_001_002);
        System.out.println("UT_TC_001_001_001_002:"+resp2.get("details"));
        verify(userDAO, times(1)).findByUserName(eq("622"));
        Assertions.assertEquals("wrong",resp2.get("status"));

        //UT_TC_001_001_002
        Map<String, String> UT_TC_001_001_002 = new HashMap<>();
        UT_TC_001_001_002.put("user_name","asd");
        UT_TC_001_001_002.put("user_password","622");

        Map<String, Object> resp3 = servletLogin.doLogin(UT_TC_001_001_002);
        System.out.println("UT_TC_001_001_002:"+resp3.get("details"));
        verify(userDAO, times(1)).findByUserName(eq("asd"));
        Assertions.assertEquals("wrong",resp3.get("status"));

        //UT_TC_001_001_003
        Map<String, String> UT_TC_001_001_003 = new HashMap<>();
        UT_TC_001_001_003.put("user_name","622");
        UT_TC_001_001_003.put("user_password","000");

        Map<String, Object> resp4 = servletLogin.doLogin(UT_TC_001_001_003);
        System.out.println("UT_TC_001_001_003:"+resp4.get("details"));
        Assertions.assertEquals("wrong",resp4.get("status"));

        //UT_TC_001_001_004
        Map<String, String> UT_TC_001_001_004 = new HashMap<>();
        UT_TC_001_001_004.put("user_name","622");
        UT_TC_001_001_004.put("user_password","622");

        Map<String, Object> resp5 = servletLogin.doLogin(UT_TC_001_001_004);
        System.out.println("UT_TC_001_001_003:"+resp5.get("details"));
        Assertions.assertEquals("right",resp5.get("status"));
    }

}
