package com.siliconmtn.io.http;

import java.security.SecureRandom;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/****************************************************************************
 * <b>Title:</b> TrustCerts.java <br>
 * <b>Project:</b> template-director <br>
 * <b>Description:</b> Class to help with a workaround for untrusted certs <br>
 * <b>Copyright:</b> Copyright (c) 2023 <br>
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author etewa
 * @version 1.x
 * @since Mar 23, 2023 <b>updates:</b>
 * 
 ****************************************************************************/

public class TrustCerts {

	/**
	 * Create a trust manager that does not validate certificate chains
	 * 
	 * @return
	 */
	public SSLContext getTrustCerts() {

		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, getTrustManagers(), new SecureRandom());
		} catch (Exception e) {	/* Nothing to do */ }

		return sc;
	}
	
	/**
	 * Gets the trust managers for the certs
	 * @return Array of trust managers
	 */
	protected TrustManager[] getTrustManagers() {
		return new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) 
			throws CertificateExpiredException, CertificateNotYetValidException {
				certs[0].checkValidity();
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) 
			throws CertificateExpiredException, CertificateNotYetValidException {
				certs[0].checkValidity();
			}
		} };

	}

}
