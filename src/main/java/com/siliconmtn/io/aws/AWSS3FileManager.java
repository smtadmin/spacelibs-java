package com.siliconmtn.io.aws;

// JDK 11.x
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Apache Commons 3.x
import org.apache.commons.io.IOUtils;

// Space Libs 1.x
import com.siliconmtn.io.BaseFile;

import lombok.Getter;
// Lombok 1.x
import lombok.extern.log4j.Log4j2;

// Amazon SDK 2.x
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
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
@Getter
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
	protected S3Client buildClient() {
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
	public List<BaseFile> listBucket(String bucketName, String prefix) {
		return listBucket(bucketName, prefix, DEFAULT_DELIMITER);
	}
	
	
	
	/**
	 * Returns a list of all object keys in the provided bucket using the provided prefix. The
	 * prefix and delimiter are concatenated prior to sending the request to S3.
	 * @param bucketName
	 * @param prefix
	 * @param delimiter
	 * @return
	 */
	public List<BaseFile> listBucket(String bucketName, String prefix, String delimiter) {
		// If key prefix doesn't end with the delimiter, add it to the delimiter before querying S3.
		if (! prefix.endsWith(delimiter)) prefix = prefix + delimiter;
		
		/* 2024-08-22 DBargerhuff
		 * Note: In the past we built the ListObjectsRequest object using the .delimiter(delimiter)
		 * method (.builder().bucket(bucketName).prefix(prefix + delimiter).delimiter(delimiter)).
		 * However, this only returns object keys that are in the 'root' of the prefix. It doesn't 
		 * return object keys 'below' that (e.g. 'recursively'). I changed the syntax to correct that.
		 */
		try (S3Client client = buildClient()) {
			ListObjectsRequest req = ListObjectsRequest
					.builder()
					.bucket(bucketName)
					.prefix(prefix)
					.build();
			
			List<BaseFile> data = new ArrayList<>();
			ListObjectsResponse res = client.listObjects(req);
			
			for(S3Object f : res.contents()) {
				BaseFile bf = new BaseFile();
				bf.assignFileInfo(f.key());
				
				// Get the ID
				Optional<String> id = f.getValueForField("ETag", String.class);
				if(id.isPresent()) bf.setId(id.get());
				
				// Get the last modified
				Optional<Instant> lastMod = f.getValueForField("LastModified", Instant.class);
				if(lastMod.isPresent()) bf.setUpdateDate(lastMod.get());
				
				// Get the file size
				Optional<Long> size = f.getValueForField("Size", Long.class);
				if(size.isPresent()) bf.setSize(size.get());
				
				// Get the owner info
				Optional<Owner> owner = f.getValueForField("Owner", Owner.class);
				if(owner.isPresent()) {
					Owner o = owner.get();
					bf.setOwner(o.displayName());
					bf.setOwnerId(o.id());
				}
				
				data.add(bf);
			}

			return data;
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
			SdkHttpResponse sdkRes = res.sdkHttpResponse();
			if (sdkRes.statusCode() != 200) {
				String status = sdkRes.statusText().toString();
				int statusCode = sdkRes.statusCode();
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
			
		} catch (IOException | NullPointerException e) {
			log.error("Failed to acquire file " + file + " from bucket " + bucketName, e);
			resArray = new byte[0];
		} finally {
			closeResponse(res);
		}
		
		return resArray;
	}
	
	/**
	 * Deletes a file in the s3 bucket
	 * @param bucket S3 Bucket to delete the file from
	 * @param key File key or name to delete
	 */
	public void removeS3File(String bucket, String key) {
		try (S3Client client = buildClient()) {
			DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
 
			client.deleteObject(request);
		}
	}
	
	/**
	 * Close the supplied response
	 * @param res
	 */
	protected void closeResponse(ResponseInputStream<GetObjectResponse> res) {
		if (res == null) return;
		try {
			res.close();
		} catch (Exception e) {
			log.error("Failed to close response stream", e);
		}
	}

}
