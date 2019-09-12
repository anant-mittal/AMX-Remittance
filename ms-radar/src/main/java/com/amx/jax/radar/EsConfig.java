package com.amx.jax.radar;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import com.amx.jax.AppContextUtil;
import com.amx.jax.AppParam;
import com.amx.utils.ArgUtil;

@SuppressWarnings("rawtypes")
@Configuration
public class EsConfig extends AbstractFactoryBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsConfig.class);

	@Value("${spring.data.elasticsearch.cluster-nodes}")
	private String clusterNodes;

	@Value("${spring.data.elasticsearch.cluster-name}")
	private String clusterName;

	@Value("${elasticsearch.scheme}")
	private String clusterScheme;

	@Value("${elasticsearch.host}")
	private String clusterHost;

	@Value("${elasticsearch.port}")
	private String clusterPort;

	@Value("${elasticsearch.url}")
	private String clusterUrl;

	@Value("${elasticsearch.username}")
	private String clusterUsername;

	@Value("${elasticsearch.password}")
	private String clusterPass;

	@Value("${elasticsearch.enabled}")
	private boolean enabled;

	private HttpHeaders basicAuthHeader;

	private RestHighLevelClient restHighLevelClient;

	@Override
	public void destroy() {
		try {
			if (restHighLevelClient != null) {
				restHighLevelClient.close();
			}
		} catch (final Exception e) {
			LOGGER.error("Error closing ElasticSearch client: ", e);
		}
	}

	@Override
	public Class<RestHighLevelClient> getObjectType() {
		return RestHighLevelClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public RestHighLevelClient createInstance() {
		return buildClient();
	}

	private RestHighLevelClient buildClient() {
		try {

			RestClientBuilder builder = RestClient.builder(
					new HttpHost(clusterHost, ArgUtil.parseAsInteger(clusterPort), clusterScheme));

			if (!ArgUtil.isEmpty(clusterPass)) {
				final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(clusterUsername, clusterPass));

				builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(
							HttpAsyncClientBuilder httpClientBuilder) {
						return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					}
				});
			}

			restHighLevelClient = new RestHighLevelClient(builder);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return restHighLevelClient;
	}

	public String getClusterHost() {
		return clusterHost;
	}

	public String getClusterPort() {
		return clusterPort;
	}

	public String getClusterUrl() {
		return clusterUrl;
	}

	public static String indexName(String name) {
		return String.format("%s-%s-%s", AppParam.APP_ENV.getValue(), AppContextUtil.getTenant(), name).toLowerCase();
	}

	public String getClusterUsername() {
		return clusterUsername;
	}

	public String getClusterPass() {
		return clusterPass;
	}

	public HttpHeaders getBasicAuthHeader() {
		if (basicAuthHeader == null && !ArgUtil.isEmpty(clusterPass)) {
			basicAuthHeader = new HttpHeaders() {
				private static final long serialVersionUID = 1L;
				{
					String auth = clusterUsername + ":" + clusterPass;
					byte[] encodedAuth = Base64.encodeBase64(
							auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
		}
		return basicAuthHeader;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
