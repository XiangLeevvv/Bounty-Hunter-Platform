package com.example.backend.api;

import com.aliyuncs.exceptions.ClientException;
import com.example.backend.dao.CodeDAO;
import com.example.backend.entity.CodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类用于注册验证码相关操作.
 * @author ghy
 * @version 1.0
 */
@RestController
public class ServletCode {
    @Autowired
    CodeDAO codeDAO;

    /**
     * 用于单元测试初始化Code类.
     * @param codeDAO 接收userDAO.
     */
    public ServletCode(CodeDAO codeDAO){
        this.codeDAO=codeDAO;
    }

    /**
     * 获取验证码.
     * @param data 接收用户手机号.
     * @return 状态status及报错细节details.
     */
    @PostMapping("/getCode")
    @ResponseBody
    public Map<String, Object> getCode(@RequestBody Map<String, String> data) throws ClientException {
        Map<String, Object> map = new HashMap<String, Object>();
        String status;
        String details;

        if(data.containsKey("user_phone")&&data.get("user_phone").length()==11) {

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
            details="请输入正确的手机号！";
        }

        map.put("status",status);
        map.put("details",details);
        return map;
    }
}
