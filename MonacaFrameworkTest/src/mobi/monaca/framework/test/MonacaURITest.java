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

	public void testGetUrlWithoutOptions() {
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?foo=bar")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?foo=bar&piyo=hoge")).getUrlWithoutOptions());

		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?=")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?hoge=")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?=hoge")).getUrlWithoutOptions());

		eq("file:///android_asset/www/aa/hoge.html", (new MonacaURI("file:///android_asset/www/aa/hoge.html?ho/ge=bar")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?hoge")).getUrlWithoutOptions());

		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html#foo")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html", (new MonacaURI("file:///android_asset/www/hoge.html?hoge=bar&aaa=bbb#foobar")).getUrlWithoutOptions());

		// undefined pattern
		eq("file:///android_asset/www/hoge.html??hoge=?foo=bar", (new MonacaURI("file:///android_asset/www/hoge.html??hoge=?foo=bar")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html#foo=bar?hoge=piyo", (new MonacaURI("file:///android_asset/www/hoge.html#foo=bar?hoge=piyo")).getUrlWithoutOptions());
		eq("file:///android_asset/www/hoge.html#foo=bar?&hoge=piyo", (new MonacaURI("file:///android_asset/www/hoge.html#foo=bar?&hoge=piyo")).getUrlWithoutOptions());


		eq("", (new MonacaURI("")).getUrlWithoutOptions());
		eq("hoge", (new MonacaURI("hoge")).getUrlWithoutOptions());
	}

	public void testBuildUrlWithQuery() {
		//TODO write other patterns
		try {
			eq("file:///android_asset/www/hoge.html?foo=bar", MonacaURI.buildUrlWithQuery("file:///android_asset/www/hoge.html",
					new JSONObject("{\"foo\":\"bar\"}")));


			eq("file:///android_asset/www/hoge.html?key1=bar&key2=piyo", MonacaURI.buildUrlWithQuery("file:///android_asset/www/hoge.html",
					new JSONObject("{\"key1\":\"bar\" , \"key2\":\"piyo\"}")));

			eq("file:///android_asset/www/hoge.html?%2e=value", MonacaURI.buildUrlWithQuery("file:///android_asset/www/hoge.html",
					new JSONObject("{\".\":\"value\"}")));

			eq("file:///android_asset/www/hoge.html?hoge&piyo=1", MonacaURI.buildUrlWithQuery("file:///android_asset/www/hoge.html",
					new JSONObject("{\"hoge\" : null, \"piyo\" : \"1\"}")));

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void eq(String expected, String actual) {
		LineComparisonFailure.doAssert(expected, actual);
	}
}
