package com.amx.jax.api;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class AmxResponseSchemes {

	public interface ApiWrapperResponse {

		public void setTimestamp(Long timestamp);

		@ApiModelProperty(example = "1541276788518")
		public Long getTimestamp();

		@ApiModelProperty(example = "200")
		public String getStatus();

		public void setStatus(String status);

		@ApiModelProperty(example = "SUCCESS")
		public String getStatusKey();

		public void setStatusKey(String statusKey);

		@ApiModelProperty(example = "This is success message in plain english")
		public String getMessage();

		public void setMessage(String message);

		@ApiModelProperty(example = "TRANX_SUCCESS:480:KWD")
		public String getMessageKey();

		public void setMessageKey(String messageKey);
	}

	public interface ApiMetaResponse<M> extends ApiWrapperResponse {
		public M getMeta();

		public void setMeta(M meta);
	}

	public interface ApiResultsResponse<T> extends ApiWrapperResponse {
		public List<T> getResults();

		public void setResults(List<T> results);
	}

	public interface ApiDataResponse<T> extends ApiWrapperResponse {
		public T getData();

		public void setData(T data);
	}

	public interface ApiResultsMetaResponse<T, M> extends ApiResultsResponse<T>, ApiMetaResponse<M> {
	}

	public interface ApiDataMetaResponse<T, M> extends ApiDataResponse<T>, ApiMetaResponse<M> {
	}
}
