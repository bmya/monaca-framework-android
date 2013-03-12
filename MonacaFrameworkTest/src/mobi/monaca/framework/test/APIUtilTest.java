package mobi.monaca.framework.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import mobi.monaca.utils.APIUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class APIUtilTest extends TestCase {

	public void testGetQuery() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("platform", "android"));
		list.add(new BasicNameValuePair("deviceId", "testdevice"));
		list.add(new BasicNameValuePair("env", "prod"));
		list.add(new BasicNameValuePair("isCustom", "false"));
		list.add(new BasicNameValuePair("version",  "1.0.0"));
		list.add(new BasicNameValuePair("registrationId", "hogehoge"));

		try {
			String query = APIUtil.getQuery(list);
			assertEquals("platform=android&deviceId=testdevice&env=prod&isCustom=false&version=1.0.0&registrationId=hogehoge", query);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail();
		}

		List<NameValuePair> list2 = new ArrayList<NameValuePair>();
		list2.add(new BasicNameValuePair("reg&/id", "hoge/foo"));

		try {
			String query = APIUtil.getQuery(list2);
			assertEquals("reg%26%2Fid=hoge%2Ffoo", query); // should be encoded and no &mark
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail();
		}
	}
}
