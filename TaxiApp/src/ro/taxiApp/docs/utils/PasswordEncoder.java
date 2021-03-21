package ro.taxiApp.docs.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.castor.core.util.Base64Encoder;

import ro.taxiApp.docs.core.AppException;
import ro.taxiApp.docs.core.AppExceptionUtils;

public class PasswordEncoder {
	private static final int ITERATION_COUNT = 65536;
	private static final int KEY_LENGTH = 128;
	private static final String SECRET_KEY_FACTORY_INSTANCE_NAME = "PBKDF2WithHmacSHA1";
	private String saltKeyValue;

	public String generatePasswordHash(String password) throws AppException {
		byte[] hashedPasswordBytes = null;

		KeySpec spec = new PBEKeySpec(password.toCharArray(), saltKeyValue.getBytes(), ITERATION_COUNT, KEY_LENGTH);
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_INSTANCE_NAME);
			hashedPasswordBytes = factory.generateSecret(spec).getEncoded();
			
			Base64Encoder encoder = new Base64Encoder();
			String returnString = new String(encoder.encode(hashedPasswordBytes));
			return returnString;
		} catch (NoSuchAlgorithmException e) {
			throw AppExceptionUtils.getAppExceptionFromExceptionCause(e);
		} 
		catch (InvalidKeySpecException e) {
			throw AppExceptionUtils.getAppExceptionFromExceptionCause(e);
		}
	}

	public String getSaltKeyValue() {
		return saltKeyValue;
	}

	public void setSaltKeyValue(String saltKeyValue) {
		this.saltKeyValue = saltKeyValue;
	}
	
}
