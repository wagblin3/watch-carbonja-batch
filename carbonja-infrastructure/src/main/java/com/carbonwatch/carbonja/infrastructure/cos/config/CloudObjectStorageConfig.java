package com.carbonwatch.carbonja.infrastructure.cos.config;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.ClientConfigurationFactory;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.BucketLifecycleConfiguration;
import com.ibm.cloud.objectstorage.services.s3.model.lifecycle.LifecycleFilter;
import com.ibm.cloud.objectstorage.services.s3.model.lifecycle.LifecyclePrefixPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URL;
import java.util.Arrays;

@Configuration
@Profile({ "local", "cloud" })
public class CloudObjectStorageConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(CloudObjectStorageConfig.class);

	private static final String RULE_ID = "Delete rule";

	@Value("${cos.endpoint}")
	private URL endpoint;

	@Value("${cos.location}")
	private String location;

	// Only for COS HMAC v2
	@Value("${cos.iam.endpoint:}")
	private String cosIamEndpoint;

	// Only for COS HMAC v2
	@Value("${apikey}")
	private String apiKey;

	// Only for COS HMAC v2
	@Value("${resource_instance_id}")
	private String serviceInstanceId;

	// Only for COS HMAC v4
	@Value("${cos_hmac_keys_access_key_id}")
	private String accessKey;

	// Only for COS HMAC v4
	@Value("${cos_hmac_keys_secret_access_key}")
	private String secretKey;

	@Value("${cos.timeout}")
	private int cosTimeout;

	@Value("${cos.archive.prefix}")
	private String prefix; // ie all objects starts with "archive"

	@Value("${cos.archive.expiration.days:30}") // files are kept for 30 days by default
	private int expirationDays;

	@Value("${cos.archive.expiration.enabled:false}")
	private boolean expirationEnabled;

	@Value("${application.bucket-name}")
	private String bucketName;

	@Bean
	AmazonS3 builder() {

		LOGGER.trace("CloudObjectStorageConfig: endpoint {}, location {}, cosIamEndpoint {}", endpoint, location,
				cosIamEndpoint);

		final AmazonS3 s3Client;
		if (cosIamEndpoint.equals("")) {
			// For COS HMAC v4
			s3Client = AmazonS3ClientBuilder.standard() //
					.withEndpointConfiguration(new EndpointConfiguration(endpoint.toString(), location)) //
					.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))) // Only
																														// for
																														// COS
																														// HMAC
																														// v4
					.withPathStyleAccessEnabled(false) // Specific for COS HMAC v4 buckets
					.withClientConfiguration(createClientConfig()) //
					.build();
		} else {
			// For COS HMAC v2
			s3Client = AmazonS3ClientBuilder.standard() //
					.withEndpointConfiguration(new EndpointConfiguration(endpoint.toString(), location)) //
					.withCredentials(
							new AWSStaticCredentialsProvider(new BasicIBMOAuthCredentials(apiKey, serviceInstanceId))) //
					.withPathStyleAccessEnabled(true) // specific for HMAC v2 Buckets
					.withIAMEndpoint(cosIamEndpoint) // Only for COS HMAC v2
					.withPathStyleAccessEnabled(true) //
					.withClientConfiguration(createClientConfig()) //
					.build();
		}

		if (expirationEnabled) {
			if (!isExpirationLifecycleConfigurationPresent(s3Client.getBucketLifecycleConfiguration(bucketName))) {
				LOGGER.debug("Rule was not found : create it");
				// Use the client to set the LifecycleConfiguration on the bucket.
				s3Client.setBucketLifecycleConfiguration(bucketName, createExpirationRule(prefix, expirationDays));
			}
		}
		return s3Client;
	}

	/**
	 * Check if rule already exist in the bucket lifecycle configuration rules
	 *
	 * @param lifecycleConfiguration the bucket lifecycle configuration
	 *
	 * @return true if rule with this ID already exist
	 */
	private boolean isExpirationLifecycleConfigurationPresent(
			final BucketLifecycleConfiguration lifecycleConfiguration) {
		return lifecycleConfiguration != null && lifecycleConfiguration.getRules() != null
				&& lifecycleConfiguration.getRules().stream().anyMatch(r -> RULE_ID.equals(r.getId()));
	}

	/**
	 * Define a rule for expiring items in a bucket
	 *
	 * @param prefix         prefix for objects affected by this rule
	 * @param expirationDays number of days to keep objects in the bucket before
	 *                       deletion
	 * @return a BucketLifecycleConfiguration
	 */
	private BucketLifecycleConfiguration createExpirationRule(final String prefix, final int expirationDays) {

		final BucketLifecycleConfiguration.Rule rule = new BucketLifecycleConfiguration.Rule() //
				.withId(RULE_ID) //
				.withFilter(new LifecycleFilter(new LifecyclePrefixPredicate(prefix))) //
				.withExpirationInDays(expirationDays) //
				.withStatus(BucketLifecycleConfiguration.ENABLED);

		// Add the rule to a new BucketLifecycleConfiguration.
		return new BucketLifecycleConfiguration().withRules(Arrays.asList(rule));
	}

	private ClientConfiguration createClientConfig() {
		return new ClientConfigurationFactory().getConfig().withConnectionTimeout(cosTimeout);
	}

}