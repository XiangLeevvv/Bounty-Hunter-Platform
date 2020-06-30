package com.example.backend.api;

import com.example.backend.dao.TaskDAO;
import com.example.backend.dao.TaskDraftDAO;
import com.example.backend.dao.TaskDraftInfoDAO;
import com.example.backend.dao.UserDAO;
import com.example.backend.entity.TaskDraftInfoEntity;
import com.example.backend.entity.TaskEntity;
import com.example.backend.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class ServletTaskDraftTest {

    private TaskDAO taskDAO;
    private TaskDraftDAO taskDraftDAO;
    private TaskDraftInfoDAO taskDraftInfoDAO;
    private ServletTaskDraft servletTaskDraft;

    @BeforeEach
    void setUp() {
        //打桩
        List<TaskDraftInfoEntity> emptyTaskDraftInfoList=new ArrayList<>();
        List<TaskEntity> emptyTaskList=new ArrayList<>();
        List<TaskDraftInfoEntity> tList= new ArrayList<>();
        List<TaskEntity> taskList=new ArrayList<>();

        TaskDraftInfoEntity taskDraftInfoEntity=new TaskDraftInfoEntity();
        taskDraftInfoEntity.setId(100);
        taskDraftInfoEntity.setBonus(10.);
        taskDraftInfoEntity.setCreator("622");
        taskDraftInfoEntity.setBeginTime(Timestamp.valueOf("2020-06-22 00:00:00"));
        taskDraftInfoEntity.setEndTime(Timestamp.valueOf("2020-06-22 12:00:00"));
        taskDraftInfoEntity.setInfo("无");
        taskDraftInfoEntity.setTags("无");
        taskDraftInfoEntity.setTitle("无");
        tList.add(taskDraftInfoEntity);

        TaskEntity taskEntity=new TaskEntity();
        taskEntity.setId(100);
        taskEntity.setBonus(10.);
        taskEntity.setBeginTime(Timestamp.valueOf("2020-06-22 00:00:00"));
        taskEntity.setEndTime(Timestamp.valueOf("2020-06-22 12:00:00"));
        taskEntity.setInfo("无");
        taskEntity.setTags("无");
        taskEntity.setTitle("无");
        taskList.add(taskEntity);

        taskDAO= mock(TaskDAO.class);
        taskDraftDAO= mock(TaskDraftDAO.class);
        taskDraftInfoDAO= mock(TaskDraftInfoDAO.class);

        when(taskDraftInfoDAO.findById(0)).thenReturn(null);
        when(taskDraftInfoDAO.findById(100)).thenReturn(taskDraftInfoEntity);
        when(taskDraftInfoDAO.findByCreator("")).thenReturn(emptyTaskDraftInfoList);
        when(taskDraftInfoDAO.findByCreator("qwe")).thenReturn(emptyTaskDraftInfoList);
        when(taskDraftInfoDAO.findByCreator("622")).thenReturn(tList);

        when(taskDAO.findById(0)).thenReturn(emptyTaskList);
        when(taskDAO.findById(100)).thenReturn(taskList);

        when(taskDAO.findByCreateTime(Timestamp.valueOf("2020-06-22 15:41:05"))).thenReturn(taskList);

        servletTaskDraft=new ServletTaskDraft(taskDAO,taskDraftDAO,taskDraftInfoDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDraftinfo() {
        //UT_TC_002_001_001
        Map<String, String> UT_TC_002_001_001 = new HashMap<>();
        UT_TC_002_001_001.put("task_id","0");

        Map<String, Object> resp1 = servletTaskDraft.getDraftinfo(UT_TC_002_001_001);
        System.out.println("UT_TC_002_001_001:"+resp1.get("details"));
        verify(taskDraftInfoDAO, times(1)).findById(eq(0));
        Assertions.assertEquals("wrong",resp1.get("status"));

        //UT_TC_002_001_002
        Map<String, String> UT_TC_002_001_002 = new HashMap<>();
        UT_TC_002_001_002.put("task_id","100");

        Map<String, Object> resp2 = servletTaskDraft.getDraftinfo(UT_TC_002_001_002);
        System.out.println("UT_TC_002_001_002:"+resp2.get("details"));
        verify(taskDraftInfoDAO, times(1)).findById(eq(100));
        Assertions.assertEquals("right",resp2.get("status"));
    }

    @Test
    void getUserDrafts() {
        //UT_TC_002_002_001
        Map<String, String> UT_TC_002_002_001 = new HashMap<>();
        UT_TC_002_002_001.put("user_name","");

        Map<String, Object> resp1 = servletTaskDraft.getUserDrafts(UT_TC_002_002_001);
        System.out.println("UT_TC_002_002_001:"+resp1.get("details"));
        verify(taskDraftInfoDAO, times(1)).findByCreator(eq(""));
        Assertions.assertEquals("wrong",resp1.get("status"));

        //UT_TC_002_002_002
        Map<String, String> UT_TC_002_002_002 = new HashMap<>();
        UT_TC_002_002_002.put("user_name","qwe");

        Map<String, Object> resp2 = servletTaskDraft.getUserDrafts(UT_TC_002_002_002);
        System.out.println("UT_TC_002_002_002:"+resp2.get("details"));
        verify(taskDraftInfoDAO, times(1)).findByCreator(eq("qwe"));
        Assertions.assertEquals("wrong",resp2.get("status"));

        //UT_TC_002_002_003
        Map<String, String> UT_TC_002_002_003 = new HashMap<>();
        UT_TC_002_002_003.put("user_name","622");

        Map<String, Object> resp3 = servletTaskDraft.getUserDrafts(UT_TC_002_002_003);
        System.out.println("UT_TC_002_002_003:"+resp3.get("details"));
        verify(taskDraftInfoDAO, times(1)).findByCreator(eq("622"));
        Assertions.assertEquals("right",resp3.get("status"));
    }

    @Test
    void modifyDrafts() {
        HashMap<String, String> UT_TC_002_003 = new HashMap<>();
        UT_TC_002_003.put("task_id","100");
        UT_TC_002_003.put("task_info","无");
        UT_TC_002_003.put("task_title","啊");
        UT_TC_002_003.put("task_bonus","10");
        UT_TC_002_003.put("task_type","跑腿");
        UT_TC_002_003.put("begin_time",'"'+"2020-06-22 15:41:05"+'"');
        UT_TC_002_003.put("end_time",'"'+"2020-06-22 23:41:05"+'"');
        String tip="任务描述不能为空";
        //UT_TC_002_003_001
        HashMap<String, String> UT_TC_002_003_001=new HashMap<>();
        UT_TC_002_003_001.putAll(UT_TC_002_003);
        UT_TC_002_003_001.replace("task_id","0");

        Map<String, Object> resp1 = servletTaskDraft.modifyDrafts(UT_TC_002_003_001);
        System.out.println("UT_TC_002_003_001:"+resp1.get("details"));
        Assertions.assertEquals("wrong",resp1.get("status"));

        //UT_TC_002_003_002
        HashMap<String, String> UT_TC_002_003_002=new HashMap<>();
        UT_TC_002_003_002.putAll(UT_TC_002_003);
        UT_TC_002_003_002.replace("task_bonus","-1");
        Map<String, Object> resp2 = servletTaskDraft.modifyDrafts(UT_TC_002_003_002);
        System.out.println("UT_TC_002_003_002:"+resp2.get("details"));

        Assertions.assertEquals("wrong",resp2.get("status"));

        //UT_TC_002_003_003
        HashMap<String, String> UT_TC_002_003_003_001=new HashMap<>();
        UT_TC_002_003_003_001.putAll(UT_TC_002_003);
        UT_TC_002_003_003_001.replace("task_info","");

        Map<String, Object> resp3 = servletTaskDraft.modifyDrafts(UT_TC_002_003_003_001);
        System.out.println("UT_TC_002_003_003:"+tip);
        Assertions.assertEquals("right",resp3.get("status"));

        //UT_TC_002_003_004
        Map<String, Object> resp4 = servletTaskDraft.modifyDrafts(UT_TC_002_003);
        System.out.println("UT_TC_002_003_004:"+resp4.get("details"));
        verify(taskDAO, times(3)).findById(eq(100));
        Assertions.assertEquals("right",resp4.get("status"));
    }

    @Test
    void createDrafts() {
        //UT_TC_002_004_001
        Map<String, String> UT_TC_002_004_001 = new HashMap<>();
        UT_TC_002_004_001.put("current_time","622");
        UT_TC_002_004_001.put("user_name","622");

        Map<String, Object> resp1 = servletTaskDraft.createDrafts(UT_TC_002_004_001);
        System.out.println("UT_TC_002_004_001:"+resp1.get("details"));
        Assertions.assertEquals("wrong",resp1.get("status"));

        //UT_TC_002_004_002
        Map<String, String> UT_TC_002_004_002 = new HashMap<>();
        UT_TC_002_004_002.put("current_time","2020-06-22 15:41:05");
        UT_TC_002_004_002.put("user_name","");

        Map<String, Object> resp2 = servletTaskDraft.createDrafts(UT_TC_002_004_002);
        System.out.println("UT_TC_002_004_002:"+resp2.get("details"));
        Assertions.assertEquals("wrong",resp2.get("status"));

        //UT_TC_002_004_003
        Map<String, String> UT_TC_002_004_003 = new HashMap<>();
        UT_TC_002_004_003.put("current_time","2020-06-22 15:41:05");
        UT_TC_002_004_003.put("user_name","622");

        Map<String, Object> resp3 = servletTaskDraft.createDrafts(UT_TC_002_004_003);
        System.out.println("UT_TC_002_004_003:"+resp3.get("details"));
        Assertions.assertEquals("right",resp3.get("status"));
    }

    @Test
    void deleteDrafts() {
        //UT_TC_002_005_001
        Map<String, String> UT_TC_002_005_001 = new HashMap<>();
        UT_TC_002_005_001.put("task_id","");

        Map<String, Object> resp1 = servletTaskDraft.deleteDrafts(UT_TC_002_005_001);
        System.out.println("UT_TC_002_005_001:"+resp1.get("details"));

        Assertions.assertEquals("wrong",resp1.get("status"));

        //UT_TC_002_001_001
        Map<String, String> UT_TC_002_005_002 = new HashMap<>();
        UT_TC_002_005_002.put("task_id","100");

        Map<String, Object> resp2 = servletTaskDraft.getDraftinfo(UT_TC_002_005_002);
        System.out.println("UT_TC_002_005_002:"+resp2.get("details"));
        Assertions.assertEquals("right",resp2.get("status"));
    }
}
