package org.kayura.activiti.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Assert;
import org.junit.Test;
import org.kayura.utils.StringUtils;

public class LoopUserTaskTest extends AbstractTest {

	private String businessKey = "327FC8F55D5C11E6ABB6525400551C0D";
	private ProcessInstance processInstance;
	private String[] assignneUsers = { "zhangfei", "liube", "zhaoyun" };
	private List<String> assignneUserList = Arrays.asList(assignneUsers);

	public void deployProcess() {

		// Deployment

		List<Deployment> deploys = this.repositoryService.createDeploymentQuery().list();
		for (Deployment deploy : deploys) {
			this.repositoryService.deleteDeployment(deploy.getId(), true);
		}

		this.repositoryService.createDeployment().addClasspathResource("custom_task.bpmn").deploy();
	}

	@Test
	public void whileTaskTest() {

		deployProcess();

		// Start

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("assignee", assignneUsers[0]);

		processInstance = runtimeService.startProcessInstanceByKey("appleave", businessKey, vars);

		// process task

		for (int i = 1; i <= assignneUsers.length; i++) {

			String curUserId = assignneUsers[i - 1];

			TaskQuery taskQuery = this.taskService.createTaskQuery();
			Task task = taskQuery.taskAssignee(curUserId).singleResult();

			if (task != null) {

				Map<String, Object> taskVars = new HashMap<String, Object>();
				if (assignneUsers.length > i) {
					taskVars.put("assignee", assignneUsers[i]);
				} else {
					taskVars.put("allowEnd", true);
				}

				this.identityService.setAuthenticatedUserId(curUserId);
				this.taskService.addComment(task.getId(), task.getProcessInstanceId(), ("同意" + curUserId));
				this.taskService.complete(task.getId(), taskVars);
			}
		}

		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstance.getId()).singleResult();

		Assert.assertNotNull(hpi);
	}

	@Test
	public void mockProcessTaskTest() {

		deployProcess();

		/** 启动流程 **/
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("assignee", assignneUsers[0]);

		processInstance = runtimeService.startProcessInstanceByKey("appleave", businessKey, vars);

		// 处理第1环节,并指定下一处理人
		handlerTask(assignneUsers[0], true);

		// 处理第2环节,并退回给第一处理人
		handlerTask(assignneUsers[1], false);

		// 重新处理第1环节,并流转至下一处理人
		handlerTask(assignneUsers[0], true);
	}

	/**
	 * 处理工作流任务
	 * 
	 * @param userId
	 *            当前处理用户Id
	 * @param direction
	 *            true 向后走, false 退回上一步
	 */
	protected void handlerTask(String userId, boolean direction) {

		int userIdx = assignneUserList.indexOf(userId);
		if (userIdx >= 0) {

			String nextUserId = "";
			boolean allowEnd = false;

			if (direction) {

				if (assignneUsers.length == userIdx + 1) {
					allowEnd = true;
				} else {
					nextUserId = assignneUsers[userIdx + 1];
				}
			} else {

				if (userIdx <= 0) {

					runtimeService.suspendProcessInstanceById(processInstance.getId());
					return;
				} else {
					nextUserId = assignneUsers[userIdx - 1];
				}
			}

			TaskQuery taskQuery = this.taskService.createTaskQuery();
			Task task = taskQuery.taskAssignee(userId).singleResult();
			this.identityService.setAuthenticatedUserId(userId);

			// 添加注释
			this.taskService.addComment(task.getId(), task.getProcessInstanceId(), ("同意" + userId));

			// 完成任务
			Map<String, Object> taskVars = new HashMap<String, Object>();
			if (allowEnd) {
				taskVars.put("allowEnd", true);
			} else {
				taskVars.put("assignee", nextUserId);
			}

			this.taskService.complete(task.getId(), taskVars);
		}
	}

	protected void delegateTask(String userId, String delegateUserId) {

		
	}

	protected void addCommant(String userId, String commint, String photoUrl, String fileUrl) {

		Task aliveTask = this.taskService.createTaskQuery().processInstanceId(this.processInstance.getId())
				.singleResult();

		if (aliveTask != null) {

			this.identityService.setAuthenticatedUserId("chengwei");

			this.taskService.addComment(aliveTask.getId(), aliveTask.getProcessInstanceId(), commint);

			if (StringUtils.isNotEmpty(photoUrl)) {
				this.taskService.createAttachment("photo", aliveTask.getId(), aliveTask.getProcessInstanceId(), "", "",
						photoUrl);
			}

			if (StringUtils.isNotEmpty(fileUrl)) {
				this.taskService.createAttachment("file", aliveTask.getId(), aliveTask.getProcessInstanceId(), "", "",
						fileUrl);
			}
		}
	}
}
