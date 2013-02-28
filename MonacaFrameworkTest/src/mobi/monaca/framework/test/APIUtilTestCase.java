package mobi.monaca.framework.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mobi.monaca.utils.APIUtil;
import mobi.monaca.utils.MonacaDevice;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import junit.framework.TestCase;

public class APIUtilTestCase extends TestCase {

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

	}
}
