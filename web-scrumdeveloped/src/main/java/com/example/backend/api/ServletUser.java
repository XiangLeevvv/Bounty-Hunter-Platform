package com.example.backend.api;

import com.example.backend.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ServletUser {
    @Autowired
    UserDAO userDAO;
    @PostMapping("/avatar")
    @ResponseBody
    public Map<String,String> getdd()
    {
        Map<String, String> map = new HashMap<String, String>();

        map.put("status", "ok");
        return map;
    }
    @PostMapping("/getUserInfo")
    @ResponseBody
    public Map<String,Object> getUserAvatar(@RequestBody Map<String, String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("user_name"))
        {
            String avatar=userDAO.findByUserName(data.get("user_name")).get(0).getUserAvatar();
            int user_id=userDAO.findByUserName(data.get("user_name")).get(0).getUserId();
            status="right";
            details="";
            map.put("user_avatar",avatar);
            map.put("user_Id", user_id);
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
