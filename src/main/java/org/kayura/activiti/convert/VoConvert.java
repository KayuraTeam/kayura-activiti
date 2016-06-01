package org.kayura.activiti.convert;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;
import org.kayura.activiti.vo.ProcessDefinitionVo;

public final class VoConvert {

	public static List<ProcessDefinitionVo> to(List<ProcessDefinition> list) {

		List<ProcessDefinitionVo> volist = new ArrayList<ProcessDefinitionVo>();
		if (list != null) {
			for (ProcessDefinition o : list) {
				volist.add(new ProcessDefinitionVo(o));
			}
		}
		return volist;
	}

	public static ProcessDefinitionVo to(ProcessDefinition o) {

		return new ProcessDefinitionVo(o);
	}

}
