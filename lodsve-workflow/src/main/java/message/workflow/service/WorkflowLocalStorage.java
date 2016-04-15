package message.workflow.service;

import message.workflow.domain.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-19 10:51
 */
@Component
public final class WorkflowLocalStorage {
    private static final Map<Class<?>, List<Workflow>> WORKFLOW_LOCAL_STORAGE = new HashMap<>();
    private static WorkflowService workflowService;

    @Autowired
    public WorkflowLocalStorage(WorkflowService workflowService) {
        WorkflowLocalStorage.workflowService = workflowService;
    }

    public static void store(Workflow workflow) {
        Assert.notNull(workflow);

        Class<?> domain = workflow.getDomainClass();
        if (domain == null) {
            domain = forName(workflow.getDomain());
        }

        if (domain == null) {
            return;
        }

        List<Workflow> workflows = WORKFLOW_LOCAL_STORAGE.get(domain);
        if (workflows == null) {
            workflows = new ArrayList<>();
        }

        workflows.add(workflow);
        WORKFLOW_LOCAL_STORAGE.put(domain, workflows);
    }

    public static List<Workflow> getWorkflow(Class<?> clazz) {
        List<Workflow> workflows = WORKFLOW_LOCAL_STORAGE.get(clazz);
        if (workflows == null || workflows.isEmpty()) {
            // 初始化
            workflows = workflowService.findWorkflowByDomain(clazz.getName());
            WORKFLOW_LOCAL_STORAGE.put(clazz, workflows);
        }

        return workflows;
    }

    private static Class<?> forName(String clazz) {
        try {
            return ClassUtils.forName(clazz, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
