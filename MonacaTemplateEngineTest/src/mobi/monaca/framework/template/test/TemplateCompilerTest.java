package mobi.monaca.framework.template.test;

import mobi.monaca.framework.template.Template;
import mobi.monaca.framework.template.TemplateCompiler;
import mobi.monaca.framework.template.TemplateResource;
import junit.framework.TestCase;

public class TemplateCompilerTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testSimpleCase() {
		TemplateResource.SimpleString map = new TemplateResource.SimpleString();
		map.put("a", "hello world");
		
		TemplateCompiler compiler = new TemplateCompiler(map);
		
		Template template = compiler.compileFrom("a");
		assertTrue(!template.hasParent());
		assertEquals("a", template.getResourcePath());
	}
	
	public void testStandardCompile() {
		TemplateResource.SimpleString map = new TemplateResource.SimpleString();
		map.put("a", "{% block hoge %}{% endblock %}{% block fuga %}{% endblock %}");
		map.put("b", "{% extends 'a' %} {% block hoge %}overrided{% endblock %}");
		
		TemplateCompiler compiler = new TemplateCompiler(map);
		
		Template template = compiler.compileFrom("b");
		assertTrue(template.hasParent());
		assertEquals("b", template.getResourcePath());
		assertNotNull(template.getBlock("hoge"));
		assertNotNull(template.getBlock("fuga"));
	}
	
	public void testMultipleInheritence() {
		TemplateResource.SimpleString map = new TemplateResource.SimpleString();
		map.put("a", "{% block hoge %}{% endblock %}{% block fuga %}{% endblock %}");
		map.put("b", "{% extends 'a' %} {% block hoge %}overrided{% endblock %}");
		map.put("c", "{% extends 'b' %}");
		
		TemplateCompiler compiler = new TemplateCompiler(map);
		
		Template template = compiler.compileFrom("c");
		assertTrue(template.hasParent());
		assertEquals("c", template.getResourcePath());
		assertNotNull(template.getBlock("hoge"));
		assertNotNull(template.getBlock("fuga"));
		assertEquals("b", template.getParent().getResourcePath());
		assertEquals("a", template.getParent().getParent().getResourcePath());
	}

}
