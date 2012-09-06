package mobi.monaca.framework.template.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.AssertionFailedError;

public class StringComparisonFailure extends AssertionFailedError {
	static final private long serialVersionUID = 0;
	
	protected String msg, actual, expect;
	
	public StringComparisonFailure(String msg, String actual, String expect) {
		this.msg = msg;
		this.actual = actual;
		this.expect = expect;
	}
	
	@Override
	public String getMessage() {
		return 
			"fail.\n" +
		
			"description:\n" + indent(msg) + "\n" +
			
			"expected" + getInfo(expect) + ":\n" +
			indent(expect) + "\n" +
			
			"actual" + getInfo(actual) + ":\n" +
			indent(actual)
			;
	}
	
	protected Map<String, String> getInfo(String str) {
		HashMap<String, String> result = new HashMap<String, String>();
		
		result.put("length", String.valueOf(str.length()));
		result.put("lines", String.valueOf(str.split("\r\n|\n").length));
		
		return result;
	}
	
	protected String indent(String str) {
		StringBuilder builder = new StringBuilder();
		
		if (str == null) {
			throw new RuntimeException("first argument must not be null.");
		}
		
		String[] lines = str.split("\r\n|\n");
		
		for (String line : lines) {
			builder.append(" |" + line + "\n");
		}
		
		return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : " <none>";
	}
	
	/** Assert string equality. Output string information in detail when fail. */
	static public void assertEquals(String msg, String actual, String expect) {
		if (msg == null) {
			msg = "";
		}
		if (actual == null) {
			actual = "";
		}
		if (expect == null) {
			expect = "";
		}
		
		if (!actual.equals(expect)) {
			throw new StringComparisonFailure(msg, actual, expect);
		}
	}

}
