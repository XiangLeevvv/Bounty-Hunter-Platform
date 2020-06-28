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
        UserEntity user=new UserEntity();
        user.setUserName("620");
        user.setUserPassword("620");
        userList.add(user);
        userDAO= mock(UserDAO.class);
        when(userDAO.findByUserName("620")).thenReturn(userList);
        servletLogin=new ServletLogin(userDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void doLogin() {
        Map<String, String> req = new HashMap<>();
        req.put("user_name","620");
        req.put("user_password","620");

        Map<String, Object> resp = servletLogin.doLogin(req);
        System.out.println(resp.get("details"));
        verify(userDAO, times(1)).findByUserName(eq("620"));
        Assertions.assertEquals("right",resp.get("status"));

    }

}
