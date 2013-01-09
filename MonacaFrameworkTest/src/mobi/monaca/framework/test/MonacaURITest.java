package mobi.monaca.framework.test;


import org.json.JSONException;
import org.json.JSONObject;

import mobi.monaca.framework.MonacaURI;
import junit.framework.TestCase;

public class MonacaURITest extends TestCase {

	//TODO fill blank methods

	public void testHasQueryParams() {
		assertTrue((new MonacaURI("file:///android_asset/www/hoge.html?foo=bar")).hasQueryParams());
		assertTrue((new MonacaURI("file:///android_asset/www/hoge.html?foo=bar&piyo=hoge")).hasQueryParams());

		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html?")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html?=")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html?hoge=")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html?=hoge")).hasQueryParams());

		assertFalse((new MonacaURI("file:///android_asset/www/aa/hoge.html?ho/ge=bar")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html?hoge")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html??hoge=?foo=bar")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html?{&hoge=&foobar}")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html#foo=bar")).hasQueryParams());

		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html#foo=bar?hoge=piyo")).hasQueryParams());
		assertFalse((new MonacaURI("file:///android_asset/www/hoge.html#foo=bar?&hoge=piyo")).hasQueryParams());

		assertFalse((new MonacaURI("")).hasQueryParams());
		assertFalse((new MonacaURI("hoge")).hasQueryParams());
	}

	public void testGetUrlWithoutQuery() {
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?foo=bar")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?foo=bar&piyo=hoge")).getUrlWithoutQuery());

		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html?", (new MonacaURI("file:///android_asset/www/hoge.html?")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html?=", (new MonacaURI("file:///android_asset/www/hoge.html?=")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html?hoge=", (new MonacaURI("file:///android_asset/www/hoge.html?hoge=")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html?=hoge", (new MonacaURI("file:///android_asset/www/hoge.html?=hoge")).getUrlWithoutQuery());

		eq("file:///android_asset/www/aa/hoge.html?ho/ge=bar", (new MonacaURI("file:///android_asset/www/aa/hoge.html?ho/ge=bar")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html?hoge", (new MonacaURI("file:///android_asset/www/hoge.html?hoge")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html??hoge=?foo=bar", (new MonacaURI("file:///android_asset/www/hoge.html??hoge=?foo=bar")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html?{&hoge=&foobar}", (new MonacaURI("file:///android_asset/www/hoge.html?{&hoge=&foobar}")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html#foo=bar", (new MonacaURI("file:///android_asset/www/hoge.html#foo=bar")).getUrlWithoutQuery());

		eq("file:///android_asset/www/hoge.html#foo=bar?hoge=piyo", (new MonacaURI("file:///android_asset/www/hoge.html#foo=bar?hoge=piyo")).getUrlWithoutQuery());
		eq("file:///android_asset/www/hoge.html#foo=bar?&hoge=piyo", (new MonacaURI("file:///android_asset/www/hoge.html#foo=bar?&hoge=piyo")).getUrlWithoutQuery());

	//	eq("", (new MonacaURI("")).getUrlWithoutQuery());
	//	eq("hoge", (new MonacaURI("hoge")).getUrlWithoutQuery());
	}

	public void testBuildUrlWithQuery() {
		//TODO write other patterns
		try {
			eq("file:///android_asset/www/hoge.html?foo=bar", MonacaURI.buildUrlWithQuery("file:///android_asset/www/hoge.html",
					new JSONObject("{\"foo\":\"bar\"}")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}
}
