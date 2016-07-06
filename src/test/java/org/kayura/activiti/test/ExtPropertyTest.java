package org.kayura.activiti.test;

import java.io.InputStream;
import java.util.Map;

import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.util.ReflectUtil;
import org.junit.Test;

public class ExtPropertyTest extends AbstractTest {

	@Test
	public void loadXmlTest() throws Exception {

		ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine
				.getProcessEngineConfiguration();
		Context.setProcessEngineConfiguration(configuration);

		InputStream inputStream = ReflectUtil.getResourceAsStream("extProperty.bpmn");

		BpmnParser bpmnParser = configuration.getBpmnParser();
		BpmnParse bpmnParse = new BpmnParse(bpmnParser).sourceInputStream(inputStream).setSourceSystemId("extProperty")
				.name("extProperty");

		bpmnParse.execute();

		ActivityImpl approveTask = bpmnParse.getCurrentScope().findActivity("approveTask");
		Map<String, Object> properties = approveTask.getProperties();

		for (String key : properties.keySet()) {
			System.out.println(properties.get(key));
		}

	}

}
