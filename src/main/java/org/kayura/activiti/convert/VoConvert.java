package org.kayura.activiti.convert;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.kayura.activiti.vo.ProcessDefinitionVo;
import org.kayura.activiti.vo.TaskVo;

public final class VoConvert {

	public static List<ProcessDefinitionVo> toProcessDefinitions(List<ProcessDefinition> list) {

		List<ProcessDefinitionVo> volist = new ArrayList<ProcessDefinitionVo>();
		if (list != null) {
			for (ProcessDefinition o : list) {
				volist.add(new ProcessDefinitionVo(o));
			}
		}
		return volist;
	}

	public static ProcessDefinitionVo toProcessDefinition(ProcessDefinition o) {

		return new ProcessDefinitionVo(o);
	}

	public static List<TaskVo> toTasks(List<Task> list) {

		List<TaskVo> volist = new ArrayList<TaskVo>();
		if (list != null) {
			for (Task o : list) {
				volist.add(new TaskVo(o));
			}
		}
		return volist;
	}

	public static TaskVo toTask(Task o) {

		return new TaskVo(o);
	}

}
