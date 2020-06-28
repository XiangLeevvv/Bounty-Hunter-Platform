package com.example.backend.api;

import com.example.backend.dao.CodeDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.entity.CodeEntity;
import com.example.backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;

@RestController
@RequestMapping("/api")
public class ServletRegister {
    @Autowired
    UserDAO userDAO;
    @Autowired
    CodeDAO codeDAO;

    @PostMapping("/register")
    @ResponseBody
    public Map<String,Object> register(@RequestBody Map<String, String> data)
    {
        String status="";
        String details="";
        String phone;
        String name;
        Map<String, Object> map = new HashMap<String, Object>();
        if(data.containsKey("user_name")&&data.containsKey("user_phone")) {
            phone = data.get("user_phone");
            name = data.get("user_name");
            List<UserEntity> ulist1 = userDAO.findByUserName(name);
            List<UserEntity> ulist2 = userDAO.findByUserPhone(phone);
            if(!ulist1.isEmpty())
            {
                status="wrong";
                details="用户名已存在";
            }
            else if(!ulist2.isEmpty())
            {
                status="wrong";
                details="手机号已注册";
            }
            //用户名和手机号都未被占用
            else {
                List<CodeEntity> code = codeDAO.findByUserPhone(phone);
                if(code.get(0).getCode().equals(data.get("user_code"))){
                    UserEntity user=new UserEntity();
                    user.setUserName(name);
                    user.setUserPassword(data.get("user_password"));
                    user.setUserSex(data.get("user_gender"));
                    user.setUserPhone(data.get("user_phone"));
                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String time = sdf.format(currentTime);
                    user.setUserAddtime(Date.valueOf(time));
                    userDAO.save(user);
                    status="right";
                    details="注册成功";
                }
            }
        }
        else
        {
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details",details);
        return map;
    }
}

