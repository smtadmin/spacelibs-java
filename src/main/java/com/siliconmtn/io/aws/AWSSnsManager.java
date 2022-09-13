package com.siliconmtn.io.aws;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Getter
@Setter
public class AWSSnsManager {
	public static final Region DEFAULT_S3_REGION = Region.US_WEST_2;

	private Region region;
	private String accessKeyId;
	private String secretAccessKey;

	AWSSnsManager(String accessKeyId, String secretAccessKey) {
		this(accessKeyId, secretAccessKey, DEFAULT_S3_REGION);
	}
	
	public AWSSnsManager(String accessKeyId, String secretAccessKey, Region region) {
		this.region = region;
		this.accessKeyId = accessKeyId;
		this.secretAccessKey = secretAccessKey;
	}

	public SnsClient buildClient() {
		// create aws credentials obj
		AwsBasicCredentials basicCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

		// create aws credentials provider, passing it the creds obj
		StaticCredentialsProvider credsProvider = StaticCredentialsProvider.create(basicCreds);
		return SnsClient.builder().region(region).credentialsProvider(credsProvider).build();
	}
}
