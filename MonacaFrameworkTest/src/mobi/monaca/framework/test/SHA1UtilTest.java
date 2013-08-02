package mobi.monaca.framework.test;

import mobi.monaca.utils.SHA1Util;
import junit.framework.TestCase;

public class SHA1UtilTest extends TestCase {

	public void testToHashedString() {
		assertEquals("31f30ddbcb1bf8446576f0e64aa4c88a9f055e3c", SHA1Util.toHashedString("hoge"));
		assertEquals("c527d2ba1d2dbcefb509822abb0bf2ab04457a2c", SHA1Util.toHashedString("123456789abcdef"));
	}

}
