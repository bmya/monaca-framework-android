package mobi.monaca.framework.test;

import mobi.monaca.framework.util.AssetUriUtil;
import android.test.AndroidTestCase;

public class AssetUriUtilTest extends AndroidTestCase {

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

	public void testExistsAssetMethod() {
		assertTrue(AssetUriUtil.existsAsset(mContext, "hoge"));
		assertTrue(AssetUriUtil.existsAsset(mContext, "hoge.html"));
		assertTrue(AssetUriUtil.existsAsset(mContext, "www/hoge.html"));
		assertTrue(AssetUriUtil.existsAsset(mContext, "www/hoge"));

		assertTrue(!AssetUriUtil.existsAsset(mContext, ""));
		assertTrue(!AssetUriUtil.existsAsset(mContext, "foobar.html"));
		assertTrue(!AssetUriUtil.existsAsset(mContext, "www/foobar.html"));

		assertTrue(!AssetUriUtil.existsAsset(null, "hoge.html"));
		assertTrue(!AssetUriUtil.existsAsset(mContext, null));
		assertTrue(!AssetUriUtil.existsAsset(null, null));
	}

	/**
	public void testAssetToStringMethod() {
		//TODO try various extensions
		try {
			eq("hogehoge", AssetUriUtil.assetToString(mContext, "hoge.html"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	**/

	public void testNormalizeMethod() {
		eq("file:///fuga", AssetUriUtil.normalize("file:///hoge/../fuga"));
		eq("file:///hoge/fuga", AssetUriUtil.normalize("file:///hoge/./fuga"));
	}

	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}

}
