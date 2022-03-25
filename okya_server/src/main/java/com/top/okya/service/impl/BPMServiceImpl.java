package com.top.okya.service.impl;

import com.top.okya.activiti6.DeleteTaskCmd;
import com.top.okya.activiti6.SetFLowNodeAndGoCmd;
import com.top.okya.service.BPMService;
import com.top.okya.system.config.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;
import org.activiti.engine.task.Task;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2021/5/12 17:53
 * @describe：
 */

@Service
@Slf4j
public class BPMServiceImpl implements BPMService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private HistoryService historyService;

    @Override
    public String deploy(String xmlName, String pngName, String flowName) throws FileNotFoundException {
        log.info("-----------------开始部署新的流程实例-------------------");
        //读取filePath文件作为一个输入流
        FileInputStream xmlInputStream = new FileInputStream(ServerConfig.uploadFolder + xmlName);
        FileInputStream pngInputStream = new FileInputStream(ServerConfig.uploadFolder + pngName);
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream(xmlName, xmlInputStream)
                .addInputStream(pngName, pngInputStream)
                .name(flowName)
                //.key("FLOWTEST123")
                .deploy();
        String deploymentId = deployment.getId();
        log.info("-----------------流程部署成功，流程ID: {}，流程名称: {} -------------------", deploymentId, flowName);
        return deploymentId;
    }

    @Override
    public void removeByDeploymentId(String deploymentId) {
        log.info("-----------------开始移除流程实例-------------------");
        repositoryService.deleteDeployment(deploymentId, true);
        log.info("流程删除成功，流程ID:" + deploymentId);
    }

    @Override
    public String startProcessByKey(String processKey, Map<String, Object> var) {
        log.info("-----------------开始启动流程实例-------------------");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, var);
        String processId = processInstance.getId();
        log.info("-----------------流程实例启动成功，流程ID: {} -------------------", processId);
        log.info("-----------------流程实例启动成功，流程定义ID: {} -------------------", processInstance.getProcessDefinitionId());
        return processId;
    }

    @Override
    public void findTask(String assignee) {
        log.info("-----------------开始查询用户${}的待办任务-------------------", assignee);
        List<Task> list = taskService.createTaskQuery() // 创建任务查询对象
                /**查询条件（where部分）*/
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                // .taskCandidateUser(candidateUser)//组任务的办理人查询
                // .processDefinitionId(processDefinitionId)//使用流程定义ID查询
                // .processInstanceId(processInstanceId)//使用流程实例ID查询
                // .executionId(executionId)//使用执行对象ID查询
                /**排序*/
                .orderByTaskCreateTime().asc()//使用创建时间的升序排列
                /**返回结果集*/
                //.singleResult()//返回惟一结果集
                // .count()//返回结果集的数量
                .list();
        if (list != null && list.size() > 0) {
            for (Task task : list) {
                log.info("任务ID:" + task.getId() + "任务名称:" + task.getName() + "任务的创建时间:" + task.getCreateTime() + "任务的办理人:" + task.getAssignee() + "流程实例ID：" + task.getProcessInstanceId() + "执行对象ID:" + task.getExecutionId() + "流程定义ID:" + task.getProcessDefinitionId());
            }
        } else {
            log.info("未查询到用户${}的待办任务", assignee);
        }
    }

    @Override
    public void completeTask(String userCode, String processId, Map<String, Object> var) {
        log.info("-----------------开始调用完成任务接口，ASSIGNEE: ${}, PROC_INST_ID: ${}-------------------", userCode, processId);
        Task currentTask = taskService.createTaskQuery()
                .taskAssignee(userCode)
                .processInstanceId(processId)
                .singleResult();
        String taskId = currentTask.getId();
        log.info("-----------------获取当前任务完成，ID：${}-------------------", taskId);
        taskService.complete(taskId, var);
        log.info("-----------------任务ID:${}执行完成-------------------", taskId);
        log.info("完成任务：任务ID：" + taskId);
    }

    @Override
    public void rejectTask(String userCode, String processId) {
        log.info("-----------------开始调用驳回任务接口，ASSIGNEE: ${}, PROC_INST_ID: ${}-------------------", userCode, processId);
        //当前任务
        log.info("-----------------获取任务的任务对象-------------------");
        List<Task> taskList = taskService.createTaskQuery()
                .processInstanceId(processId)
                .list();
        log.info("-----------------获取任务对象，完成-------------------");
        //Task currentTask = taskService.createTaskQuery().taskAssignee(userCode).processInstanceId(processId).singleResult();
        if (!taskList.isEmpty()) {
            //获取流程定义
            log.info("-----------------根据任务对象获取流程-------------------");
            Process process = repositoryService.getBpmnModel(taskList.get(0).getProcessDefinitionId()).getMainProcess();
            log.info("-----------------获取流程对象，完成-------------------");
            //获取目标节点定义
            log.info("-----------------获取上级节点对象-------------------");
            List<HistoricTaskInstance> historyList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processId)
                    .orderByHistoricTaskInstanceEndTime()
                    .finished()
                    .desc()
                    .list();
            FlowNode targetNode = null;
            if(!historyList.isEmpty()){
                targetNode = (FlowNode) process.getFlowElement(historyList.get(0).getTaskDefinitionKey());
                log.info("-----------------获取上级节点对象，完成 ${} -------------------", historyList.get(0).getTaskDefinitionKey());
            }
            //删除当前所有任务
            log.info("-----------------删除当前运行的所有任务-------------------");
            String executionEntityId = null;
            for (Task currentTask : taskList) {
                log.info("-----------------删除当前运行任务，ID${}-------------------", currentTask.getId());
                executionEntityId = managementService.executeCommand(new DeleteTaskCmd(currentTask.getId()));
            }
            log.info("-----------------删除当前运行的所有任务，完成 ${}-------------------", executionEntityId);
            //流程执行到来源节点
            log.info("-----------------开始驳回任务-------------------");
            managementService.executeCommand(new SetFLowNodeAndGoCmd(targetNode, executionEntityId));
            log.info("-----------------驳回完成-------------------");
        }
    }

}
