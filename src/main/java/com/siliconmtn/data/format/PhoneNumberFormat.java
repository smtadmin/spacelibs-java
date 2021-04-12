package com.siliconmtn.data.format;

// JDK 11
import java.io.Serializable;

// Google Phone Libs 8.x
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: PhoneNumberFormat.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Takes a phone number and formats it into the 
 * applicable notation.  Includes international formatting as well as US
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Jan 13, 2021
 * @updates:
 ****************************************************************************/
public class PhoneNumberFormat implements Serializable {
	
	/**
	 * Enum to define the formatting type of the phone number
	 * @author etewa
	 *
	 */
	public enum FormatType {
		NATIONAL_FORMAT(1), 
		DOT_FORMATTING(2),
		DASH_FORMATTING(3),
		INTERNATIONAL_FORMAT(4);
		
		int type = 1;
		FormatType(int type) {
			this.type = type;
		}
	}
		
	/// Alias the formatting to keep code clean below
	private static final PhoneNumberUtil.PhoneNumberFormat INTL_FORMAT = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;
	private static final PhoneNumberUtil.PhoneNumberFormat NTNAL_FORMAT = PhoneNumberUtil.PhoneNumberFormat.NATIONAL;
	
	/**
	 * This formatting uses +xx x xxx xxxx
	 */
	public static final int INTL_FORMATTING = 4;
	
	// Members
	private static final long serialVersionUID = 1l;
	private String formattedNumber = null;
	private String phoneNumber = null;
	private FormatType formatType = FormatType.NATIONAL_FORMAT;
	private String countryCode = "US";
	
	/**
	 * Constructor that accepts a string phone number and phone type
	 * @param phoneNumber Number to be formatted
	 */
	public PhoneNumberFormat(String phoneNumber) {
		this(phoneNumber, "US", FormatType.NATIONAL_FORMAT);
	}
	
	/**
	 * Constructor that accepts a string phone number and phone type
	 * @param phoneNumber Number to be formatted
	 * @param type Formatting type
	 */
	public PhoneNumberFormat(String phoneNumber, FormatType type) {
		this(phoneNumber, "US", type);
	}
	
	/**
	 * 
	 * @param phoneNumber Phone number to be formatted
	 * @param countryCode ISO country code of the phone number
	 * @param type Formatting type
	 */
	public PhoneNumberFormat(String phoneNumber, String countryCode, FormatType type) {
		this.setPhoneNumber(phoneNumber);
		this.setCountryCode(countryCode);
		this.setFormatType(type);
		assignFormattedNumber();
	}
	
	/**
	 * Return the phone number with the country dialing code
	 * @return Full phone number with country code
	 */
	public String getFullNumber() {
		String countryDialingCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryCode) + "";
		if (phoneNumber.startsWith(countryDialingCode)) {
			return phoneNumber;
		} else {
			return countryDialingCode + phoneNumber;
		}
	}
	
	/**
	 * Formats the phone number based upon the type of formatting requested
	 * Automatically converts formatting to "INTERNATIONAL" or "NATIONAL" if the
	 * Address is a non-US address 
	 */
	void assignFormattedNumber() {
		if (StringUtil.isEmpty(phoneNumber)) return;
		
		// If the request is for a dot or dash formatting for a non-US address
		// Convert the formatting type to International formatting
		StringBuilder sb = new StringBuilder();
		switch (formatType) {
			case DOT_FORMATTING:
				sb.append(assignUSFormatting('.', '.'));
				break;
			case DASH_FORMATTING:
				sb.append(assignUSFormatting('-', '-'));
				break;
			case INTERNATIONAL_FORMAT:
				sb.append(PhoneNumberUtil.getInstance().format(assignPhoneNumber(), INTL_FORMAT));
				break;
			default:
				sb.append(PhoneNumberUtil.getInstance().format(assignPhoneNumber(), NTNAL_FORMAT));
		}
		
		formattedNumber = sb.toString();
	}
	
	/**
	 * Dot or dash notation for US phones
	 * @param startDelim Delimiter to be applied
	 * @return Country formatted string
	 */
	String assignUSFormatting(char startDelim, char endDelim) {
		if (StringUtil.isEmpty(formattedNumber) || formattedNumber.length() < 7) return formattedNumber; 
		StringBuilder sb = new StringBuilder();
		sb.append(formattedNumber.substring(0,3)).append(startDelim);
		sb.append(formattedNumber.substring(3,6)).append(endDelim);
		sb.append(formattedNumber.substring(6,formattedNumber.length()));
		
		return sb.toString();
	}
	
	/**
	 * Converts the string phone number to a java phonenumber
	 * @return Parses the string phone number into a phone object
	 */
	PhoneNumber assignPhoneNumber() {
		PhoneNumber phone = null;
		PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
		try {
			phone = pnu.parse(formattedNumber, countryCode);
		} catch (Exception e) { /* Nothing to do */}
		
		return phone;
	}
	
	/**
	 * Returns the formatted number
	 * @return Formatted number
	 */
	public String getFormattedNumber() {
		return formattedNumber;
	}

	/**
	 * Gets the phone number
	 * @return Phone number member variable
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phone number
	 * @param phoneNumber Sets the phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		formattedNumber = StringUtil.removeNonNumeric(phoneNumber);
	}

	/**
	 * Sets the format type for the phone number
	 * @return Returns the formatting type
	 */
	public FormatType getFormatType() {
		return formatType;
	}

	/**
	 * Type of format
	 * @param formatType Sets the type of formatting for the phone number
	 */
	public void setFormatType(FormatType formatType) {
		this.formatType = formatType;
	}

	/**
	 * @return the countryCode in ISO format
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Sets the country code (ISO Format)
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = StringUtil.defaultString(countryCode, "US");
	}

	/**
	 * returns an obfuscated version of the provided phone number.
	 * @return obfuscated number e.g.: (XXX)XXX-1234
	 */
	public String obfuscatePhoneNumber() {
		StringBuilder sb = new StringBuilder();
		int ctr = 0;
		for (char c : formattedNumber.toCharArray()) {
			if (! Character.isDigit(c) || ctr > 5) sb.append(c);
			else {
				ctr++;
				sb.append('X');
			}
			
		}
		
		return sb.toString();
	}
	
}

