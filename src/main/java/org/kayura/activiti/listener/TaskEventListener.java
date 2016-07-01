package org.kayura.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TaskEventListener implements ActivitiEventListener {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public void onEvent(ActivitiEvent event) {

		logger.debug("TaskEventListener -> Type: " + event.getType() + 
				"; ExecutionId: " + event.getExecutionId() + 
				"; ProcessInstanceId: " + event.getProcessInstanceId());
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

}
