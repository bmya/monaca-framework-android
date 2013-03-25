package mobi.monaca.framework.test;

import mobi.monaca.framework.util.AssetUriUtil;
import android.test.AndroidTestCase;

public class AssetUriUtilTest extends AndroidTestCase {
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

	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}

}
