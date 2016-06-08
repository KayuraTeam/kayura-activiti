package org.kayura.activiti.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

public class DateTimeFormatTest {

	@Test
	public void convertTest() {

		Date now = new Date();
		String source = now.toString();
		System.out.println(source);

		FastDateFormat chinaDate = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		FastDateFormat sourceDate = FastDateFormat.getInstance("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

		try {
			Date object = sourceDate.parse(source);
			System.out.println(object);

			String format = chinaDate.format(object);
			System.out.println(format);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void convertTest2() {

		Date now = new Date();
		String source = now.toString();
		System.out.println(source);

		SimpleDateFormat chinaDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sourceDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

		try {
			Date object = sourceDate.parse(source);
			System.out.println(object);

			String format = chinaDate.format(object);
			System.out.println(format);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test2() {

		String dateString = "Thu Jan 01 22:23:43 CST 1970";

		SimpleDateFormat sfEnd = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
		try {
			System.out.println(sfEnd.format(sfStart.parse(dateString)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void timeTest1() {

		SimpleDateFormat timeFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		try {
			Date date = timeFormat.parse("Wed Jun 22 09:36:23 CST 2016");
			System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
