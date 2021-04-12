package com.siliconmtn.data.format;

// JDK 11.x
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Apache commons 3.x
import org.apache.commons.collections4.map.HashedMap;

// Spacelibs 1.0
import com.siliconmtn.data.bean.GenericVO;
import com.siliconmtn.data.text.StringUtil;

/****************************************************************************
 * <b>Title</b>: TimeZoneManager.java
 * <b>Project</b>: SpaceLibs-Java
 * <b>Description: </b> Manages the data and interfacing into the time zones
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 1.0
 * @since Jan 21, 2021
 * @updates:
 ****************************************************************************/
public class TimeZoneManager {
	/**
	 * Assigns the time zones and their associate meta data
	 */
	protected static final Map<String, TimeZoneVO> TIME_ZONES = getTimeZones();

	/**
	 * Not used due to static methods only allowed
	 */
	private TimeZoneManager() {
		super();
	}
	
	/**
	 * Retrieves the current list of supported time zones
	 * @return The current list of supported time zones
	 */
	public static List<GenericVO> getTimeZoneList() {
		List<GenericVO> zones = new ArrayList<>();
		for (Map.Entry<String, TimeZoneVO> entry : TIME_ZONES.entrySet()) {
			zones.add(new GenericVO(entry.getKey(), entry.getValue().getName()));
		}
		
		return zones;
	}
	
	/**
	 * Retrieves the extended data for a given time zone id
	 * @param timeZoneId ID of the timezone to retrieve
	 * @return Timezone object
	 */
	public static TimeZoneVO getTimeZone(String timeZoneId) {
		return TIME_ZONES.get(timeZoneId);
	}

	/**
	 * Returns all of the TimeZone VOs that match the given iso code
	 * @param isoCode ISO identifier
	 * @return Collection of timezones that match the code (there is some overlap)
	 */
	public static List<TimeZoneVO> getTimeZoneByIsoCode(String isoCode) {
		List<TimeZoneVO> zones = new ArrayList<>();
		if (StringUtil.isEmpty(isoCode)) return zones;
		
		for (Map.Entry<String, TimeZoneVO> entry : TIME_ZONES.entrySet()) {
			if (isoCode.equalsIgnoreCase(entry.getValue().getIsoCode()))
				zones.add(entry.getValue());
		}
		
		return zones;

	}
	
