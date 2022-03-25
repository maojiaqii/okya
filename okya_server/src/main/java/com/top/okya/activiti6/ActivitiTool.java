package com.top.okya.activiti6;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;

public class ActivitiTool {
    // 载入资源
    /*
     * private static ProcessEngineConfiguration processEngineConfiguration =
     * ProcessEngineConfiguration
     * .createProcessEngineConfigurationFromResourceDefault();
     */
    // 创建引擎

    private static ProcessEngine processEngine = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();

    private static RepositoryService repositoryService = processEngine.getRepositoryService();

    private static RuntimeService runtimeService = processEngine.getRuntimeService();

    private static TaskService taskService = processEngine.getTaskService();

    private static HistoryService historyService = processEngine.getHistoryService();

    // 初始化流程
    public static void deployProcessDefinition(String Name, String bpmn) throws Exception {
        Deployment deployment = repositoryService // 获取部署相关Service
                .createDeployment().name(Name)// 创建部署
                .addClasspathResource("com/nc/activiti/" + bpmn + ".bpmn") // 加载资源文件
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }

    public static void deleteProcessDefinition(String deploymentId) throws Exception {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    // 启动流程实例
    public static String StartProcessInstance(String key, Map<String, Object> leavemap) throws Exception {
        ProcessInstance pi = runtimeService // 运行时Service
                .startProcessInstanceByKey(key, leavemap); // 流程定义表的KEY字段值
        // 这个是查看数据库中act_re_procdef表
        return pi.getId();

    }

    // 删除流程实例
    public static void deleteProcessInstance(String processId, String info) throws Exception {
        runtimeService.deleteProcessInstance(processId, info);
    }

    // 根据assignee 查询待办
    public static List<Task> findTask(String assignee) throws Exception {
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .taskAssignee(assignee).orderByTaskCreateTime().desc().list(); // 指定某个人

        return taskList;
    }

    // 根据processId 查询待办
    public static List<Task> findTaskByProcessId(String processId) throws Exception {
        List<Task> taskList = taskService.createTaskQuery() // 创建任务查询
                .processInstanceId(processId).orderByTaskCreateTime().desc().list(); // 指定某个人

        return taskList;
    }

    // 根据 processId 和 assignee 查询待办
    public static List<Task> findTask(String processId, String assignee) throws Exception {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processId) // 创建任务查询
                .taskAssignee(assignee).orderByTaskCreateTime().desc().list(); // 指定某个人

        return taskList;
    }

    // 完成任务
    public static void completeTask(String taskId, Map<String, Object> varMap) throws Exception {
        taskService.complete(taskId, varMap); // 任务相关Service
    }

    /**
     * 取回流程中当前任务的上一个任务
     *
     * @param taskId     当前任务ID
     * @param activityId 取回节点ID （这里不一定是上一个流程节点id）
     * @throws Exception
     */
    private static void callBackProcess(String taskId, String activityId, Map<String, Object> varMap) throws Exception {
        if (activityId == null) {
            throw new Exception("目标节点ID为空！");
        }
        // 通过任务id获取流程实例id
        String processInstanceId = findProcessInstanceByTaskId(taskId).getId();

        // 通过任务id获取任务定义key
        String taskDefinitionKey = findTaskById(taskId).getTaskDefinitionKey();

        // 查找所有并行任务节点，同时取回
        List<Task> taskList = findTaskListByKey(processInstanceId, taskDefinitionKey);

        for (Task task : taskList) {
            commitProcess(task.getId(), varMap, activityId);
        }
    }

    /**
     * 提交流程/流程转向
     *
     * @param taskId     当前任务ID
     * @param variables  流程变量
     * @param activityId 流程转向执行任务节点ID,此参数为空，默认为提交操作
     * @throws Exception
     */
    private static void commitProcess(String taskId, Map<String, Object> variables, String activityId)
            throws Exception {
        if (variables == null) {
            variables = new HashMap<String, Object>();
        }
        // 跳转节点为空，默认提交操作
        if (activityId == null) {
            taskService.complete(taskId, variables);
        } else {
            // 流程转向操作
            // turnTransition(taskId, activityId, variables);
        }
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId 任务ID
     * @return 流程定义实体
     * @throws Exception
     */
    public static ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());
        if (processDefinition == null) {
            throw new Exception("流程定义未找到!");
        }
        return processDefinition;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return 任务实例
     * @throws Exception
     */
    static TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }

    /**
     * 根据流程实例ID和任务key值查询所有同级任务集合
     *
     * @param processInstanceId 流程实例id
     * @param key               任务定义key
     * @return
     */
    private static List<Task> findTaskListByKey(String processInstanceId, String key) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(key).list();
    }

    /**
     * 根据任务ID获取对应的流程实例
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    public static ProcessInstance findProcessInstanceByTaskId(String taskId) throws Exception {
        // 找到流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
        if (processInstance == null) {
            throw new Exception("流程实例未找到!");
        }
        return processInstance;
    }

    /**
     * 转办流程
     *
     * @param taskId   当前任务节点ID
     * @param userCode 被转办人Code
     */
    public static void transferAssignee(String taskId, String userCode) {
        taskService.setAssignee(taskId, userCode);
    }

    // 是否结束
    public static boolean isEnd(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (processInstance != null) {
            return processInstance.isEnded();
        } else {
            return true;
        }
    }

    // 会签
    public void countersignNotify(DelegateTask delegateTask) {
        System.out.println("会签监听");
        // 获取流程id
        String exId = delegateTask.getExecutionId();
        // 获取流程参数pass，会签人员完成自己的审批任务时会添加流程参数pass，false为拒绝，true为同意
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        TaskService taskService = engine.getTaskService();
        boolean pass = (Boolean) runtimeService.getVariable(exId, "pass");
        /*
         * false：有一个人拒绝，整个流程就结束了， 因为Complete condition的值为pass ==
         * false，即，当流程参数为pass时会签就结束开始下一个任务 所以，当pass == false时，直接设置下一个流程跳转需要的参数
         * true：审批人同意，同时要判断是不是所有的人都已经完成了，而不是由一个人同意该会签就结束
         * 值得注意的是如果一个审批人完成了审批进入到该监听时nrOfCompletedInstances的值还没有更新，因此需要+1
         */
        if (pass == false) {

            runtimeService.setVariable(exId, "result", "false");

        } else {
            Integer complete = (Integer) runtimeService.getVariable(exId, "nrOfCompletedInstances");
            Integer all = (Integer) runtimeService.getVariable(exId, "nrOfInstances");
            // 说明都完成了并且没有人拒绝
            if ((complete + 1) / all == 1) {
                runtimeService.setVariable(exId, "result", "true");
                // 下个任务
                String processInstanceId = delegateTask.getProcessInstanceId();
                Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
                System.out.println("下个任务编码：" + task.getId() + "，下个任务名称：" + task.getName());
            }
        }
    }

}
