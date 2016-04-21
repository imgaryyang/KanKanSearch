package com.kankanews.security;

import java.io.PrintStream;
import java.net.URLEncoder;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESOld {
	private static final String DES = "DES";

	public static byte[] encrypt(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws Exception {
		SecureRandom localSecureRandom = new SecureRandom();
		DESKeySpec localDESKeySpec = new DESKeySpec(paramArrayOfByte2);
		SecretKeyFactory localSecretKeyFactory = SecretKeyFactory
				.getInstance("DES");
		SecretKey localSecretKey = localSecretKeyFactory
				.generateSecret(localDESKeySpec);
		Cipher localCipher = Cipher.getInstance("DES");
		localCipher.init(1, localSecretKey, localSecureRandom);
		return localCipher.doFinal(paramArrayOfByte1);
	}

	public static final String encrypt(String paramString1, String paramString2) {
		try {
			return byte2String(encrypt(paramString1.getBytes(),
					paramString2.getBytes()));
		} catch (Exception localException) {
		}
		return null;
	}

	public static String byte2String(byte[] paramArrayOfByte) {
		String str1 = "";
		String str2 = "";
		for (int i = 0; i < paramArrayOfByte.length; i++) {
			str2 = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
			if (str2.length() == 1)
				str1 = str1 + "0" + str2;
			else
				str1 = str1 + str2;
		}
		return str1.toUpperCase();
	}

	public static byte[] decrypt(byte[] paramArrayOfByte1,
			byte[] paramArrayOfByte2) throws Exception {
		SecureRandom localSecureRandom = new SecureRandom();
		DESKeySpec localDESKeySpec = new DESKeySpec(paramArrayOfByte2);
		SecretKeyFactory localSecretKeyFactory = SecretKeyFactory
				.getInstance("DES");
		SecretKey localSecretKey = localSecretKeyFactory
				.generateSecret(localDESKeySpec);
		Cipher localCipher = Cipher.getInstance("DES");
		localCipher.init(2, localSecretKey, localSecureRandom);
		return localCipher.doFinal(paramArrayOfByte1);
	}

	public static final String decrypt(String paramString1, String paramString2) {
		try {
			String str = new String(decrypt(
					String2byte(paramString1.getBytes()),
					paramString2.getBytes()), "GBK");

			return URLEncoder.encode(str, "GB2312");
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static byte[] String2byte(byte[] paramArrayOfByte) {
		if (paramArrayOfByte.length % 2 != 0)
			throw new IllegalArgumentException("MUST 6");
		byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
		for (int i = 0; i < paramArrayOfByte.length; i += 2) {
			String str = new String(paramArrayOfByte, i, 2);
			arrayOfByte[(i / 2)] = (byte) Integer.parseInt(str, 16);
		}
		return arrayOfByte;
	}

	public static void main(String[] paramArrayOfString) {
		if (paramArrayOfString.length != 3) {
			System.out.print("args error!");
			System.exit(0);
		}

		String str1 = paramArrayOfString[0];
		String str2 = paramArrayOfString[1];
		String str3 = paramArrayOfString[2];

		if ((!"en".equals(str1)) && (!"de".equals(str1))) {
			System.out.print("args error!");
			System.exit(0);
		}
		String str4;
		if ("en".equals(str1)) {
			str4 = encrypt(str2, str3);
			System.out.print(str4);
		} else if ("de".equals(str1)) {
			str4 = decrypt(str2, str3);
			System.out.print(str4);
		} else {
			System.out.print("some error!");
			System.exit(0);
		}
	}
}
