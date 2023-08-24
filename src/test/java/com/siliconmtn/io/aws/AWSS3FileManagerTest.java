package com.siliconmtn.io.aws;

// Junit 5
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

// JDK 11.x
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// AWS Imports
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.Owner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

/****************************************************************************
 * <b>Title</b>: AWSS3FileManagerTest.java
 * <b>Project</b>: spacelibs-java
 * <b>Description: </b> Unit tests for the AWS Manager Class
 * <b>Copyright:</b> Copyright (c) 2022
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author James Camire
 * @version 3.0
 * @since Aug 27, 2022
 * @updates:
 ****************************************************************************/
class AWSS3FileManagerTest {
	
	@Mock
	AwsBasicCredentials abc;
	
	@Mock
	StaticCredentialsProvider scp;
	
	S3Client client;
	ListObjectsResponse loRes;
	List<S3Object> fullData;
	
	@Mock
	ListObjectsRequest loReq;
	
	@BeforeEach
	public void setup(){
		client = mock(S3Client.class);
		loReq = mock(ListObjectsRequest.class);

	}

	@Test
	void testAWSS3FileManagerStringString() {
		AWSS3FileManager mgr = new AWSS3FileManager("accessKeyId","secretAccessKey");
		assertEquals("accessKeyId", mgr.getAccessKeyId());
		assertEquals("secretAccessKey", mgr.getSecretAccessKey());
		assertEquals(Region.US_WEST_2, mgr.getRegion());
	}

	@Test
	void testAWSS3FileManagerStringStringString() {
		AWSS3FileManager mgr = new AWSS3FileManager("accessKeyId","secretAccessKey", Region.AP_NORTHEAST_1.id());
		assertEquals("accessKeyId", mgr.getAccessKeyId());
		assertEquals("secretAccessKey", mgr.getSecretAccessKey());
		assertEquals(Region.AP_NORTHEAST_1, mgr.getRegion());
	}

	@Test
	void testBuildClient() {
		AWSS3FileManager mgr = new AWSS3FileManager("accessKeyId","secretAccessKey");
		assertNotNull(mgr.buildClient());
	}

	@Test
	void testListBucketStringString() {	
		
		// Add the full data
		fullData = new ArrayList<>();
		S3Object o = S3Object.builder()
				.eTag("1234")
				.build();
		
		fullData.add(o);
		loRes = ListObjectsResponse.builder().contents(fullData).build();
		
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		when(client.listObjects(any((ListObjectsRequest.class)))).thenReturn(loRes);

		assertNotNull(mgr1.listBucket("smt-juv2-xfer", "usms_jira_json"));
	}

	@Test
	void testListBucketSize() {	
		
		// Add the full data
		fullData = new ArrayList<>();
		S3Object o = S3Object.builder()
				.size(1234l)
				.build();
		
		fullData.add(o);
		loRes = ListObjectsResponse.builder().contents(fullData).build();
		
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		when(client.listObjects(any((ListObjectsRequest.class)))).thenReturn(loRes);

		assertEquals(1, mgr1.listBucket("smt-juv2-xfer", "usms_jira_json").size());
	}

	@Test
	void testListBucketModified() {	
		
		// Add the full data
		fullData = new ArrayList<>();
		S3Object o = S3Object.builder()
				.lastModified(Instant.now())
				.build();
		
		fullData.add(o);
		loRes = ListObjectsResponse.builder().contents(fullData).build();
		
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		when(client.listObjects(any((ListObjectsRequest.class)))).thenReturn(loRes);

		assertEquals(1, mgr1.listBucket("smt-juv2-xfer", "usms_jira_json").size());
	}
	
	@Test
	void testListBucketOwner() {	
		
		// Add the full data
		fullData = new ArrayList<>();
		S3Object o = S3Object.builder()
				.owner(Owner.builder().id("1234").build())
				.build();
		
		fullData.add(o);
		loRes = ListObjectsResponse.builder().contents(fullData).build();
		
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		
		doReturn(client).when(mgr1).buildClient();
		
		when(client.listObjects(any((ListObjectsRequest.class)))).thenReturn(loRes);

		assertEquals(1, mgr1.listBucket("smt-juv2-xfer", "usms_jira_json").size());
	}
	
	@Test
	void testProcessBucketPutObject() {
		SdkHttpResponse res = SdkHttpResponse.builder()
				.statusCode(200)
				.statusText("Hello World")
				.build();
		
		PutObjectResponse por = mock(PutObjectResponse.class);
		
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		
		when(client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(por);
		when(por.sdkHttpResponse()).thenReturn(res);

		assertDoesNotThrow(() -> mgr1.processBucketPutObject(new byte[0], "Key", "Name"));
	}
	
	@Test
	void testProcessBucketPutObjectError() {
		SdkHttpResponse res = SdkHttpResponse.builder()
				.statusCode(400)
				.statusText("Hello World")
				.build();
		
		PutObjectResponse por = mock(PutObjectResponse.class);
		
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		
		when(client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(por);
		when(por.sdkHttpResponse()).thenReturn(res);

		assertThrows(IOException.class, () -> mgr1.processBucketPutObject(new byte[0], "Key", "Name"));
	}

	@Test
	void testGetBucketObject() {
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		when(client.getObject(any(GetObjectRequest.class))).thenReturn(null);
		assertEquals(0, mgr1.getBucketObject("name", "file").length);

		byte [] ba = "test".getBytes();
		InputStream is = new ByteArrayInputStream(ba);
		AbortableInputStream ais = AbortableInputStream.create(is, () -> { });
		GetObjectResponse gor = GetObjectResponse.builder().build();
		ResponseInputStream<GetObjectResponse> res = new ResponseInputStream<>(gor, ais);
		when(client.getObject(any(GetObjectRequest.class))).thenReturn(res);
		assertEquals(ba.length, mgr1.getBucketObject("name", "file").length);
	}

	@Test
	void testCloseResponse() {
		InputStream is = new ByteArrayInputStream("test".getBytes());
		AbortableInputStream ais = AbortableInputStream.create(is, () -> { });
		GetObjectResponse gor = GetObjectResponse.builder().build();
		ResponseInputStream<GetObjectResponse> res = new ResponseInputStream<>(gor, ais);
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		assertDoesNotThrow(() -> mgr.closeResponse(res));
	}

	@Test
	void testRemoveS3File() {
		AWSS3FileManager mgr = new AWSS3FileManager("1234", "5678");
		AWSS3FileManager mgr1 = spy(mgr);
		doReturn(client).when(mgr1).buildClient();
		when(client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(null);
		
		assertDoesNotThrow(() -> mgr1.removeS3File("bucket","key"));
	}
}
