
package com.example.backend.api;

import com.example.backend.dao.UserDAO;
import com.example.backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类演示了文档注释。
 * @author Ayan Amhed
 * @version 1.2
 */
@RestController
public class ServletLogin {
    @Autowired
    UserDAO userDAO;
    /**
     * 这个类演示了文档注释
     * @author Ayan Amhed
     * @version 1.2
     */
    public ServletLogin(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> doLogin(@RequestBody Map<String, String> data) {
        Map<String, Object> map = new HashMap<>();
        String status = "";
        String details = "";

        if (data.containsKey("user_name")) {
            List<UserEntity> list = userDAO.findByUserName(data.get("user_name"));
            if (list.isEmpty()) {
                status = "wrong";
                details = "用户不存在";
            } else if (!list.get(0).getUserPassword().equals(data.get("user_password"))) {
                status = "wrong";
                details = "密码错误";
            }
            //用户名与密码正确
            else {
                status = "right";
                String userAvatar = list.get(0).getUserAvatar();
                int userId = list.get(0).getUserId();

                map.put("user_avatar", userAvatar);
                map.put("user_id", userId);
            }
        } else {
            status = "wrong";
            details = "连接失败";
        }

        map.put("status", status);
        map.put("details", details);

        return map;
    }
}

