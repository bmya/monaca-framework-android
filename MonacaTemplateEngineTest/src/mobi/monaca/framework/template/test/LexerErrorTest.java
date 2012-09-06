package mobi.monaca.framework.template.test;

import mobi.monaca.framework.template.LexerError;
import junit.framework.TestCase;

public class LexerErrorTest extends TestCase {
	
	public void testToString() {
		LexerError error = new LexerError("hoge", "fuga.html", 1);
		assertEquals(error.toString(), "LexerError: hoge in fuga.html on line 1");
	}
}
