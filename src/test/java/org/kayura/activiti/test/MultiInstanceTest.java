package org.kayura.activiti.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class MultiInstanceTest extends AbstractTest {

	private String businessKey = "327FC8F55D5C11E6ABB6525400551C0D";
	private ProcessInstance processInstance;
	private String[] assignneUsers = { "zhangfei", "liube", "zhaoyun" };

	public void deployProcess() {

		// Deployment

		List<Deployment> deploys = this.repositoryService.createDeploymentQuery().list();
		for (Deployment deploy : deploys) {
			this.repositoryService.deleteDeployment(deploy.getId(), true);
		}

		this.repositoryService.createDeployment().addClasspathResource("multiInstance.bpmn").deploy();
	}

	public void startProcess() {

		// Start

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("assignneUsers", Arrays.asList(assignneUsers));

		processInstance = this.runtimeService.startProcessInstanceByKey("appleave", businessKey, vars);
	}

	public void endProcess() {

		// end

		HistoricProcessInstance hpi = this.historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstance.getId()).singleResult();

		System.out.println(hpi.getStartActivityId());
	}

	@Test
	public void whileTaskTest() {

		deployProcess();
		startProcess();

		// tasks

		TaskQuery taskQuery = this.taskService.createTaskQuery();
		List<Task> tasks = taskQuery.list();
		while (!tasks.isEmpty()) {

			for (Task task : tasks) {

				this.taskService.addComment(task.getId(), task.getProcessInstanceId(), "同意1");
				this.taskService.complete(task.getId());
			}

			tasks = taskQuery.list();
		}
	}

	@Test
	public void customTaskTest() {

		deployProcess();
		startProcess();

		// 张飞(zhangfei) 正常处理任务
		TaskQuery taskQuery = this.taskService.createTaskQuery();
		List<Task> zhangfeiTasks = taskQuery.taskAssignee(assignneUsers[0]).list();
		this.identityService.setAuthenticatedUserId(assignneUsers[0]);
		for (Task task : zhangfeiTasks) {
			this.taskService.addComment(task.getId(), task.getProcessInstanceId(), "同意(zhangfei)");
			this.taskService.complete(task.getId());
		}

		// 一个用户来评论该流程(此人叫 chengwei)
		Task aliveTask = this.taskService.createTaskQuery().processInstanceId(this.processInstance.getId())
				.singleResult();
		this.identityService.setAuthenticatedUserId("chengwei");
		this.taskService.addComment(aliveTask.getId(), aliveTask.getProcessInstanceId(), "评论意见(chengwei)");
		this.taskService.createAttachment("photo", aliveTask.getId(), aliveTask.getProcessInstanceId(), "现场照片", "有点意思",
				"http://images.cnitblog.com/blog2015/677951/201503/261821005208961.jpg");

		// 刘备(liube) 退回给张飞.
		taskQuery = this.taskService.createTaskQuery();
		List<Task> liubeTasks = taskQuery.taskCandidateOrAssigned(assignneUsers[1]).list();
		for (Task task : liubeTasks) {

			TaskEntity newTask = (TaskEntity) taskService.newTask();
			newTask.setName(task.getName());
			newTask.setAssignee(assignneUsers[0]);
			newTask.setProcessDefinitionId(processInstance.getProcessDefinitionId());
			newTask.setProcessInstanceId(processInstance.getProcessInstanceId());
			newTask.setExecutionId(task.getExecutionId());
			taskService.saveTask(newTask);

			this.identityService.setAuthenticatedUserId(assignneUsers[1]);
			this.taskService.addComment(task.getId(), task.getProcessInstanceId(), "退回给张飞(liube)");
			this.taskService.complete(task.getId());
		}

		// 张飞(zhengfei) 再次处理任务.
		taskQuery = this.taskService.createTaskQuery();
		zhangfeiTasks = taskQuery.taskCandidateOrAssigned(assignneUsers[0]).list();
		for (Task task : liubeTasks) {
			this.identityService.setAuthenticatedUserId(assignneUsers[1]);
			this.taskService.addComment(task.getId(), task.getProcessInstanceId(), "同意(zhangfei)");
			this.taskService.complete(task.getId());
		}

		// zhaogyu task
		taskQuery = this.taskService.createTaskQuery();
		List<Task> zhaoyunTasks = taskQuery.taskCandidateOrAssigned(assignneUsers[2]).list();
		this.identityService.setAuthenticatedUserId(assignneUsers[2]);
		for (Task task : zhaoyunTasks) {

			// 完成意见
			this.taskService.addComment(task.getId(), task.getProcessInstanceId(), "任务委托给xialiang");
			this.taskService.delegateTask(task.getId(), "xialiang");
		}

		// xialiang task
		taskQuery = this.taskService.createTaskQuery();
		List<Task> xialiangTasks = taskQuery.taskCandidateOrAssigned("xialiang").list();
		this.identityService.setAuthenticatedUserId("xialiang");
		for (Task task : xialiangTasks) {

			// 完成意见
			this.taskService.addComment(task.getId(), task.getProcessInstanceId(), "处理 zhaogyu 委托的任务");
			this.taskService.resolveTask(task.getId());
		}

	}

}
