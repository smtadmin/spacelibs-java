package com.siliconmtn.io.aws;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

class AWSSNSManagerTest {

	@Mock
	AwsBasicCredentials abc;
	
	@Mock
	StaticCredentialsProvider scp;

	SnsClient client;

	@BeforeEach
	public void setup(){
		client = mock(SnsClient.class);
	}

	@Test
	void testAWSS3FileManagerStringString() {
		AWSSnsManager mgr = new AWSSnsManager("accessKeyId","secretAccessKey");
		assertEquals("accessKeyId", mgr.getAccessKeyId());
		assertEquals("secretAccessKey", mgr.getSecretAccessKey());
		assertEquals(Region.US_WEST_2, AWSSnsManager.DEFAULT_S3_REGION);
	}

	@Test
	void testAWSS3FileManagerStringStringString() {
		AWSSnsManager mgr = new AWSSnsManager("accessKeyId","secretAccessKey", Region.AP_NORTHEAST_1);
		assertEquals("accessKeyId", mgr.getAccessKeyId());
		assertEquals("secretAccessKey", mgr.getSecretAccessKey());
		assertEquals(Region.AP_NORTHEAST_1, mgr.getRegion());
	}

	@Test
	void testBuildClient() {
		AWSSnsManager mgr = new AWSSnsManager("accessKeyId","secretAccessKey");
		assertNotNull(mgr.buildClient());
	}
}
