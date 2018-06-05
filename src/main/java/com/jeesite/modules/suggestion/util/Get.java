package com.jeesite.modules.suggestion.util;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.suggestion.entity.Question;
import com.jeesite.modules.suggestion.service.QuestionService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.OfficeService;

public class Get {

	public static Question getQuestion(String taskId) {
		TaskService taskService=SpringUtils.getBean(TaskService.class);
		RuntimeService runtimeService=SpringUtils.getBean(RuntimeService.class);
		Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance processInstance=runtimeService.createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
		.singleResult();
		String bzKey=processInstance.getBusinessKey();
		QuestionService questionService=SpringUtils.getBean(QuestionService.class);
		return questionService.get(bzKey);
	}
}
