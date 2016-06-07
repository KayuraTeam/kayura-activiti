package org.kayura.activiti.form;

import java.text.Format;
import java.text.ParseException;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.form.AbstractFormType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

public class DateTimeFormType extends AbstractFormType {

	private static final long serialVersionUID = 1L;

	protected String datePattern = "YYYY-MM-dd hh:ss:mm";
	protected Format dateFormat;

	public DateTimeFormType(String datePattern) {
		this.datePattern = datePattern;
		this.dateFormat = FastDateFormat.getInstance(datePattern);
	}

	public String getName() {
		return "datetime";
	}

	public Object getInformation(String key) {
		if ("datePattern".equals(key)) {
			return datePattern;
		}
		return null;
	}

	public Object convertFormValueToModelValue(String propertyValue) {
		if (StringUtils.isEmpty(propertyValue)) {
			return null;
		}
		try {
			return dateFormat.parseObject(propertyValue);
		} catch (ParseException e) {
			throw new ActivitiIllegalArgumentException("invalid date value " + propertyValue);
		}
	}

	public String convertModelValueToFormValue(Object modelValue) {
		if (modelValue == null) {
			return null;
		}
		return dateFormat.format(modelValue);
	}

}
