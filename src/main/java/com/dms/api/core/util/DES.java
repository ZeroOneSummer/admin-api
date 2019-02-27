package com.dms.api.core.util;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @version V1.0
 * @Description:
 * DES加密算法工具类
 * @author:dustine
 * @date 2017/10/20 下午6:27
 */
public class DES {

	private static final String CHARSET = "UTF-8";

	public static final String DES_ALGORITHM = "DES/CBC/PKCS5Padding";

	// 若采用NoPadding模式，data长度必须是8的倍数
//		public static final String DES_ALGORITHM = "DES/CBC/NoPadding";

	private static Logger logger = Logger.getLogger(DES.class);

	/**
	 * DES-CBC模式加密
	 * 
	 * @param plainData
	 *            明文
	 * @param keyData
	 *            密钥
	 * @return 密文
	 */
	public static final byte[] encryptCBC(byte[] plainData, byte[] keyData) {
		byte[] encryptData = null;

		try {
			DESKeySpec keySpec = new DESKeySpec(keyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec("12347890".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			encryptData = cipher.doFinal(plainData);
		} catch (Exception e) {
			logger.error(e);
		}

		return encryptData;
	}

	/**
	 * des 加密
	 * @param plainData
	 * @param key
	 * @return
	 * @throws Exception
     */
	public static final String encrypt(String plainData, String key) throws Exception {
		return Base64.encode(encryptCBC(plainData.getBytes(CHARSET), key.getBytes(CHARSET)));
	}

	/**
	 * DES-CBC模式解密
	 * 
	 * @param encryptData
	 *            密文
	 * @param keyData
	 *            密钥
	 * @return 明文
	 */
	public static final byte[] decryptCBC(byte[] encryptData, byte[] keyData) {
		byte[] plainData = null;

		try {
			DESKeySpec keySpec = new DESKeySpec(keyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(keySpec);

			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec("12347890".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			plainData = cipher.doFinal(encryptData);
		} catch (Exception e) {
			logger.error(e);
		}
		return plainData;
	}

	/**
	 * des 解密
	 * @param encryptData
	 * @param key
	 * @return
	 * @throws Exception
     */
	public static final String decrypt(String encryptData, String key) throws Exception {
		return new String(decryptCBC(Base64.decode(encryptData), key.getBytes(CHARSET)), CHARSET);
	}

	public static void main(String[] args) throws Exception {

		String plain = "测试abcdef";
		String key = "12345678";
		System.out.println("原文：" + plain);
		System.out.println("key：" + key);
		String encoded = DES.encrypt(plain, key);
		System.out.println("密文：" + encoded);

		String decoded = DES.decrypt(encoded, key);
		System.out.println("密文解密后：" + decoded);
	}


}
