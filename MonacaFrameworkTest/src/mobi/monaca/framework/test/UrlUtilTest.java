package mobi.monaca.framework.test;

import android.os.Environment;
import android.test.AndroidTestCase;
import mobi.monaca.framework.util.UrlUtil;
import junit.framework.TestCase;

public class UrlUtilTest extends AndroidTestCase {

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

	public void testIsMonacaUriMethod() {
		//Built project uri
		assertTrue(UrlUtil.isMonacaUri(mContext, "file:///android_asset/"));
		assertTrue(UrlUtil.isMonacaUri(mContext, "file:///android_asset/www/"));
		assertTrue(UrlUtil.isMonacaUri(mContext, "file:///android_asset/www/index.html"));
		assertTrue(UrlUtil.isMonacaUri(mContext, "file:///android_asset/www/hoge/"));

		//DebuggerProject uri
		assertTrue(UrlUtil.isMonacaUri(mContext, "file://" + mContext.getApplicationInfo().dataDir));

		//others
		assertFalse(UrlUtil.isMonacaUri(mContext, "http://www.google.com/"));
		assertFalse(UrlUtil.isMonacaUri(mContext, "http://localhost/"));
		assertFalse(UrlUtil.isMonacaUri(mContext, "hogehoge"));
		assertFalse(UrlUtil.isMonacaUri(mContext, "file:///data/"));
		assertFalse(UrlUtil.isMonacaUri(mContext, "file:///data/data"));
		assertFalse(UrlUtil.isMonacaUri(mContext, Environment.getExternalStorageDirectory().getAbsolutePath()));
	}

	public void testGetResolvedUrlMethod() {
		// getResolvedUrl is for file protocol
		// http protocol should not be resolved
		eq("http://monaca.mobi", UrlUtil.getResolvedUrl("http://monaca.mobi"));
		eq("http://monaca.mobi/features/../", UrlUtil.getResolvedUrl("http://monaca.mobi/features/../"));
		eq("http://blog.asial.co.jp/category/21/../../", UrlUtil.getResolvedUrl("http://blog.asial.co.jp/category/21/../../"));

		eq("file:///android_asset/www", "file:///android_asset/www");
		eq("file:///android_asset/www/", "file:///android_asset/www/");

		eq("file:///android_asset/", UrlUtil.getResolvedUrl("file:///android_asset/"));
		eq("file:///android_asset/", UrlUtil.getResolvedUrl("file:///android_asset/www/../"));
		eq("file:///android_asset/", UrlUtil.getResolvedUrl("file:///android_asset/www/hoge/../../"));
		eq("file:///android_asset/", UrlUtil.getResolvedUrl("file:///android_asset/www/hoge/foo/../../../"));

		eq("file:///data/data/mobi.monaca.framework.psedo/", UrlUtil.getResolvedUrl("file:///data/data/mobi.monaca.framework.psedo/"));
		eq("file:///data/data/mobi.monaca.framework.psedo/", UrlUtil.getResolvedUrl("file:///data/data/mobi.monaca.framework.psedo/www/../"));
		eq("file:///data/data/mobi.monaca.framework.psedo/", UrlUtil.getResolvedUrl("file:///data/data/mobi.monaca.framework.psedo/www/hoge/../../"));
		eq("file:///data/data/mobi.monaca.framework.psedo/", UrlUtil.getResolvedUrl("file:///data/data/mobi.monaca.framework.psedo/www/hoge/foo/../../../"));
	}

	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}

}
