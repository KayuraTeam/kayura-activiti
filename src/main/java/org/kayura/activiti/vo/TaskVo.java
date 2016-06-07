package org.kayura.activiti.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

public class TaskVo implements Task, Serializable {

	private static final long serialVersionUID = -2823798352166402264L;
	private String id;
	private String name;
	private String description;
	private int priority;
	private String owner;
	private String assignee;
	private String processInstanceId;
	private String executionId;
	private String processDefinitionId;
	private Date createTime;
	private String taskDefinitionKey;
	private Date dueDate;
	private String category;
	private String parentTaskId;
	private String tenantId;
	private String formKey;
	private Map<String, Object> taskLocalVariables;
	private Map<String, Object> processVariables;
	private boolean suspended;
	private DelegationState delegationState;

	public TaskVo() {
	}

	public TaskVo(Task task) {
		this.id = task.getId();
		this.name = task.getName();
		this.description = task.getDescription();
		this.priority = task.getPriority();
		this.owner = task.getOwner();
		this.assignee = task.getAssignee();
		this.processInstanceId = task.getProcessInstanceId();
		this.executionId = task.getExecutionId();
		this.processDefinitionId = task.getProcessDefinitionId();
		this.createTime = task.getCreateTime();
		this.taskDefinitionKey = task.getTaskDefinitionKey();
		this.dueDate = task.getDueDate();
		this.category = task.getCategory();
		this.parentTaskId = task.getParentTaskId();
		this.tenantId = task.getTenantId();
		this.formKey = task.getFormKey();
		this.suspended = task.isSuspended();
		this.delegationState = task.getDelegationState();
		this.taskLocalVariables = task.getTaskLocalVariables();
		this.processVariables = task.getProcessVariables();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public String getAssignee() {
		return assignee;
	}

	@Override
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	@Override
	public String getExecutionId() {
		return executionId;
	}

	@Override
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getParentTaskId() {
		return parentTaskId;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public String getFormKey() {
		return formKey;
	}

	@Override
	public Map<String, Object> getTaskLocalVariables() {
		return taskLocalVariables;
	}

	@Override
	public Map<String, Object> getProcessVariables() {
		return processVariables;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setLocalizedName(String localizedName) {
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setLocalizedDescription(String localizeddescription) {
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	@Override
	public DelegationState getDelegationState() {
		return delegationState;
	}

	@Override
	public void setDelegationState(DelegationState delegationState) {
		this.delegationState = delegationState;
	}

	@Override
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public void delegate(String userId) {
	}

	@Override
	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	@Override
	public boolean isSuspended() {
		return suspended;
	}

}
