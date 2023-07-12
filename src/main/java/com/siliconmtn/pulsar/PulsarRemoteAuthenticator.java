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
import com.siliconmtn.io.http.SMTHttpConnectionManager;
import com.siliconmtn.io.http.SMTHttpConnectionManager.HttpConnectionType;
import com.siliconmtn.io.http.TrustCerts;

import lombok.extern.log4j.Log4j2;

/**
 * @author raptor
 *
 */
@Component
@Log4j2
public class PulsarRemoteAuthenticator implements Supplier<String> {	

	private String token;
	private static final String CLIENT_ID = "client_id";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String SCOPE = "scope";
	private static final String GRANT_TYPE = "grant_type";
	private static final String ACCESS_TOKEN = "access_token";
	private static final int DAY_IN_MS = 24*60*60*1000;

	private PulsarConfig config;
	private ObjectMapper mapper;
	private SMTHttpConnectionManager manager;

	public PulsarRemoteAuthenticator(PulsarConfig config, SMTHttpConnectionManager manager) {
		this.config = config;
		this.mapper = new ObjectMapper();
		this.manager = manager;
		this.manager.setSslSocketFactory(new TrustCerts().getTrustCerts().getSocketFactory());
		mapper.findAndRegisterModules();

		//Load the initial token.
		updateToken();
	}

	@Override
	public String get() {
		log.info("Requesting Token");
		return token;
	}

	/**
	 * curl -k --location --request POST <KEY_CLOAK_URL> 
	 * --header 'Content-Type: application/x-www-form-urlencoded' 
	 * --data-urlencode "client_id=svc<CLIENT_ID>" 
	 * --data-urlencode "client_secret=<CLIENT_SECRET>" 
	 * --data-urlencode 'scope=email' 
	 * --data-urlencode 'grant_type=client_credentials'
	 */
	@Scheduled(fixedDelay = DAY_IN_MS)
	public void updateToken() {
		log.info("Updating Token");
		Map<String, Object> postBody = new HashMap<>();
		postBody.put(CLIENT_ID, config.getClientNpeId());
		postBody.put(CLIENT_SECRET, config.getClientNPESecret());
		postBody.put(SCOPE, "email");
		postBody.put(GRANT_TYPE, "client_credentials");

		manager.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			byte [] data = manager.getRequestData(config.getKeyCloakUrl(), postBody, HttpConnectionType.POST);
			log.info("Retrieved Data for Pulsar Token");
			String s = new String(data);
			log.info(s);
			JsonNode g = mapper.readTree(s);
			token = g.get(ACCESS_TOKEN).asText();
			
		} catch(Exception e) {
			log.error("There was a problem retrieving the JWT Token for Pulsar", e);
		}
	}
}
