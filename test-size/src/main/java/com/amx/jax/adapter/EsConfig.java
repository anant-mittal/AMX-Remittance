package com.amx.jax.adapter;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;

import com.amx.utils.ArgUtil;

@Configuration
public class EsConfig extends AbstractFactoryBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(EsConfig.class);

	@Value("${spring.data.elasticsearch.cluster-nodes}")
	private String clusterNodes;

	@Value("${spring.data.elasticsearch.cluster-name}")
	private String clusterName;

	@Value("${elasticsearch.host}")
	private String clusterHost;

	@Value("${elasticsearch.port}")
	private String clusterPort;

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
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(
							new HttpHost(clusterHost, ArgUtil.parseAsInteger(clusterPort), "http")
					)
			);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return restHighLevelClient;
	}
}
