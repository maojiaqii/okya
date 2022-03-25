package com.top.okya.controller;

import com.top.okya.service.BPMService;
import com.top.okya.util.response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2021/5/12 16:56
 * @describe：
 */

@Slf4j
@Controller
@RequestMapping("/flow")
public class TestController {

    @Autowired
    private BPMService bpmService;

    @RequestMapping(value = "/initDeploy", method = RequestMethod.GET)
    @ResponseBody
    public String initDeploy() {
        String xmlName = "process/ops-coffee.bpmn";
        String pngName = "process/ops-coffee.png";
        String flowName = "前台流程";
        String deploymentId = null;
        try {
            deploymentId = bpmService.deploy(xmlName, pngName, flowName);
        } catch (FileNotFoundException e) {
            log.info("流程配置文件未找到，部署流程失败!");
            return new ResponseObject(false, "流程配置文件未找到，部署流程失败", null).invoke();
        }
        log.info("流程部署成功，流程ID:" + deploymentId);
        return new ResponseObject(true, "流程部署成功，流程ID:" + deploymentId, null).invoke();
    }

    @RequestMapping(value = "/deploy", method = RequestMethod.GET)
    @ResponseBody
    public String deploy() {
        String xmlName = "process/hqcs.bpmn";
        String pngName = "process/hqcs.png";
        String flowName = "会签流程测试";
        String deploymentId = null;
        try {
            deploymentId = bpmService.deploy(xmlName, pngName, flowName);
        } catch (FileNotFoundException e) {
            log.info("流程配置文件未找到，部署流程失败!");
            return new ResponseObject(false, "流程配置文件未找到，部署流程失败", null).invoke();
        }
        log.info("流程部署成功，流程ID:" + deploymentId);
        return new ResponseObject(true, "流程部署成功，流程ID:" + deploymentId, null).invoke();
    }

    @RequestMapping(value = "/removeByDeploymentId", method = RequestMethod.GET)
    @ResponseBody
    public String removeByDeploymentId() {
        String deploymentId = "2501";
        bpmService.removeByDeploymentId(deploymentId);
        return new ResponseObject(true, "流程删除成功，流程ID:" + deploymentId, null).invoke();
    }

    @RequestMapping(value = "/startProcessByKey", method = RequestMethod.GET)
    @ResponseBody
    public String startProcessByKey(String processKey) {
        //String processKey = "test3";
        Map<String, Object> var = new HashMap<String, Object>();
        var.put("userCode", "user1");
        var.put("date", 10);
        String processId = bpmService.startProcessByKey(processKey, var);
        return new ResponseObject(true, "流程启动成功，流程实例ID:" + processId, null).invoke();
    }

    @RequestMapping(value = "/findTask", method = RequestMethod.GET)
    @ResponseBody
    public String findTask(String assignee) {
        //String assignee = "user1";
        bpmService.findTask(assignee);
        return new ResponseObject(true, "查询任务成功！", null).invoke();
    }

    @RequestMapping(value = "/completeTask", method = RequestMethod.GET)
    @ResponseBody
    public String completeTask(String processId, String userCode) {
        Map<String, Object> var = new HashMap<String, Object>();
        List userCodes = new ArrayList<String>();
        userCodes.add("user4_2");
        userCodes.add("user5_2");
        var.put("userCode",  "user6");
        //var.put("userList",  userCodes);
        bpmService.completeTask(userCode, processId, var);
        return new ResponseObject(true, "完成任务成功！", null).invoke();
    }

    @RequestMapping(value = "/rejectTask", method = RequestMethod.GET)
    @ResponseBody
    public String rejectTask(String processId, String userCode) {
        //String taskId  = "27508";
        bpmService.rejectTask(userCode, processId);
        return new ResponseObject(true, "驳回任务成功！", null).invoke();
    }

}