	/**
	 * Assigns the time zones to a map for use in the date formatting
	 * @return id and name of the time zone
	 */
	protected static Map<String, TimeZoneVO> getTimeZones() {
		Map<String, TimeZoneVO> tz = new HashedMap<>();
		tz.put("Asia/Kabul", new TimeZoneVO("Asia/Kabul", "Afghanistan Standard Time", "(GMT +04:30) Kabul", ""));
		tz.put("America/Anchorage", new TimeZoneVO("America/Anchorage", "Alaskan Standard Time", "(GMT -09:00) Alaska", ""));
		tz.put("Asia/Riyadh", new TimeZoneVO("Asia/Riyadh", "Arab Standard Time", "(GMT +03:00) Kuwait, Riyadh", ""));
		tz.put("Asia/Dubai", new TimeZoneVO("Asia/Dubai", "Arabian Standard Time", "(GMT +04:00) Abu Dhabi, Muscat", ""));
		tz.put("Asia/Baghdad", new TimeZoneVO("Asia/Baghdad", "Arabic Standard Time", "(GMT +03:00) Baghdad", ""));
		tz.put("America/Buenos_Aires", new TimeZoneVO("America/Buenos_Aires", "Argentina Standard Time", "(GMT -03:00) Buenos Aires", ""));
		tz.put("America/Halifax", new TimeZoneVO("America/Halifax", "Atlantic Standard Time", "(GMT -04:00) Atlantic Time (Canada)", ""));
		tz.put("Australia/Darwin", new TimeZoneVO("Australia/Darwin", "AUS Central Standard Time", "(GMT +09:30) Darwin", ""));
		tz.put("Australia/Sydney", new TimeZoneVO("Australia/Sydney", "AUS Eastern Standard Time", "(GMT +10:00) Canberra, Melbourne, Sydney", ""));
		tz.put("Asia/Baku", new TimeZoneVO("Asia/Baku", "Azerbaijan Standard Time", "(GMT +04:00) Baku", ""));
		tz.put("Atlantic/Azores", new TimeZoneVO("Atlantic/Azores", "Azores Standard Time", "(GMT -01:00) Azores", ""));
		tz.put("Asia/Dhaka", new TimeZoneVO("Asia/Dhaka", "Bangladesh Standard Time", "(GMT +06:00) Dhaka", ""));
		tz.put("America/Regina", new TimeZoneVO("America/Regina", "Canada Central Standard Time", "(GMT -06:00) Saskatchewan", ""));
		tz.put("Atlantic/Cape_Verde", new TimeZoneVO("Atlantic/Cape_Verde", "Cape Verde Standard Time", "(GMT -01:00) Cape Verde Is.", ""));
		tz.put("Asia/Yerevan", new TimeZoneVO("Asia/Yerevan", "Caucasus Standard Time", "(GMT +04:00) Yerevan", ""));
		tz.put("Australia/Adelaide", new TimeZoneVO("Australia/Adelaide", "Cen. Australia Standard Time", "(GMT +09:30) Adelaide", ""));
		tz.put("America/Guatemala", new TimeZoneVO("America/Guatemala", "Central America Standard Time", "(GMT -06:00) Central America", ""));
		tz.put("Asia/Almaty", new TimeZoneVO("Asia/Almaty", "Central Asia Standard Time", "(GMT +06:00) Astana", ""));
		tz.put("America/Cuiaba", new TimeZoneVO("America/Cuiaba", "Central Brazilian Standard Time", "(GMT -04:00) Cuiaba", ""));
		tz.put("Europe/Budapes", new TimeZoneVO("Europe/Budapest", "Central Europe Standard Time", "(GMT +01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague", ""));
		tz.put("Europe/Warsaw", new TimeZoneVO("Europe/Warsaw", "Central European Standard Time", "(GMT +01:00) Sarajevo, Skopje, Warsaw, Zagreb", ""));
		tz.put("Pacific/Guadalcanal", new TimeZoneVO("Pacific/Guadalcanal", "Central Pacific Standard Time", "(GMT +11:00) Solomon Is., New Caledonia", ""));
		tz.put("America/Mexico_City", new TimeZoneVO("America/Mexico_City", "Central Standard Time (Mexico)", "(GMT -06:00) Guadalajara, Mexico City, Monterrey", ""));
		tz.put("America/Chicago", new TimeZoneVO("America/Chicago", "Central Standard Time", "(GMT -06:00) Central Time (US & Canada)", ""));
		tz.put("Asia/Shanghai", new TimeZoneVO("Asia/Shanghai", "China Standard Time", "(GMT +08:00) Beijing, Chongqing, Hong Kong, Urumqi", ""));
		tz.put("Etc/GMT+12", new TimeZoneVO("Etc/GMT+12", "Dateline Standard Time", "(GMT -12:00) International Date Line West", ""));
		tz.put("Africa/Nairobi", new TimeZoneVO("Africa/Nairobi", "E. Africa Standard Time", "(GMT +03:00) Nairobi", ""));
		tz.put("Australia/Brisbane", new TimeZoneVO("Australia/Brisbane", "E. Australia Standard Time", "(GMT +10:00) Brisbane", ""));
		tz.put("Europe/Minsk", new TimeZoneVO("Europe/Minsk", "E. Europe Standard Time", "(GMT +02:00) Minsk", ""));
		tz.put("America/Sao_Paulo", new TimeZoneVO("America/Sao_Paulo", "E. South America Standard Time", "(GMT -03:00) Brasilia", ""));
		tz.put("America/New_York", new TimeZoneVO("America/New_York", "Eastern Standard Time", "(GMT -05:00) Eastern Time (US & Canada)", ""));
		tz.put("Africa/Cairo", new TimeZoneVO("Africa/Cairo", "Egypt Standard Time", "(GMT +02:00) Cairo", ""));
		tz.put("Asia/Yekaterinburg", new TimeZoneVO("Asia/Yekaterinburg", "Ekaterinburg Standard Time", "(GMT +05:00) Ekaterinburg", ""));
		tz.put("Pacific/Fiji", new TimeZoneVO("Pacific/Fiji", "Fiji Standard Time", "(GMT +12:00) Fiji, Marshall Is.", ""));
		tz.put("Europe/Kiev", new TimeZoneVO("Europe/Kiev", "FLE Standard Time", "(GMT +02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius", ""));
		tz.put("Asia/Tbilisi", new TimeZoneVO("Asia/Tbilisi", "Georgian Standard Time", "(GMT +04:00) Tbilisi", ""));
		tz.put("Europe/London", new TimeZoneVO("Europe/London", "GMT Standard Time", "(GMT) Dublin, Edinburgh, Lisbon, London", ""));
		tz.put("America/Godthab", new TimeZoneVO("America/Godthab", "Greenland Standard Time", "(GMT -03:00) Greenland", ""));
		tz.put("Atlantic/Reykjavik", new TimeZoneVO("Atlantic/Reykjavik", "Greenwich Standard Time", "(GMT) Monrovia, Reykjavik", ""));
		tz.put("Europe/Istanbul", new TimeZoneVO("Europe/Istanbul", "GTB Standard Time", "(GMT +02:00) Athens, Bucharest, Istanbul", ""));
		tz.put("Pacific/Honolulu", new TimeZoneVO("Pacific/Honolulu", "Hawaiian Standard Time", "(GMT -10:00) Hawaii", ""));
		tz.put("Asia/Calcutta", new TimeZoneVO("Asia/Calcutta", "India Standard Time", "(GMT +05:30) Chennai, Kolkata, Mumbai, New Delhi", ""));
		tz.put("Asia/Tehran", new TimeZoneVO("Asia/Tehran", "Iran Standard Time", "(GMT +03:30) Tehran", ""));
		tz.put("Asia/Jerusalem", new TimeZoneVO("Asia/Jerusalem", "Israel Standard Time", "(GMT +02:00) Jerusalem", ""));
		tz.put("Asia/Amman", new TimeZoneVO("Asia/Amman", "Jordan Standard Time", "(GMT +02:00) Amman", ""));
		tz.put("Asia/Kamchatka", new TimeZoneVO("Asia/Kamchatka", "Kamchatka Standard Time", "(GMT +12:00) Petropavlovsk-Kamchatsky - Old", ""));
		tz.put("Asia/Seoul", new TimeZoneVO("Asia/Seoul", "Korea Standard Time", "(GMT +09:00) Seoul", ""));
		tz.put("Asia/Magadan", new TimeZoneVO("Asia/Magadan", "Magadan Standard Time", "(GMT +11:00) Magadan", ""));
		tz.put("Indian/Mauritius", new TimeZoneVO("Indian/Mauritius", "Mauritius Standard Time", "(GMT +04:00) Port Louis", ""));
		tz.put("Etc/GMT+2", new TimeZoneVO("Etc/GMT+2", "Mid-Atlantic Standard Time", "(GMT -02:00) Mid-Atlantic", ""));
		tz.put("Asia/Beirut", new TimeZoneVO("Asia/Beirut", "Middle East Standard Time", "(GMT +02:00) Beirut", ""));
		tz.put("America/Montevideo", new TimeZoneVO("America/Montevideo", "Montevideo Standard Time", "(GMT -03:00) Montevideo", ""));
		tz.put("Africa/Casablanca", new TimeZoneVO("Africa/Casablanca", "Morocco Standard Time", "(GMT) Casablanca", ""));
		tz.put("America/Chihuahua", new TimeZoneVO("America/Chihuahua", "Mountain Standard Time (Mexico)", "(GMT -07:00) Chihuahua, La Paz, Mazatlan", "MST"));
		tz.put("America/Denver", new TimeZoneVO("America/Denver", "Mountain Standard Time", "(GMT -07:00) Mountain Time (US & Canada)", "MST"));
		tz.put("Asia/Rangoon", new TimeZoneVO("Asia/Rangoon", "Myanmar Standard Time", "(GMT +06:30) Yangon (Rangoon)", ""));
		tz.put("Asia/Novosibirsk", new TimeZoneVO("Asia/Novosibirsk", "N. Central Asia Standard Time", "(GMT +06:00) Novosibirsk", ""));
		tz.put("Africa/Windhoek", new TimeZoneVO("Africa/Windhoek", "Namibia Standard Time", "(GMT +02:00) Windhoek", ""));
		tz.put("Asia/Katmandu", new TimeZoneVO("Asia/Katmandu", "Nepal Standard Time", "(GMT +05:45) Kathmandu", ""));
		tz.put("Pacific/Auckland", new TimeZoneVO("Pacific/Auckland", "New Zealand Standard Time", "(GMT +12:00) Auckland, Wellington", ""));
		tz.put("America/St_Johns", new TimeZoneVO("America/St_Johns", "Newfoundland Standard Time", "(GMT -03:30) Newfoundland", ""));
		tz.put("Asia/Irkutsk", new TimeZoneVO("Asia/Irkutsk", "North Asia East Standard Time", "(GMT +08:00) Irkutsk", ""));
		tz.put("Asia/Krasnoyarsk", new TimeZoneVO("Asia/Krasnoyarsk", "North Asia Standard Time", "(GMT +07:00) Krasnoyarsk", ""));
		tz.put("America/Santiago", new TimeZoneVO("America/Santiago", "Pacific SA Standard Time", "(GMT -04:00) Santiago", ""));
		tz.put("America/Tijuana", new TimeZoneVO("America/Tijuana", "Pacific Standard Time (Mexico)", "(GMT -08:00) Baja California", ""));
		tz.put("America/Los_Angeles", new TimeZoneVO("America/Los_Angeles", "Pacific Standard Time", "(GMT -08:00) Pacific Time (US & Canada)", ""));
		tz.put("Asia/Karachi", new TimeZoneVO("Asia/Karachi", "Pakistan Standard Time", "(GMT +05:00) Islamabad, Karachi", ""));
		tz.put("America/Asuncion", new TimeZoneVO("America/Asuncion", "Paraguay Standard Time", "(GMT -04:00) Asuncion", ""));
		tz.put("Europe/Paris", new TimeZoneVO("Europe/Paris", "Romance Standard Time", "(GMT +01:00) Brussels, Copenhagen, Madrid, Paris", ""));
		tz.put("Europe/Moscow", new TimeZoneVO("Europe/Moscow", "Russian Standard Time", "(GMT +03:00) Moscow, St. Petersburg, Volgograd", ""));
		tz.put("America/Cayenne", new TimeZoneVO("America/Cayenne", "SA Eastern Standard Time", "(GMT -03:00) Cayenne, Fortaleza", ""));
		tz.put("America/Bogota", new TimeZoneVO("America/Bogota", "SA Pacific Standard Time", "(GMT -05:00) Bogota, Lima, Quito", ""));
		tz.put("America/La_Paz", new TimeZoneVO("America/La_Paz", "SA Western Standard Time", "(GMT -04:00) Georgetown, La Paz, Manaus, San Juan", ""));
		tz.put("Pacific/Samoa", new TimeZoneVO("Pacific/Samoa", "Samoa Standard Time", "(GMT -11:00) Samoa", ""));
		tz.put("Asia/Bangkok", new TimeZoneVO("Asia/Bangkok", "SE Asia Standard Time", "(GMT +07:00) Bangkok, Hanoi, Jakarta", ""));
		tz.put("Asia/Singapore", new TimeZoneVO("Asia/Singapore", "Singapore Standard Time", "(GMT +08:00) Kuala Lumpur, Singapore", ""));
		tz.put("Africa/Johannesburg", new TimeZoneVO("Africa/Johannesburg", "South Africa Standard Time", "(GMT +02:00) Harare, Pretoria", ""));
		tz.put("Asia/Colombo", new TimeZoneVO("Asia/Colombo", "Sri Lanka Standard Time", "(GMT +05:30) Sri Jayawardenepura", ""));
		tz.put("Asia/Damascus", new TimeZoneVO("Asia/Damascus", "Syria Standard Time", "(GMT +02:00) Damascus", ""));
		tz.put("Asia/Taipei", new TimeZoneVO("Asia/Taipei", "Taipei Standard Time", "(GMT +08:00) Taipei", ""));
		tz.put("Australia/Hobart", new TimeZoneVO("Australia/Hobart", "Tasmania Standard Time", "(GMT +10:00) Hobart", ""));
		tz.put("Asia/Tokyo", new TimeZoneVO("Asia/Tokyo", "Tokyo Standard Time", "(GMT +09:00) Osaka, Sapporo, Tokyo", ""));
		tz.put("Pacific/Tongatapu", new TimeZoneVO("Pacific/Tongatapu", "Tonga Standard Time", "(GMT +13:00) Nuku'alofa", ""));
		tz.put("Asia/Ulaanbaatar", new TimeZoneVO("Asia/Ulaanbaatar", "Ulaanbaatar Standard Time", "(GMT +08:00) Ulaanbaatar", ""));
		tz.put("America/Indianapolis", new TimeZoneVO("America/Indianapolis", "US Eastern Standard Time", "(GMT -05:00) Indiana (East)", "EST"));
		tz.put("America/Phoenix", new TimeZoneVO("America/Phoenix", "US Mountain Standard Time", "(GMT -07:00) Arizona", "MST"));
		tz.put("Etc/GMT", new TimeZoneVO("Etc/GMT", "GMT", "(GMT) Coordinated Universal Time", "GMT"));
		tz.put("Etc/GMT-12", new TimeZoneVO("Etc/GMT-12", "GMT +12", "(GMT +12:00) Coordinated Universal Time+12", ""));
		tz.put("Etc/GMT+11", new TimeZoneVO("Etc/GMT+11", "GMT -11", "(GMT -11:00) Coordinated Universal Time-11", ""));
		tz.put("America/Caracas", new TimeZoneVO("America/Caracas", "Venezuela Standard Time", "(GMT -04:30) Caracas", ""));
		tz.put("Asia/Vladivostok", new TimeZoneVO("Asia/Vladivostok", "Vladivostok Standard Time", "(GMT +10:00) Vladivostok", ""));
		tz.put("Australia/Perth", new TimeZoneVO("Australia/Perth", "W. Australia Standard Time", "(GMT +08:00) Perth", ""));
		tz.put("Africa/Lagos", new TimeZoneVO("Africa/Lagos", "W. Central Africa Standard Time", "(GMT +01:00) West Central Africa", ""));
		tz.put("Europe/Berlin", new TimeZoneVO("Europe/Berlin", "W. Europe Standard Time", "(GMT +01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna", ""));
		tz.put("Asia/Tashkent", new TimeZoneVO("Asia/Tashkent", "West Asia Standard Time", "(GMT +05:00) Tashkent", ""));
		tz.put("Pacific/Port_Moresby", new TimeZoneVO("Pacific/Port_Moresby", "West Pacific Standard Time", "(GMT +10:00) Guam, Port Moresby", ""));
		tz.put("Asia/Yakutsk", new TimeZoneVO("Asia/Yakutsk", "Yakutsk Standard Time", "(GMT +09:00) Yakutsk", ""));
		
		return tz;
	}
}
