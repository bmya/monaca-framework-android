package mobi.monaca.framework.template.test;

import java.io.IOException;

import mobi.monaca.framework.template.LexerDumper;

import junit.framework.TestCase;

public class LexerDumperTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testLexerDumper() throws IOException {
		assertEquals("T_RAW", LexerDumper.dump("hoge"));
		assertEquals("", LexerDumper.dump(""));
		assertEquals("T_RAW\nT_RAW", LexerDumper.dump("hoge{#comment#}hoge"));
		assertEquals("T_BLOCK\nT_ID\nT_RAW\nT_ENDBLOCK", LexerDumper.dump("{% block a %}raw{% endblock %}"));
		assertEquals("T_EXTENDS\nT_STRING", LexerDumper.dump("{% extends \"hoge\" %}"));
	}
	
	public void testInvalidStateClosing() throws IOException {
		try {
			LexerDumper.dump("{# comment");
			fail();
		} catch (RuntimeException e) {
			assertTrue(true);
		}
		
		try {
			LexerDumper.dump("{% hoge ");
			fail();
		} catch (RuntimeException e) {
			assertTrue(true);
		}
	}

}
