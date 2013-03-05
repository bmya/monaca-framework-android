package mobi.monaca.framework.test;

import mobi.monaca.utils.MonacaConst;
import android.test.AndroidTestCase;

public class MonacaConstTest extends AndroidTestCase {
	//TODO test various unexpected cases
	public void testGetConst() {
		String apiurl = MonacaConst.getPushRegistrationAPIUrl(mContext, "mpushprojectid");
		assertEquals("https://goodurl.com/v1/push/register/mpushprojectid", apiurl); // defined in monaca_const.xml

		String encodedurl = MonacaConst.getPushRegistrationAPIUrl(mContext, "m/push/project/id");
		assertEquals("https://goodurl.com/v1/push/register/m%2Fpush%2Fproject%2Fid", encodedurl); // UTF8-Encode

		try {
			MonacaConst.getPushRegistrationAPIUrl(null, "m/push/project/id");
			fail("should be thrown NullPointerException");
		} catch (NullPointerException e) {
		}

		try {
			MonacaConst.getPushRegistrationAPIUrl(mContext, null);
			fail("should be thrown NullPointerException");
		} catch (NullPointerException e) {
		}

		try {
			MonacaConst.getPushRegistrationAPIUrl(null, null);
			fail("should be thrown NullPointerException");
		} catch (NullPointerException e) {
		}
	}

	public void testGetIsCustom() {
		String iscustom = MonacaConst.getIsCustom(mContext);
		assertEquals("good", iscustom); //first monaca tag is used

		try {
			MonacaConst.getIsCustom(null);
			fail("should be thrown NullPointerException");
		} catch (NullPointerException e) {
		}
	}

	public void testGetEnv() {
		String env = MonacaConst.getEnv(mContext);
		assertEquals("fine", env); //first monaca tag is used

		try {
			MonacaConst.getEnv(null);
			fail("should be thrown NullPointerException");
		} catch (NullPointerException e) {
		}
	}
}
