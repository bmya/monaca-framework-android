package mobi.monaca.framework.template.test;

import mobi.monaca.framework.template.TemplateEngine;
import mobi.monaca.framework.template.TemplateResource;
import android.test.InstrumentationTestCase;

public class TemplateEngineTest extends InstrumentationTestCase {
	
	public void testPathResolucation() {
		TemplateEngine engine = new TemplateEngine(getInstrumentation().getContext());
		
		assertEquals("hoge", engine.execute("pathtest/b/a.html"));
	}
	
	public void testRealTestcase() {
		TemplateEngine engine = new TemplateEngine(getInstrumentation().getContext());
		
		assertEquals("HelloWorld", engine.execute("file:///android_asset/realtestcases/01/index.html"));
	}
	
	public void testConstantsExistence() {
		
		assertEquals("<symbol: IOS>", execute("{{ IOS | raw }}"));
		assertEquals("<symbol: Android>", execute("{{ Android | raw }}"));
		assertEquals("<symbol: Android>", execute("{{ Device.Platform | raw }}"));
		assertEquals("<false>", execute("{{ Device.Platform == IOS | raw }}"));
		assertEquals("<true>", execute("{{ Device.Platform == Android | raw }}"));
		
		assertTrue(execute("{{ Device.Name }}").length() > 0);
		assertTrue(execute("{{ Device.UUID }}").length() > 0);
		assertTrue(execute("{{ Network.Hostname }}").length() > 0);
		
		assertTrue(
			execute("{{ Network.IsReachable | raw }}").equals("<true>") || 
			execute("{{ Network.IsReachable | raw }}").equals("<false>")
		);
	}
	
	protected String execute(String template) {
		TemplateResource.SimpleString resource = new TemplateResource.SimpleString();
		resource.put("a", template);
		TemplateEngine engine = new TemplateEngine(getInstrumentation().getContext(), resource);
		return engine.execute("a");
	}
	
}
