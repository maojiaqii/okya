package com.top.okya.activiti6;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日期：2020年6月12日下午3:02:54
 */
@Component
@Slf4j
public class ActivitiMore implements TaskListener {

    private static RuntimeService runtimeService;

    private static TaskService taskService;

    @Autowired
    public void setRuntimeService(RuntimeService runtimeService) {
        ActivitiMore.runtimeService = runtimeService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        ActivitiMore.taskService = taskService;
    }

    public void notify(DelegateTask delegateTask) {
        log.info("---------------------会签监听开始-------------------------");
        // 获取流程id
        String exId = delegateTask.getExecutionId();
        /*
         * 要判断是不是所有的人都已经完成了，而不是由一个人同意该会签就结束
         * 值得注意的是如果一个审批人完成了审批进入到该监听时nrOfCompletedInstances的值还没有更新，因此需要+1
         */
        Object nrOfCompletedInstances = runtimeService.getVariable(exId, "nrOfCompletedInstances");
        Object nrOfInstances = runtimeService.getVariable(exId, "nrOfInstances");
        if(nrOfCompletedInstances != null && nrOfInstances != null){
            Integer complete = (Integer) nrOfCompletedInstances;
            Integer all = (Integer) nrOfInstances;
            log.info("审核通过人数：" + complete);
            log.info("审核总人数：" + all);
            // 说明都完成了并且没有人拒绝
            if (complete == all) {
                runtimeService.setVariable(exId, "result", "Y");
                // 下个任务
                String processInstanceId = delegateTask.getProcessInstanceId();
                Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
                System.out.println("下个任务编码：" + task.getId() + "，下个任务名称：" + task.getName());
            }
        }
    }
}
