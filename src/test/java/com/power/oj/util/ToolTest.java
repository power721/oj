package com.power.oj.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ToolTest {

	@Test
	public void testFormatBaseURL() {
		assertNull(Tool.formatBaseURL(null));
		assertEquals("/oj", Tool.formatBaseURL("/oj"));
		assertEquals("", Tool.formatBaseURL("/"));
		assertEquals("/oj/user", Tool.formatBaseURL("/oj/user/"));
		assertEquals("/oj/user", Tool.formatBaseURL("/oj/user//"));
	}

	@Test
	public void testRandomPassword() {
		final int count = 9;
		String password = Tool.randomPassword(count);

		assertEquals(count, password.length());
	}
}
