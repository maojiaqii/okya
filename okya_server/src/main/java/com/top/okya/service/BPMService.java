package com.top.okya.service;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2021/5/12 17:46
 * @describe： 流程引擎专用业务类
 */

public interface BPMService {

    String deploy(String xmlName, String pngName, String flowName) throws FileNotFoundException;

    /**
     * @param deploymentId 对应ACT_RE_DEPLOYMENT表的ID字段
     */
    void removeByDeploymentId(String deploymentId);

    /**
     * @param processKey 对应ACT_RE_PROCDEF表的KEY字段
     * @param var
     */
    String startProcessByKey(String processKey, Map<String, Object> var);

    /**
     * @param assignee 对应ACT_RE_DEPLOYMENT表的ID字段
     */
    void findTask(String assignee);

    /**
     * @param processId 对应ACT_RU_TASK表的PROC_INST_ID字段
     * @param userCode  对应ACT_RU_TASK表的ASSIGNEE字段
     */
    void completeTask(String userCode, String processId, Map<String, Object> var);

    /**
     * @param processId 对应ACT_RU_TASK表的PROC_INST_ID字段
     * @param userCode  对应ACT_RU_TASK表的ASSIGNEE字段
     */
    void rejectTask(String userCode, String processId);
}
