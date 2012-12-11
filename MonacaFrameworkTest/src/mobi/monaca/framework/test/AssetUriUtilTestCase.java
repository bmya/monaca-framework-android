package mobi.monaca.framework.test;

import mobi.monaca.framework.util.AssetUriUtil;
import junit.framework.TestCase;

public class AssetUriUtilTestCase extends TestCase {
	
	public void testIsAssetUriMethod() {
		assertTrue(!AssetUriUtil.isAssetUri("file:///"));
		assertTrue(!AssetUriUtil.isAssetUri("file:///android_asset"));
		assertTrue(AssetUriUtil.isAssetUri("file:///android_asset/"));
		assertTrue(AssetUriUtil.isAssetUri("file:///android_asset/hoge"));
	}
	
	public void testResolveMethod() {
		eq("file:///fuga", AssetUriUtil.resolve("file:///hoge", "./fuga"));
		eq("file:///hoge/fuga", AssetUriUtil.resolve("file:///hoge/", "./fuga"));
		eq("file:///fuga", AssetUriUtil.resolve("file:///hoge/", "../fuga"));
		eq("file:///hoge/fuga/hoge", AssetUriUtil.resolve("file:///hoge/", "fuga/hoge"));
	}
	
	public void testNormalizeMethod() {
		eq("file:///fuga", AssetUriUtil.normalize("file:///hoge/../fuga"));
		eq("file:///hoge/fuga", AssetUriUtil.normalize("file:///hoge/./fuga"));
	}
	
	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}
	
}
