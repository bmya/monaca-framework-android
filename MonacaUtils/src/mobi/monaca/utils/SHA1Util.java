package mobi.monaca.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1Util {
	private SHA1Util() {};

	public static String toHashedString(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(source.getBytes());

			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				String hex = String.format("%02x", b);
				sb.append(hex);
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
