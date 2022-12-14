package com.amx.jax.rest;

public interface IMetaRequestOutFilter<T extends RequestMetaInfo> {

	/**
	 * @deprecated in favor of
	 *             {@link IMetaRequestOutFilter#outFilter(RequestMetaInfo)}
	 * @param meta
	 */
	@Deprecated
	public default T exportMeta() {
		@SuppressWarnings("unchecked")
		T requestMeta = (T) new RequestMetaInfo();
		outFilter(requestMeta);
		return requestMeta;
	};

	/**
	 * Meta Data Info you want to send with outgoing request
	 * 
	 * @param meta
	 */
	public void outFilter(T requestMeta);

}
