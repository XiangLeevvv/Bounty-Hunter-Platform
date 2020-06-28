package com.example.backend.api;

import com.example.backend.dao.TaskDAO;
import com.example.backend.dao.TaskDraftDAO;
import com.example.backend.dao.TaskDraftInfoDAO;
import com.example.backend.entity.TaskDraftEntity;
import com.example.backend.entity.TaskDraftInfoEntity;
import com.example.backend.entity.TaskEntity;
import com.example.backend.entity.TaskPublishedEntity;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ServletTaskDraft {
    @Autowired
    TaskDAO taskDAO;
    @Autowired
    TaskDraftDAO taskDraftDAO;
    @Autowired
    TaskDraftInfoDAO taskDraftInfoDAO;

    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/getDraftinfo")
    @ResponseBody
    public Map<String,Object> getDraftinfo(@RequestBody Map<String, String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("task_id"))
        {
            TaskDraftInfoEntity taskDraftInfo = taskDraftInfoDAO.findById(Integer.parseInt(data.get("task_id")));
            if(taskDraftInfo != null) {
                status = "right";
                details = "";
            }
            else {
                status = "wrong";
                details="草稿箱为空";
            }
            map.put("List",taskDraftInfo);
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

    @PostMapping("/getUserDrafts")
    @ResponseBody
    public Map<String,Object> getUserDrafts(@RequestBody Map<String, String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("user_name"))
        {
            List<TaskDraftInfoEntity> taskDraftInfoList=taskDraftInfoDAO.findByCreator(data.get("user_name"));
            if(taskDraftInfoList.size()>0) {
                status = "right";
                details = "";
            }
            else {
                status = "wrong";
                details="草稿箱为空";
            }
            map.put("List",taskDraftInfoList);
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

    @PostMapping("/modifyDrafts")
    @ResponseBody
    public Map<String,Object> modifyDrafts(@RequestBody Map<String, String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("task_id")) {
            TaskEntity taskEntity = taskDAO.findById(Integer.parseInt(data.get("task_id"))).get(0);
            taskEntity.setInfo(data.get("task_info"));
            String time1=data.get("begin_time");
            String beginTime = time1.substring(1, 11) + " " + time1.substring(12, 20);
            String time2=data.get("end_time");
            String endTime = time2.substring(1, 11) + " " + time2.substring(12, 20);
            taskEntity.setBeginTime(Timestamp.valueOf(beginTime));
            taskEntity.setEndTime(Timestamp.valueOf(endTime));
            taskEntity.setBonus(Double.parseDouble(data.get("task_bonus")));
            taskEntity.setTags(data.get("task_type"));
            taskEntity.setTitle(data.get("task_title"));
            taskDAO.save(taskEntity);
            status="right";
            details="编辑成功";
        } else {
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details",details);
        return map;
    }

    @PostMapping("/createDrafts")
    @ResponseBody
    public Map<String,Object> createDrafts(@RequestBody Map<String, String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("current_time"))
        {
            Timestamp currentTime = Timestamp.valueOf(data.get("current_time"));
            TaskEntity taskEntity = taskDAO.findByCreateTime(currentTime).get(0);
            TaskDraftEntity taskDraft = new TaskDraftEntity();
            taskDraft.setTaskId(taskEntity.getId());
            taskDraft.setCreator(data.get("user_name"));
            taskDraftDAO.save(taskDraft);
            status="right";
            details="保存成功";
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

    @PostMapping("/deleteDrafts")
    @ResponseBody
    public Map<String,Object> deleteDrafts(@RequestBody Map<String, String> data)
    {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if(data.containsKey("task_id"))
        {

            taskDraftDAO.deleteById(Integer.parseInt(data.get("task_id")));
            status="right";
            details="删除成功";
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


  /*  @PostMapping("/getTime")
    @ResponseBody
    public Map<String,Object> getTime()
    {
        String status="right";
        String details="";
        Map<String, Object> map = new HashMap<String, Object>();

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(currentTime);
        map.put("current_time",time);
        map.put("status",status);
        map.put("details",details);
        return map;
    }*/
}
