package mobi.monaca.framework.test;

import mobi.monaca.framework.UrlUtil;
import junit.framework.TestCase;

public class UrlUtilTestCase extends TestCase {
	
	public void testIsUrlMethod() {
		assertTrue(UrlUtil.isUrl("http://hogehoge"));
		assertTrue(UrlUtil.isUrl("https://hogehoge"));
		assertTrue(!UrlUtil.isUrl("file:///hogehoge"));
		assertTrue(!UrlUtil.isUrl("monaca://fuga"));
	}
	
	public void testResolveMethod() {
		eq("http://hoge/fuga", UrlUtil.resolve("http://hoge/hoge", "fuga"));
		eq("http://hoge/fuga", UrlUtil.resolve("http://hoge/hoge", "./fuga"));
		eq("http://hoge/fuga", UrlUtil.resolve("http://hoge/hoge/hoge", "../fuga"));
		eq("http://hoge/fuga", UrlUtil.resolve("http://hoge/hoge", "../fuga"));
	}
	
	public void testNormalizeMethod() {
		eq("http://hoge/hoge", UrlUtil.normalize("http://hoge/hoge"));
		eq("http://hoge/hoge", UrlUtil.normalize("http://hoge/./hoge"));
		eq("http://hoge/hoge", UrlUtil.normalize("http://hoge/../hoge"));
		eq("http://hoge/hoge", UrlUtil.normalize("http://hoge/../../hoge"));
	}
	
	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}
	
}
