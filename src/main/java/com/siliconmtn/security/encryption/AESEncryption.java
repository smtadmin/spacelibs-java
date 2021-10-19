package com.siliconmtn.security.encryption;

// JDK 11.x
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Spring 5.x
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

// Spacelibs java 1.x
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title:</b> AESEncryption.java
 * <b>Project:</b> spacelibs-java
 * <b>Description:</b> AES 256 encryption wrapper
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Oct 14, 2021
 * <b>updates:</b>
 * 
 ****************************************************************************/
public class AESEncryption implements EncryptionIntfc {
	
	// Members
	private String passPhrase;
	private String salt;
	
	/**
	 * Assigns the pass phrase and salt to the encrypter
	 * @param passPhrase Pass phrase / password for the encryption
	 * @param salt Encryption salt
	 */
	public AESEncryption(String passPhrase, String salt) {
		this.passPhrase = passPhrase;
		this.salt = salt;
	}
	
	public AESEncryption(Map<String, String> attributes) {
		this.passPhrase = attributes.get(PASS_PHRASE_ELEMENT_NAME);
		this.salt = attributes.get(SALT_ELEMENT_NAME);
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.security.encryption.EncryptionIntfc#encrypt(java.lang.String)
	 */
	@Override
	public String encrypt(String data) throws EncryptionException {
		TextEncryptor encryptor = Encryptors.text(passPhrase, salt);    
		return encryptor.encrypt(data);
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.security.encryption.EncryptionIntfc#decrypt(java.lang.String)
	 */
	@Override
	public String decrypt(String data) throws EncryptionException {
		TextEncryptor encryptor = Encryptors.text(passPhrase, salt);    
		return encryptor.decrypt(data);
	}

	/* (non-javadoc)
	 * @see com.siliconmtn.security.encryption.EncryptionIntfc#isEncrypted(java.lang.String)
	 */
	@Override
	public boolean isEncrypted(String value) {
		if (StringUtil.isEmpty(value)) return false;
		
		// Check for base 64 encoding.  Return false if not encoded
		String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
	    Pattern r = Pattern.compile(pattern);
	    Matcher m = r.matcher(value);
	    if (!m.find()) return false;
	    
	    // Some values show as base64, but they are not encoded.  Try and decrypt
	    try {
	    	decrypt(value);
	    	return true;
	    } catch(Exception e) { /* Noting to do */ }
	    
	    return false;
	}
}
