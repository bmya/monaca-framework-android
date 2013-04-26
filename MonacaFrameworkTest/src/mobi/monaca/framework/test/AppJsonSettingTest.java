package mobi.monaca.framework.test;

import org.json.JSONObject;

import mobi.monaca.framework.AppJsonSetting;
import junit.framework.TestCase;

public class AppJsonSettingTest extends TestCase {
	AppJsonSetting appJson;
	AppJsonSetting emptyAppJson;
	@Override
	protected void setUp() throws Exception {
		JSONObject json = new JSONObject();

		JSONObject monacaCloud = new JSONObject();
		monacaCloud.put("endPoint", "https://cloud.monaca.mobi/json-rpc/foobar");

		json.put("monacaCloud", monacaCloud);

		appJson = new AppJsonSetting(json);
		super.setUp();

		emptyAppJson = new AppJsonSetting(new JSONObject());
	}
	public void testParseMonacaCloud() {
		assertEquals("cloud.monaca.mobi", appJson.getMonacaCloudDomain());
		assertEquals("/json-rpc/foobar", appJson.getMonacaCloudPath());

		assertEquals("", emptyAppJson.getMonacaCloudDomain());
		assertEquals("", emptyAppJson.getMonacaCloudPath());
	}
}
