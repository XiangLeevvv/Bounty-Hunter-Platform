package com.example.backend.api;

import com.example.backend.dao.HistoryDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.entity.HistoryEntity;
import com.example.backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
public class ServletHistory {
    @Autowired
    HistoryDAO historyDAO;
    @Autowired
    UserDAO userDAO;

    @PostMapping("/addHistory")
    @ResponseBody
    public Map<String,Object> addHistory(@RequestBody Map<String,String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("user_name")) {
       // List<UserEntity> userList=userDAO.findByUserName(data.get("user_name"));
        HistoryEntity historyEntity=new HistoryEntity();
        historyEntity.setTaskId(Integer.parseInt(data.get("task_id")));
        historyEntity.setUserName(data.get("user_name"));
        historyEntity.setTime(new Timestamp(System.currentTimeMillis()));
        historyDAO.save(historyEntity);
        status="right";
        details="";
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
