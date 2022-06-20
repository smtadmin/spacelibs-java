package com.siliconmtn.io.aws;

// JDK 11.x
import java.io.IOException;
import java.util.List;

// Apache Commons 3.x
import org.apache.commons.io.IOUtils;

// Lombok 1.x
import lombok.extern.log4j.Log4j2;

// Amazon SDK 2.x
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

/****************************************************************************
 *<b>Title:</b> AWSS3FileManager
 *<b>Description:</b> Manager that creates S3Clients for the user it is
 *instantiated for and downloads, uploads, and views files in an S3 bucket.
 *<b>Copyright:</b> Copyright (c) 2021
 *<b>Company:</b> Silicon Mountain Technologies
 *
 * @author Eric Damschroder
 * @version 1.0
 * @since Oct 14, 2021
 ****************************************************************************/
@Log4j2
public class AWSS3FileManager {

	public static final Region DEFAULT_S3_REGION = Region.US_WEST_2;
	public static final String DEFAULT_DELIMITER = "/";
	
	private Region region;
	private String accessKeyId;
	private String secretAccessKey;
	
	/**
	 * Standard constructor
	 * @param accessKeyId
	 * @param secretAccessKey
	 */
	public AWSS3FileManager(String accessKeyId, String secretAccessKey) {
		this(accessKeyId, secretAccessKey, DEFAULT_S3_REGION.id());
	}
	
	
	/**
	 * Extra constructor should other regions ever be needed
	 * @param accessKeyId
	 * @param secretAccessKey
	 * @param region
	 */
	public AWSS3FileManager(String accessKeyId, String secretAccessKey, String strRegion) {
		this.region = Region.of(strRegion);
		this.accessKeyId = accessKeyId;
		this.secretAccessKey = secretAccessKey;
	}
	
	
	/**
	 * Builds the AWS SecretsManagerClient object from the 
	 * credentials used to instantiate this class
	 * @return
	 */
	private S3Client buildClient() {
		// create aws credentials obj
		AwsBasicCredentials basicCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

		// create aws credentials provider, passing it the creds obj
		StaticCredentialsProvider credsProvider = StaticCredentialsProvider.create(basicCreds);

		// create the S3 client 
		return S3Client
		.builder()
		.region(region)
		.credentialsProvider(credsProvider)
		.build();
	}
	
	
	/**
	 * Returns a list of all items in the provided bucket using the default delimiter
	 * @param bucketName
	 * @param prefix
	 * @return
	 */
	public List<S3Object> listBucket(String bucketName, String prefix) {
		return listBucket(bucketName, prefix, DEFAULT_DELIMITER);
	}
	
	
	
	/**
	 * Returns a list of all items in the provided bucket using the provided delimiter
	 * @param bucketName
	 * @param prefix
	 * @param delimiter
	 * @return
	 */
	public List<S3Object> listBucket(String bucketName, String prefix, String delimiter) {
		try (S3Client client = buildClient()) {
			ListObjectsRequest req = ListObjectsRequest
					.builder()
					.bucket(bucketName)
					.prefix(prefix + delimiter)
					.delimiter(delimiter)
					.build();

			ListObjectsResponse res = client.listObjects(req);
			
			return res.contents();
		}
	}
	
	
	/**
	 * Process a 'put' to the bucket.
	 * @param client
	 * @param srcFile
	 * @param destBucketKey
	 */
	public void processBucketPutObject(byte[] fileData, String destBucketKey, String bucketName) 
	throws IOException {
		try (S3Client client = buildClient()) {
			PutObjectRequest req = PutObjectRequest
				.builder()
				.bucket(bucketName)
				.key(destBucketKey)
				.build();
		
			PutObjectResponse res = client.putObject(req, RequestBody.fromBytes(fileData));
			
			if (res.sdkHttpResponse().statusCode() != 200) {
				String status = res.sdkHttpResponse().statusText().toString();
				int statusCode = res.sdkHttpResponse().statusCode();
				throw new IOException(statusCode + ": " + status);
			}
		}
	}
	
	
	/**
	 * Get the requested file from the S3 bucket. File ids will generally be in
	 * {folder}{delimiter}{filename} format
	 * @param bucketName
	 * @param fileId
	 * @return
	 */
	public byte[] getBucketObject(String bucketName, String file) {
		ResponseInputStream<GetObjectResponse> res = null;
		byte[] resArray = null;
		try (S3Client client = buildClient()) {
		
			GetObjectRequest req = GetObjectRequest
					.builder()
					.bucket(bucketName)
					.key(file)
					.build();
			
			res = client.getObject(req);

			resArray = IOUtils.toByteArray(res);
			
		} catch (IOException e) {
			log.error("Failed to acquire file " + file + " from bucket " + bucketName, e);
		} finally {
			closeResponse(res);
		}
		
		return resArray;
	}
	
	
	/**
	 * Close the supplied response
	 * @param res
	 */
	private void closeResponse(ResponseInputStream<GetObjectResponse> res) {
		if (res == null) return;
		try {
			res.close();
		} catch (Exception e) {
			log.error("Failed to close response stream", e);
		}
	}

}
