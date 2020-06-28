package com.example.backend.api;

import com.example.backend.dao.*;
import com.example.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ServletTask {
    @Autowired
    TaskDraftInfoDAO taskDraftInfoDAO;
    @Autowired
    TaskPublishedInfoDAO taskPublishedInfoDAO;
    @Autowired
    TaskFinishedInfoDAO taskFinishedInfoDAO;
    @Autowired
    TaskOngoingInfoDAO taskOngoingInfoDAO;
    @Autowired
    TaskDAO taskDAO;
    @Autowired
    TaskPublishedDAO taskPublishedDAO;
    @Autowired
    TaskFinishedDAO taskFinishedDAO;
    @Autowired
    TaskOngoingDAO taskOngoingDAO;

    @PostMapping("/task/latest")
    @ResponseBody
    public Map<String, Object> getLatestTask() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("latest", taskPublishedInfoDAO.findLatest());
        return map;
    }

    @PostMapping("/task/publishedTask")
    @ResponseBody
    public Map<String, Object> getPubilshedTask(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userName;
        String status;
        String details;
        if(inMap.containsKey("user_name")){
            userName= inMap.get("user_name");
            status = "right";
            details = "";
            if (inMap.get("type").equals("published") ) {
                map.put("tasks", taskPublishedInfoDAO.findByTaskPunlisherNameOnOmit(userName));
            }
            else if (inMap.get("type").equals("ing")) {
                map.put("tasks",taskOngoingInfoDAO.findByTaskPunlisherNameOnOmit(userName));
            }
            else if (inMap.get("type").equals("ed")) {
                map.put("tasks",taskFinishedInfoDAO.findByTaskPunlisherNameOnOmit(userName));
            }
            else {
                status = "wrong";
                details = "";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/findTaskondetail")
    @ResponseBody
    public Map<String, Object> getdetailTask(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        String status;
        String details;
        int id;
        if(inMap.containsKey("task_id")){
            id= Integer.parseInt(inMap.get("task_id"));
            map.put("ed",taskFinishedInfoDAO.findById(id));
            map.put("ing",taskOngoingInfoDAO.findById(id));
            map.put("published",taskPublishedInfoDAO.findById(id));
            if(map.get("ed")==null&&map.get("ing")==null&&map.get("published")==null){
                status="wrong";
                details="没有找到任务";
            }
            else{
                status="right";
                details="";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/executeTask")
    @ResponseBody
    public Map<String, Object> getExecuteTaskOnOmit(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;
        String userName;
        if(inMap.containsKey("user_name")){
            userName= inMap.get("user_name");
            map.put("tasking",taskOngoingInfoDAO.findByTaskExecutorNameOnOmit(userName));
            if(map.get("tasking")==null){
                status="wrong";
                details="您没有领取的任务";
            }
            else{
                status="right";
                details="";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/publishTask")
    @ResponseBody
    public Map<String, Object> publishTask(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;

        String taskTitle;
        String taskInfo;
        Double taskBonus;
        String taskTypes;
        String endTime;
        String beginTime;
        String time1;
        String time2;
        Timestamp currentTime;
        if(inMap.containsKey("user_name")){
            taskTitle=inMap.get("task_title");
            taskInfo=inMap.get("task_info");
            taskBonus= Double.valueOf(inMap.get("task_bonus"));
            taskTypes=inMap.get("task_type");
            String aa = "\\[",bb = "]";
            String taskType = taskTypes.replaceAll(bb, "").replaceAll(aa, "").replaceAll(",", " ").replaceAll("\"", "");
            time1=inMap.get("begin_time");
            beginTime = time1.substring(1, 11) + " " + time1.substring(12, 20);
            time2 = inMap.get("end_time");
            endTime = time2.substring(1, 11) + " " + time2.substring(12, 20);
            System.out.println(endTime);
            TaskEntity taskEntity=new TaskEntity();
            taskEntity.setInfo(taskInfo);
            taskEntity.setTitle(taskTitle);
            taskEntity.setTags(taskType);
            taskEntity.setBonus(taskBonus);
            taskEntity.setEndTime(Timestamp.valueOf(endTime));
            taskEntity.setBeginTime(Timestamp.valueOf(beginTime));
            currentTime = new Timestamp(System.currentTimeMillis());
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(currentTime);
            taskEntity.setCreateTime(Timestamp.valueOf(time));
            taskDAO.save(taskEntity);
            status="right";
            details="";
            map.put("currentTimes", time);
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/publishTask1")
    @ResponseBody
    public Map<String, Object> publishTask1(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;
        String userName;
        Timestamp currentTime;

        if(inMap.containsKey("user_name")){
            userName= inMap.get("user_name");
            currentTime = Timestamp.valueOf(inMap.get("current_time"));
            TaskEntity taskEntity = taskDAO.findByCreateTime(currentTime).get(0);
            TaskPublishedEntity taskPublishedEntity=new TaskPublishedEntity();
            taskPublishedEntity.setPublisher(userName);
            taskPublishedEntity.setPublishtime(new Timestamp(System.currentTimeMillis()));
            taskPublishedEntity.setTaskId(taskEntity.getId());
            taskPublishedDAO.save(taskPublishedEntity);
            status="right";
            details="";
            map.put("data", taskEntity);
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/deleteTask")
    @ResponseBody
    public Map<String, Object> deleteTask(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;

        int taskId;
        if(inMap.containsKey("task_id")){
            taskId= Integer.parseInt(inMap.get("task_id"));
            TaskPublishedEntity taskPublishedEntity=taskPublishedDAO.findByTaskId(taskId);
            TaskFinishedEntity taskFinishedEntity=taskFinishedDAO.findByTaskId(taskId);
            if(taskFinishedEntity==null && taskPublishedEntity==null){
                status="wrong";
                details="没有找到目标任务";
            }
            else{
                if(taskFinishedEntity!=null)
                    taskFinishedDAO.delete(taskFinishedEntity);
                else
                    taskPublishedDAO.delete(taskPublishedEntity);
                TaskEntity taskEntity=taskDAO.findById(taskId).get(0);
                taskDAO.delete(taskEntity);
                status="right";
                details="";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/completeTask")
    @ResponseBody
    public Map<String, Object> completeTask(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;

        int taskId;
        if(inMap.containsKey("task_id")){
            taskId= Integer.parseInt(inMap.get("task_id"));
            TaskOngoingInfoEntity tasktarget=taskOngoingInfoDAO.findById(taskId).get(0);
            if(tasktarget==null){
                status="wrong";
                details="没有找到目标任务";
            }
            else{
                TaskFinishedEntity taskFinishedEntity=new TaskFinishedEntity();
                taskFinishedEntity.setTaskId(tasktarget.getId());
                taskFinishedEntity.setPublisher(tasktarget.getPublisher());
                taskFinishedEntity.setPublishtime(tasktarget.getPublishtime());
                taskFinishedEntity.setReceiver(tasktarget.getReceiver());
                taskFinishedEntity.setReceivetime(tasktarget.getReceivetime());
                taskFinishedEntity.setFinishedtime(new Timestamp(System.currentTimeMillis()));
                taskFinishedDAO.save(taskFinishedEntity);
                taskOngoingDAO.delete(taskOngoingDAO.findByTaskId(taskId));
                status="right";
                details="";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/receiveTask")
    @ResponseBody
    public Map<String, Object> receiveTask(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;
        Timestamp receivetime;

        int taskId;
        String userName;
        if(inMap.containsKey("task_id")){
            taskId= Integer.parseInt(inMap.get("task_id"));
            TaskPublishedInfoEntity tasktarget=taskPublishedInfoDAO.findById(taskId).get(0);
            if(tasktarget==null){
                status="wrong";
                details="没有找到目标任务";
            }
            else{
                userName=inMap.get("user_name");
                TaskOngoingEntity taskOngoingEntity=new TaskOngoingEntity();
                taskOngoingEntity.setTaskId(tasktarget.getId());
//                taskOngoingEntity.setInfo(tasktarget.getInfo());
//                taskOngoingEntity.setTitle(tasktarget.getTitle());
//                taskOngoingEntity.setTags(tasktarget.getTags());
//                taskOngoingEntity.setBonus(tasktarget.getBonus());
                taskOngoingEntity.setPublisher(tasktarget.getPublisher());
                taskOngoingEntity.setPublishtime(tasktarget.getPublishtime());
                taskOngoingEntity.setReceiver(userName);
                taskOngoingEntity.setReceivetime(new Timestamp(System.currentTimeMillis()));
//                taskOngoingEntity.setBeginTime(tasktarget.getBeginTime());
//                taskOngoingEntity.setEndTime(tasktarget.getEndTime());
                TaskPublishedEntity taskPublishedEntity=taskPublishedDAO.findByTaskId(taskId);
                taskPublishedDAO.delete(taskPublishedEntity);
                taskOngoingDAO.save(taskOngoingEntity);
                status="right";
                details="";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }

    @PostMapping("/task/findTaskByTags")
    @ResponseBody
    public Map<String, Object> getTaskByTags(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String Status;
        String details;

        String tags;
        if(inMap.containsKey("tags")){
            tags= inMap.get("tags");
            map.put("task_omitinfo",taskPublishedInfoDAO.findByTagsonomit(tags));
            System.out.println(map.get("task_omitinfo"));
            if(map.get("task_omitinfo")==null){
                Status="wrong";
                details="没有符合的任务";
            }
            else{
                Status="right";
                details="";
            }
        }
        else{
            Status="wrong";
            details="连接失败";
        }
        map.put("Status",Status);
        map.put("Details", details);
        return map;
    }

    @PostMapping("/task/search")
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, String> inMap) {
        Map<String, Object> map = new HashMap<String, Object>();

        String status;
        String details;

        String keyword;
        if(inMap.containsKey("keyword")){
            keyword= inMap.get("keyword");
            map.put("task_omitinfo",taskPublishedInfoDAO.Search(keyword));
            if(map.get("task_omitinfo")==null){
                status="wrong";
                details="没有符合的任务";
            }
            else{
                status="right";
                details="";
            }
        }
        else{
            status="wrong";
            details="连接失败";
        }
        map.put("status",status);
        map.put("details", details);
        return map;
    }
}
