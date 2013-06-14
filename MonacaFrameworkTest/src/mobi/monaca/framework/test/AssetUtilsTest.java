package mobi.monaca.framework.test;

import mobi.monaca.framework.util.AssetUtils;
import android.test.AndroidTestCase;

public class AssetUtilsTest extends AndroidTestCase {
	public void testExistsAssetMethod() {
		assertTrue(AssetUtils.existsAsset(mContext, "hoge"));
		assertTrue(AssetUtils.existsAsset(mContext, "hoge.html"));
		assertTrue(AssetUtils.existsAsset(mContext, "www/hoge.html"));
		assertTrue(AssetUtils.existsAsset(mContext, "www/hoge"));

		assertTrue(!AssetUtils.existsAsset(mContext, ""));
		assertTrue(!AssetUtils.existsAsset(mContext, "foobar.html"));
		assertTrue(!AssetUtils.existsAsset(mContext, "www/foobar.html"));

		assertTrue(!AssetUtils.existsAsset(null, "hoge.html"));
		assertTrue(!AssetUtils.existsAsset(mContext, null));
		assertTrue(!AssetUtils.existsAsset(null, null));
	}

	/**
	public void testAssetToStringMethod() {
		//TODO try various extensions
		try {
			eq("hogehoge", AssetUtils.assetToString(mContext, "hoge.html"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	**/

	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}

}
