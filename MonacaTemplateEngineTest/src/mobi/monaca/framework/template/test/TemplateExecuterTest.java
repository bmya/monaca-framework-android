package mobi.monaca.framework.template.test;

import android.test.InstrumentationTestCase;
import java.io.InputStream;
import java.util.Map;
import mobi.monaca.framework.template.TemplateCompiler;
import mobi.monaca.framework.template.TemplateEngineError;
import mobi.monaca.framework.template.TemplateExecuter;
import mobi.monaca.framework.template.TemplateResource;
import android.content.res.AssetManager;

public class TemplateExecuterTest extends InstrumentationTestCase {
	
	/** Test basic operation on template executer. */
	public void testTemplateExecuter() {
		TemplateResource.SimpleString map = new TemplateResource.SimpleString();
		map.put("a", "in a template");
		map.put("b", "hoge\n({% include 'a' %})\nfuga");
		
		TemplateCompiler compiler = new TemplateCompiler(map);
		
		assertEquals(
			"hoge\n(in a template)\nfuga",
			TemplateExecuter.build(map).execute(compiler.compileFrom("b"))
		);
	}
	
	/** Test basic operation on template executer. */
	public void testMultibyteTemplateExecuter() {
		TemplateResource.SimpleString map = new TemplateResource.SimpleString();
		map.put("a", "“ú–{Œê");
		
		TemplateCompiler compiler = new TemplateCompiler(map);
		
		assertEquals(
			"“ú–{Œê",
			TemplateExecuter.build(map).execute(compiler.compileFrom("a"))
		);
	}
	
	/**
	 * Test all text representation of template executer test case. 
	 *  
	 * All text test case file is located in 'assets/testcases/execution'.
	 */
	public void testAllTextTestCase() {
		AssetManager assetManager = getInstrumentation().getContext().getResources().getAssets();
		
		for (String path : TestUtil.getTestCaseFiles("testcases/execution", assetManager)) {
			
			TextTestCase test = null;
			try {
				InputStream stream = assetManager.open(path);
				test = buildTextTestCase(
					path, 
					TestUtil.testCaseRepresentation2Map(TestUtil.stream2String(stream))
				);
				stream.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			runTextTestCase(test);
		}
	}
	
	/** Run text test case to assert. */
	protected void runTextTestCase(TextTestCase test) {
		
		System.out.println(test.toString());
		
		try {
			StringComparisonFailure.assertEquals(
				test.testProps.get("description"), 
				TemplateExecuter.build(test.templateResource).execute(test.compiler.compileFrom(test.startPath)),
				test.testProps.get("expect")
			);
			System.out.println("ok " + test.name);
			
		} catch (TemplateEngineError e) {
			
			if (test.testProps.get("error") == null) {
				System.out.println("not ok " + test.name);
				throw e;
			}
			
			StringComparisonFailure.assertEquals(
				test.testProps.get("description"),
				e.toString(),
				test.testProps.get("error")
			);
			System.out.println("ok " + test.name);
			
		} catch (Exception e) {
			System.out.println("not ok " + test.name);
			throw new RuntimeException(e);
		}
		
	}
	
	/** This class represent text test case. */
	class TextTestCase {
		public String startPath;
		public TemplateCompiler compiler;
		public Map<String, String> testProps;
		public String name;
		public TemplateResource templateResource;
		
		public TextTestCase(String name, String startPath, TemplateCompiler compiler, Map<String, String> testProps, TemplateResource templateResource) {
			this.name = name;
			this.startPath = startPath;
			this.compiler = compiler;
			this.testProps = testProps;
			this.templateResource = templateResource;
		}
		
		@Override
		public String toString() {
			return testProps.toString();
		}
	}
	
	/** Build a text test case object. */
	protected TextTestCase buildTextTestCase(String name, Map<String, String> testProps) {
		
		TemplateResource.SimpleString resourceMap = new TemplateResource.SimpleString();
		String startPath = null;
		
		for (String key : testProps.keySet()) {
			if (key.startsWith("input:")) {
				resourceMap.put(key.substring(6, key.length()), testProps.get(key));
				if (startPath == null) {
					startPath = key.substring(6, key.length());
				}
			}
		}
		
		if (startPath == null) {
			throw new RuntimeException("Test properties must has start point for executingtemplate.");
		}
		
		return new TextTestCase(name, startPath, new TemplateCompiler(resourceMap), testProps, resourceMap);
	}
	
}
