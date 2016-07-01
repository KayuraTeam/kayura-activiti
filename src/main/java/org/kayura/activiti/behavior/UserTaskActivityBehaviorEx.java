package org.kayura.activiti.behavior;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.task.TaskDefinition;

public class UserTaskActivityBehaviorEx extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 1L;

	public UserTaskActivityBehaviorEx(String userTaskId, TaskDefinition taskDefinition) {
		super(userTaskId, taskDefinition);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void handleAssignments(Expression assigneeExpression, Expression ownerExpression,
			Set<Expression> candidateUserExpressions, Set<Expression> candidateGroupExpressions, TaskEntity task,
			ActivityExecution execution) {

		if (assigneeExpression != null) {
			Object assigneeExpressionValue = assigneeExpression.getValue(execution);
			String assigneeValue = null;
			if (assigneeExpressionValue != null) {
				assigneeValue = assigneeExpressionValue.toString();
			}
			task.setAssignee(assigneeValue, true, false);
		}

		if (ownerExpression != null) {
			Object ownerExpressionValue = ownerExpression.getValue(execution);
			String ownerValue = null;
			if (ownerExpressionValue != null) {
				ownerValue = ownerExpressionValue.toString();
			}
			task.setOwner(ownerValue);
		}

		if (candidateGroupExpressions != null && !candidateGroupExpressions.isEmpty()) {
			for (Expression groupIdExpr : candidateGroupExpressions) {
				Object value = groupIdExpr.getValue(execution);
				if (value instanceof String) {
					List<String> candidates = extractCandidates((String) value);
					task.addCandidateGroups(candidates);
				} else if (value instanceof Collection) {
					task.addCandidateGroups((Collection) value);
				} else {
					throw new ActivitiIllegalArgumentException(
							"Expression did not resolve to a string or collection of strings");
				}
			}
		}

		if (candidateUserExpressions != null && !candidateUserExpressions.isEmpty()) {
			for (Expression userIdExpr : candidateUserExpressions) {
				Object value = userIdExpr.getValue(execution);
				if (value instanceof String) {
					List<String> candiates = extractCandidates((String) value);
					task.addCandidateUsers(candiates);
				} else if (value instanceof Collection) {
					task.addCandidateUsers((Collection) value);
				} else {
					throw new ActivitiException("Expression did not resolve to a string or collection of strings");
				}
			}
		}

		if (!taskDefinition.getCustomUserIdentityLinkExpressions().isEmpty()) {
			Map<String, Set<Expression>> identityLinks = taskDefinition.getCustomUserIdentityLinkExpressions();
			for (String identityLinkType : identityLinks.keySet()) {
				for (Expression idExpression : identityLinks.get(identityLinkType)) {
					Object value = idExpression.getValue(execution);
					if (value instanceof String) {
						List<String> userIds = extractCandidates((String) value);
						for (String userId : userIds) {
							task.addUserIdentityLink(userId, identityLinkType);
						}
					} else if (value instanceof Collection) {
						Iterator userIdSet = ((Collection) value).iterator();
						while (userIdSet.hasNext()) {
							task.addUserIdentityLink((String) userIdSet.next(), identityLinkType);
						}
					} else {
						throw new ActivitiException("Expression did not resolve to a string or collection of strings");
					}
				}
			}
		}

		if (!taskDefinition.getCustomGroupIdentityLinkExpressions().isEmpty()) {
			Map<String, Set<Expression>> identityLinks = taskDefinition.getCustomGroupIdentityLinkExpressions();
			for (String identityLinkType : identityLinks.keySet()) {
				for (Expression idExpression : identityLinks.get(identityLinkType)) {
					Object value = idExpression.getValue(execution);
					if (value instanceof String) {
						List<String> groupIds = extractCandidates((String) value);
						for (String groupId : groupIds) {
							task.addGroupIdentityLink(groupId, identityLinkType);
						}
					} else if (value instanceof Collection) {
						Iterator groupIdSet = ((Collection) value).iterator();
						while (groupIdSet.hasNext()) {
							task.addGroupIdentityLink((String) groupIdSet.next(), identityLinkType);
						}
					} else {
						throw new ActivitiException("Expression did not resolve to a string or collection of strings");
					}
				}
			}
		}
	}

}
