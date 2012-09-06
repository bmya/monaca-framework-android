package mobi.monaca.framework.template.test;

import java.io.StringReader;
import mobi.monaca.framework.template.ast.Node;
import mobi.monaca.framework.template.TemplateLexer;
import mobi.monaca.framework.template.TemplateParser;
import junit.framework.TestCase;

public class TemplateParserTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected TemplateParser createParser(String testCode) {
		StringReader reader = new StringReader(testCode);
		TemplateLexer lexer = new TemplateLexer(reader);
		return new TemplateParser(lexer);
	}
	
	protected Node parse(String testCode) {
		try {
			return (Node)createParser(testCode).parse().value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void testNodeDump() {
		assertTrue(parse("hoge") instanceof Node);
		assertEquals("raw\nraw", parse("hoge{#comment#}hoge").toString());
		assertEquals("raw\nblock:\n  raw", parse("hoge{% block a %}hoge{% endblock %}").toString());
		
		assertTrue(parse("hoge") instanceof Node);
		assertEquals(
				"raw\n" +
				"block:\n" +
				"  raw\n" +
				"  parent\n" +
				"  raw\n" +
				"raw", 
				parse("hoge{% block a %}hoge{% parent %}fuga{% endblock %}haha").toString());
	}
	
	public void testEmptyBlock() {
		StringComparisonFailure.assertEquals(
			"", 
			"raw\nblock:\nraw", 
			parse("hoge{% block hogehoge endblock %}hoge").toString()
		);
	}
	
	public void testFatalErrorReporting() {
		try {
			parse("{% block block %}").toString();
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
