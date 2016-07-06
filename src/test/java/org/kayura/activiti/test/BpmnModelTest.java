package org.kayura.activiti.test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.junit.Test;
import org.kayura.activiti.editor.json.converter.BpmnJsonExConverter;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BpmnModelTest {

	private BpmnModel buildModel() {

		BpmnModel bpmnModel = new BpmnModel();

		// Process
		Process process = new Process();
		process.setId("testModel");

		ExtensionAttribute initDate = new ExtensionAttribute("initDate");
		initDate.setNamespace("http://activiti.org/bpmn");
		initDate.setValue("${initDate}");
		process.addAttribute(initDate);

		bpmnModel.addProcess(process);

		// StartEvent
		StartEvent startEvent = new StartEvent();
		startEvent.setId("startEvent");
		FormProperty startDate = new FormProperty();
		startDate.setId("startDate");
		startDate.setType("date");
		startEvent.getFormProperties().add(startDate);

		ExtensionElement extElement = new ExtensionElement();
		extElement.setName("extentProperty");
		extElement.setNamespace("http://activiti.org/bpmn");
		ExtensionAttribute nameAttr = new ExtensionAttribute("id");
		nameAttr.setValue("status");
		ExtensionAttribute valueAttr = new ExtensionAttribute("value");
		valueAttr.setValue("0");
		extElement.addAttribute(nameAttr);
		extElement.addAttribute(valueAttr);
		startEvent.addExtensionElement(extElement);

		process.addFlowElement(startEvent);

		// UserTask
		UserTask userTask = new UserTask();
		userTask.setId("userTask");
		process.addFlowElement(userTask);

		// EndEvent
		EndEvent endEvent = new EndEvent();
		endEvent.setId("endEvent");
		process.addFlowElement(endEvent);

		GraphicInfo giStartEvent = new GraphicInfo();
		giStartEvent.setElement(startEvent);
		bpmnModel.addGraphicInfo(startEvent.getId(), giStartEvent);

		GraphicInfo giUserTask = new GraphicInfo();
		giUserTask.setElement(userTask);
		bpmnModel.addGraphicInfo(userTask.getId(), giUserTask);

		GraphicInfo giEndEvent = new GraphicInfo();
		giEndEvent.setElement(endEvent);
		bpmnModel.addGraphicInfo(endEvent.getId(), giEndEvent);

		return bpmnModel;
	}

	@Test
	public void convertToXmlTest() throws Exception {

		BpmnModel bpmnModel = buildModel();

		BpmnXMLConverter converter = new BpmnXMLConverter();
		String xml = new String(converter.convertToXML(bpmnModel), "UTF-8");

		System.out.println(xml);
	}

	@Test
	public void convertToModelTest() throws Exception {

		BpmnModel bpmnModel = buildModel();
		BpmnXMLConverter converter = new BpmnXMLConverter();
		String xml = new String(converter.convertToXML(bpmnModel), "UTF-8");

		byte[] bytes = xml.getBytes();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

		XMLInputFactory xif = XMLInputFactory.newInstance();
		InputStreamReader in = new InputStreamReader(inputStream, "UTF-8");
		XMLStreamReader xtr = xif.createXMLStreamReader(in);

		bpmnModel = converter.convertToBpmnModel(xtr);

		xml = new String(converter.convertToXML(bpmnModel), "UTF-8");
		System.out.println(xml);
	}

	public void convertToJsonTest() {

		BpmnModel bpmnModel = buildModel();

		BpmnJsonConverter jsonConverter = new BpmnJsonExConverter();
		ObjectNode objectNode = jsonConverter.convertToJson(bpmnModel);

		System.out.println(objectNode.toString());
	}

	@Test
	public void jsonToModelTest() throws Exception {

		BpmnModel bpmnModel = buildModel();
		BpmnJsonConverter jsonConverter = new BpmnJsonExConverter();
		ObjectNode objectNode = jsonConverter.convertToJson(bpmnModel);
		System.out.println(objectNode.toString());

		bpmnModel = jsonConverter.convertToBpmnModel(objectNode);
		BpmnXMLConverter converter = new BpmnXMLConverter();
		String xml = new String(converter.convertToXML(bpmnModel), "UTF-8");

		System.out.println(xml);
	}

}
