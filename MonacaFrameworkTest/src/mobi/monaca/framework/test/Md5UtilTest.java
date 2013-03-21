package mobi.monaca.framework.test;

import mobi.monaca.framework.bootloader.Md5Util;
import android.test.AndroidTestCase;

public class Md5UtilTest extends AndroidTestCase {

	public void testMd5() {
		assertEquals("a0ea1fa04a5798be", Md5Util.md5("hoge"));
		assertEquals("e6110b9a43f7cf67", Md5Util.md5("0123456789abcdef0123456789abcdef0123456789abcdef"));
	}

	public void testToMD5() {
		assertEquals("ea703e7aa1efda0064eaa507d9e8ab7e", Md5Util.toMD5("hoge"));
		assertEquals("fe9651e1d05b096a64737f476cef7647", Md5Util.toMD5("0123456789abcdef0123456789abcdef0123456789abcdef"));
	}

	public void testGetAssetFileHash() {
		assertEquals("a0ea1fa04a5798be", Md5Util.getAssetFileHash(mContext, "hoge"));
		assertEquals("88620cc1f0c6326f", Md5Util.getAssetFileHash(mContext, "www/foo.html"));
	}
}
