package com.siliconmtn.io.api.security;

// Auth ) 3.x
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

// Lombok 1.x
import lombok.extern.log4j.Log4j2;

/****************************************************************************
 * <b>Title</b>: JWTTokenCreator.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Creates a Valid JWT Token that can be utilized in unit tests
 * for the JWT token validation
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Mar 11, 2021
 * @updates:
 ****************************************************************************/
@Log4j2
public class JWTTokenManager {

	/**
	 * Constructor not needed on a class with all static methods
	 */
	private JWTTokenManager() {
		super();
	}

	/**
	 * Retrieves a valid token to be utilized for test purposes
	 * @param issuer Name of the issuing authority
	 * @param id User's unique ID
	 * @param email User's Email Address for the token
	 * @param name User's Name
	 * @return Valid JWT token encoded with provided values
	 */
	public static String getTestToken(String issuer, String id, String email, String name) {
		String token = null;
		try {
			
		    Algorithm algorithm = Algorithm.HMAC256("secret");
		    token = JWT.create()
		        .withClaim("sub", id)
		        .withClaim("owner", issuer)
		        .withClaim("email", email)
		        .withClaim("preferred_username", name)
	    		.withIssuer(issuer)
		        .sign(algorithm);
		    
		} catch (JWTCreationException e){
		    log.error("Unable to create a valid token", e);
		}
		
		return token;
		
	}
	
	/**
	 * Validates that the provided token is from the specified issuer
	 * @param token Token to validate
	 * @param issuer Name of the issuing authority
	 * @return True if the token and issuer match.  False otherwise
	 */
	public static boolean isTokenValid(String token, String issuer) {
		boolean isSuccess = false;
		try {
			
		    Algorithm algorithm = Algorithm.HMAC256("secret");
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer(issuer)
		        .build(); 
		    
		    verifier.verify(token);
		    isSuccess = true;
		} catch (JWTVerificationException e){ /* Nothing to do */ }
		
		return isSuccess;
	}
	
	/**
	 * Validates the token first and then decodes the provided token
	 * @param token JWT token to unpack
	 * @param issuer Name of the issuing authority
	 * @return Decoded JWT Token with all valida information unpacked
	 */
	public static DecodedJWT getToken(String token, String issuer) {
		if (! isTokenValid(token, issuer)) throw new JWTDecodeException("Invalid Token");
		else return JWT.decode(token); 
	}

}
