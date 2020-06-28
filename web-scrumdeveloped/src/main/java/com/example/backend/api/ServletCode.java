package com.example.backend.api;

import com.aliyuncs.exceptions.ClientException;
import com.example.backend.dao.CodeDAO;
import com.example.backend.entity.CodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ServletCode {
    @Autowired
    CodeDAO codeDAO;

    @PostMapping("/getCode")
    @ResponseBody
    public Map<String, Object> getCode(@RequestBody Map<String, String> data) throws ClientException {
        Map<String, Object> map = new HashMap<String, Object>();
        String status;
        String details;

        if(data.containsKey("user_phone")) {
            int code = ((int) ((Math.random() * 9 + 1) * 100000));
            List<CodeEntity> list = codeDAO.findByUserPhone(data.get("user_phone"));

            if(list.isEmpty()) {
                CodeEntity codeEntity = new CodeEntity();
                codeEntity.setCode(Integer.toString(code));
                codeEntity.setUserPhone(data.get("user_phone"));

                codeDAO.save(codeEntity);
            }
            //手机号已存在，更新验证码
            else
            {
                list.get(0).setCode(Integer.toString(code));

                codeDAO.save(list.get(0));
            }
            status="right";
            details="";
            Sms.sendSms(data.get("user_phone"),code);
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
