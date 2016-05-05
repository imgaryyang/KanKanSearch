package com.kankanews.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password, String iv) {
		try {
			// password = "testtest20160422testtest20160422";
			// String password2 = "testtesttesttest";

			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// kgen.init(256, new SecureRandom(password.getBytes("utf-8")));
			// SecretKey secretKey = kgen.generateKey();
			// byte[] enCodeFormat = secretKey.getEncoded();
			//
			// KeyGenerator kgen2 = KeyGenerator.getInstance("AES");
			// kgen2.init(128, new SecureRandom(password.getBytes("utf-8")));
			// SecretKey secretKey2 = kgen2.generateKey();
			// byte[] enCodeFormat2 = secretKey2.getEncoded();

			SecretKeySpec key = new SecretKeySpec(password.getBytes("UTF-8"),
					"AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			byte[] byteContent = content.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key,
					new IvParameterSpec(iv.getBytes("UTF-8")));// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password, String iv) {
		try {
			// password = "testtest20160422testtest20160422";
			// String password2 = "testtesttesttest";

			// System.out.println(password.getBytes("utf-8").length);
			// KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// // SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// // random.setSeed(password.getBytes("utf-8"));
			// kgen.init(256, new SecureRandom(password.getBytes("utf-8")));
			// SecretKey secretKey = kgen.generateKey();
			// byte[] enCodeFormat = secretKey.getEncoded();
			//
			// KeyGenerator kgen2 = KeyGenerator.getInstance("AES");
			// kgen2.init(128, new SecureRandom(password2.getBytes("utf-8")));
			// SecretKey secretKey2 = kgen2.generateKey();
			// byte[] enCodeFormat2 = secretKey2.getEncoded();

			// System.out.println(password2.getBytes("utf-8").length);

			SecretKeySpec key = new SecretKeySpec(password.getBytes("UTF-8"),
					"AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key,
					new IvParameterSpec(iv.getBytes("UTF-8")));// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// public static String decrypt(String key, byte[] encrypted)
	// throws GeneralSecurityException {
	//
	// byte[] raw = key.getBytes(Charset.forName("UTF-8"));
	// if (raw.length != 16) {
	// throw new IllegalArgumentException("Invalid key size.");
	// }
	// SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	//
	// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	// cipher.init(Cipher.DECRYPT_MODE, skeySpec,
	// new IvParameterSpec(new byte[16]));
	// byte[] original = cipher.doFinal(encrypted);
	//
	// return new String(original, Charset.forName("UTF-8"));
	// }

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String content = "{\"id\":\"test\",\"name\":\"test\",\"website\":\"test\",\"gkey\":\"2b356568-1ce9-40fd-b6f6-bdedc4f06105\"}";
		String password = "testtest20160422testtest20160422";
		String iv = "testtesttesttest";
		// 加密
		System.out.println("加密前：" + content);
		byte[] encryptResult = encrypt(content, password, iv);
		String encryptResultStr = new String(Base64.encode(encryptResult));
		System.out.println("加密后：" + encryptResultStr);
		// 解密
//		String encryStr = "UvAyoUEclUVOyz6fSFeI1mktkEp+ARmLsAqOU2EPlBCRLoPlCOe8+EdOBFAtqkg+EQoOIRiqgJGPHCxjN2uOPFmhZa1E4ZToBr7gflmdcO0Qp8BQUBo6S9sl947Ed0Jm";
		String str = "5/DR7MPd4OlDJPNMfxwWtKsy4ZZDovhemWcKEAaURFyiKsNCO1/4LEHVzkhY6kI74/6wi0LXzlvHX6dLcu+2PI7s8iQeLG+RbVUUbnFJYqwSWbq+O66P5xalEoZGB2cyhNhutF7HQgUQ72XTAPxkJA==";
		// byte[] decryptFrom =
		// parseHexStr2Byte("UvAyoUEclUVOyz6fSFeI1mktkEp+ARmLsAqOU2EPlBCRLoPlCOe8+EdOBFAtqkg+EQoOIRiqgJGPHCxjN2uOPFmhZa1E4ZToBr7gflmdcO0Qp8BQUBo6S9sl947Ed0Jm");
		byte[] decryptResult = decrypt(Base64.decode(str), password, iv);
		System.out.println("解密后：" + new String(decryptResult, "utf-8"));
	}
}
