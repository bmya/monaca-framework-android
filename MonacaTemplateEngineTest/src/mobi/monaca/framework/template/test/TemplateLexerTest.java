package mobi.monaca.framework.template.test;

import java.io.InputStream;

import java.util.Map;

import mobi.monaca.framework.template.LexerDumper;
import mobi.monaca.framework.template.LexerError;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

/** This class represents lexer test cases. */
public class TemplateLexerTest extends InstrumentationTestCase {
	
	/**
	 * Test all text representation of lexer test case. 
	 *  
	 * A text test case file is located in 'assets/testcases/lexer'.
	 */
	public void testAllTextTestCase() throws Throwable {
		AssetManager assetManager = getInstrumentation().getContext().getResources().getAssets();
		
		for (String path : TestUtil.getTestCaseFiles("testcases/lexer", assetManager)) {
			TextTestCase test;
			try {
				InputStream stream = assetManager.open(path);
				test = new TextTestCase(
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
	
	class TextTestCase {
		public Map<String, String> testProps;
		public String name;
		public String get(String key) { return testProps.get(key); }
		public TextTestCase(String name, Map<String, String> testProps) { 
			this.name = name;
			this.testProps = testProps; 
		}
	}
	
	protected void runTextTestCase(TextTestCase test) throws Throwable {
		String result = null;
		
		try {
			
			result = LexerDumper.dump(test.get("input"));
			StringComparisonFailure.assertEquals(test.get("description"), result, test.get("expect"));
			System.out.println("ok " + test.name);
			
		} catch (LexerError e) {
			throw new RuntimeException(e);
			
			/*result = e.toString();
			StringComparisonFailure.assertEquals(
				test.get("description"), 
				test.get("error"), 
				e.toString()
			);
			System.out.println("ok " + test.name);*/
		} 
	}

}
