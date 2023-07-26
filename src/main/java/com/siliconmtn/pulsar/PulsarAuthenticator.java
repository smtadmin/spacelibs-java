/**
 * 
 */
package com.siliconmtn.pulsar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siliconmtn.data.text.StringUtil;
import com.siliconmtn.io.http.SMTHttpConnectionManager;
import com.siliconmtn.io.http.SMTHttpConnectionManager.HttpConnectionType;

import lombok.extern.log4j.Log4j2;

/**
 * <b>Title:</b> PulsarAuthenticator.java
 * <b>Project:</b> SpaceLibs-Java
 * <b>Description:</b> Pulsar Authentication Supplier that handles JWT token lifecycle
 *
 * <b>Copyright:</b> 2023
 * <b>Company:</b> Silicon Mountain Technologies
 *
 * @author raptor
 * @version 1.0
 * @since Jul 13, 2023
 * @updates
 *
 */
@Component
@Log4j2
public class PulsarAuthenticator implements Supplier<String> {

	//Constants
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String SCOPE = "scope";
	public static final String GRANT_TYPE = "grant_type";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String OAUTH_IDENTIFIER = "pulsar";

	//Autowired Member Variables
	protected PulsarConfig config;

	//Instance Variables
	private String token;
	protected ObjectMapper mapper;
	protected SMTHttpConnectionManager manager;

	public PulsarAuthenticator(PulsarConfig config) {
		this.config = config;
		this.manager = new SMTHttpConnectionManager();
		prepareManager(this.manager);
		this.mapper = new ObjectMapper();
		this.mapper.findAndRegisterModules();
		updateToken();
	}

	/**
	 * Helper method that manages setting up the SSL and Header variables for the
	 * SMTHttpConnectionManager.
	 * @param manager
	 */
	protected void prepareManager(SMTHttpConnectionManager manager) {
		manager.addRequestHeader(SMTHttpConnectionManager.REQUEST_PROPERTY_CONTENT_TYPE, "application/x-www-form-urlencoded");
	}

	/**
	 * Override method responsible for returning the JWT Token.
	 */
	@Override
	public String get() {
		log.debug("Pulsar JWT Token is being requested");
		return token;
	}

	/**
	 * Responsible for determining manner of token updating for the PulsarClient.
	 *
	 * Supports both pre-generated tokens as well as a KeyCloak NPE via
	 * OAuth.
	 *
	 * If enabled on the application, also supports scheduling via a Spring Cron expression
	 */
	@Scheduled(cron = "${pulsar.cronSchedule:-}")
	public void updateToken() {
		log.debug("Populating Pulsar JWT Token");
		if(config.hasNPEAuth()) {
			retrieveNPEJWTToken(config);
		} else if (config.hasJWTAuth()) {
			token = StringUtil.defaultString(config.getAdminJWT(), config.getClientJWT());
		} else {
			//Authenticator requires at least an empty string to avoid an NPE.
			token = "";
		}
		log.debug("Populated Pulsar JWT Token");
	}

	/**
	 * Responsible for retrieving the JWT Token authorized by the given ClientRegistration config.
	 * The Curl command being generated and executed is as follows.
	 *
	 * curl -k --location --request POST <KEY_CLOAK_URL> 
	 * --header 'Content-Type: application/x-www-form-urlencoded' 
	 * --data-urlencode "client_id=svc<CLIENT_ID>" 
	 * --data-urlencode "client_secret=<CLIENT_SECRET>" 
	 * --data-urlencode 'scope=email' 
	 * --data-urlencode 'grant_type=client_credentials'
	 * @param reg
	 */
	protected void retrieveNPEJWTToken(PulsarConfig config) {
		token = null;

		if(config == null) {
			return;
		}
		Map<String, Object> postBody = new HashMap<>();
		postBody.put(CLIENT_ID, config.getClientId());
		postBody.put(CLIENT_SECRET, config.getClientSecret());
		postBody.put(SCOPE, config.getScope());
		postBody.put(GRANT_TYPE, config.getAuthorizationGrantType());

		try {
			byte [] data = manager.getRequestData(config.getTokenUri(), postBody, HttpConnectionType.POST);
			log.debug("Received Pulsar JWT Token");
			JsonNode g = mapper.readTree(data);
			token = g.get(ACCESS_TOKEN).asText();
		} catch(Exception e) {
			log.error("There was a problem retrieving the Pulsar JWT Token", e);
		}
	}
}
