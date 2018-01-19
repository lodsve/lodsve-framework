/*
 * Copyright (C) 2018  Sun.Hao
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package lodsve.workflow.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lodsve.workflow.domain.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 2015-11-19 10:51
 */
@Component
public final class WorkflowLocalStorage {
    private static final Map<Class<?>, List<Workflow>> WORKFLOW_LOCAL_STORAGE = new HashMap<>();
    private static WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowLocalStorage(WorkflowRepository workflowRepository) {
        WorkflowLocalStorage.workflowRepository = workflowRepository;
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
            workflows = workflowRepository.findByDomain(clazz.getName());
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
