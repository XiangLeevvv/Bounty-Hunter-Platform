package com.example.backend.api;

import com.example.backend.dao.TaskDAO;
import com.example.backend.dao.TaskDraftDAO;
import com.example.backend.dao.TaskDraftInfoDAO;
import com.example.backend.entity.TaskDraftEntity;
import com.example.backend.entity.TaskDraftInfoEntity;
import com.example.backend.entity.TaskEntity;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类用于草稿相关操作.
 * @author ghy
 * @version 1.0
 */
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

    /**
     * 用于单元测试初始化TaskDraft类.
     * @param taskDAO,taskDraftDAO,taskDraftInfoDAO 接收任务相关DAO类
     */
    public ServletTaskDraft(TaskDAO taskDAO, TaskDraftDAO taskDraftDAO, TaskDraftInfoDAO taskDraftInfoDAO) {
        this.taskDAO = taskDAO;
        this.taskDraftDAO = taskDraftDAO;
        this.taskDraftInfoDAO = taskDraftInfoDAO;
    }

    /**
     * 获取草稿信息.
     * @param data 接收task id.
     * @return 状态status及报错细节details.
     */
    @PostMapping("/getDraftinfo")
    @ResponseBody
    public Map<String, Object> getDraftinfo(@RequestBody Map<String, String> data) {
        String status="wrong";
        String details="连接失败";
        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("task_id")) {
            TaskDraftInfoEntity taskDraftInfo = taskDraftInfoDAO.findById(Integer.parseInt(data.get("task_id")));
            if (taskDraftInfo != null) {
                status = "right";
                details = "";
            } else {
                status = "wrong";
                details = "未查询到任务";
            }
            map.put("List", taskDraftInfo);
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    /**
     * 获取用户草稿箱.
     * @param data 接收用户名.
     * @return 状态status及报错细节details.
     */
    @PostMapping("/getUserDrafts")
    @ResponseBody
    public Map<String, Object> getUserDrafts(@RequestBody Map<String, String> data) {
        String status="wrong";
        String details="连接失败";
        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("user_name")) {
            List<TaskDraftInfoEntity> taskDraftInfoList = taskDraftInfoDAO.findByCreator(data.get("user_name"));
            if (taskDraftInfoList.size() > 0) {
                status = "right";
                details = "";
            } else {
                status = "wrong";
                details = "草稿箱为空";
            }
            map.put("List", taskDraftInfoList);
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    /**
     * 修改草稿.
     * @param data 接受task信息.
     * @return 状态status及报错细节details.
     */
    @PostMapping("/modifyDrafts")
    @ResponseBody
    public Map<String, Object> modifyDrafts(@RequestBody Map<String, String> data) {
        String status="wrong";
        String details="连接失败";
        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("task_id")) {
            List<TaskEntity> taskEntityList = taskDAO.findById(Integer.parseInt(data.get("task_id")));
            if (taskEntityList.size() > 0) {
                if (Double.parseDouble(data.get("task_bonus")) > 0) {
                    TaskEntity taskEntity = taskEntityList.get(0);
                    taskEntity.setInfo(data.get("task_info"));
                    String time1 = data.get("begin_time");
                    String beginTime = time1.substring(1, 11) + " " + time1.substring(12, 20);
                    String time2 = data.get("end_time");
                    String endTime = time2.substring(1, 11) + " " + time2.substring(12, 20);
                    taskEntity.setBeginTime(Timestamp.valueOf(beginTime));
                    taskEntity.setEndTime(Timestamp.valueOf(endTime));
                    taskEntity.setBonus(Double.parseDouble(data.get("task_bonus")));
                    taskEntity.setTags(data.get("task_type"));
                    taskEntity.setTitle(data.get("task_title"));
                    taskDAO.save(taskEntity);
                    status = "right";
                    details = "";
                } else {
                    status = "wrong";
                    details = "酬金不能为负";
                }
            } else {
                status = "wrong";
                details = "任务ID不存在";
            }
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    /**
     * 创建草稿.
     * @param data 接收草稿信息.
     * @return 状态status及报错细节details.
     */
    @PostMapping("/createDrafts")
    @ResponseBody
    public Map<String, Object> createDrafts(@RequestBody Map<String, String> data) {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("current_time") && data.get("current_time").length() > 5) {
            Timestamp currentTime = Timestamp.valueOf(data.get("current_time"));
            TaskEntity taskEntity = taskDAO.findByCreateTime(currentTime).get(0);
            TaskDraftEntity taskDraft = new TaskDraftEntity();
            taskDraft.setTaskId(taskEntity.getId());
            taskDraft.setCreator(data.get("user_name"));
            taskDraftDAO.save(taskDraft);
            if (data.get("user_name") == "") {
                status = "wrong";
                details = "用户名不存在";
            } else {
                status = "right";
                details = "";
            }
        } else {
            status = "wrong";
            details = "时间格式不合法";
        }
        map.put("status", status);
        map.put("details", details);
        return map;
    }

    /**
     * 删除草稿.
     * @param data 接收task id.
     * @return 状态status及报错细节details.
     */
    @PostMapping("/deleteDrafts")
    @ResponseBody
    public Map<String, Object> deleteDrafts(@RequestBody Map<String, String> data) {
        String status;
        String details;
        Map<String, Object> map = new HashMap<String, Object>();

        if (data.containsKey("task_id") && !data.get("task_id").equals("")) {

            taskDraftDAO.deleteById(Integer.parseInt(data.get("task_id")));
            status = "right";
            details = "";
        } else {
            status = "wrong";
            details = "任务ID不存在";
        }
        map.put("status", status);
        map.put("details", details);
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

