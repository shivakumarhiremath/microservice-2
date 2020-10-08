package com.mbrdi.EncryptDecrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class AESEncryptDecryptUtil {
	
	private static final String ALGO = "AES";

	private static final byte[] secretKey = new byte[] {'O', 'C', 'T', 'O', 'B', 'E', 'R', '$', '%', '*', '&', '$', '%', '*', '&','*'};

	private static SecretKeySpec skeySpec = new SecretKeySpec(secretKey, ALGO);

	public  String encrypt(String plainText)
	{
		String encryptedText = "";
		try
		{
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			encryptedText = Base64Utils.encodeToString(cipher.doFinal(plainText.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();;
		}
		return encryptedText;
	}

	public  String decrypt(String encryptedText) throws Exception
	{
		String plainText = "";
		try
		{
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			plainText = new String(cipher.doFinal(Base64Utils.decodeFromString(encryptedText)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return plainText;
	}

}
