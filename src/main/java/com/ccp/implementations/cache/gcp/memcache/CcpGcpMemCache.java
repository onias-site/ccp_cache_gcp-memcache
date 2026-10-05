package com.ccp.implementations.cache.gcp.memcache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

/**
 * DI provider that exposes GcpMemCache as the CcpCache implementation on Google Cloud Platform.
 */
public class CcpGcpMemCache implements CcpInstanceProvider<CcpCache> {

	/**
	 * Builds the Memcache implementation of {@code CcpCache}.
	 * @return a new {@code GcpMemCache}
	 */
	public CcpCache getInstance() { 
		GcpMemCache gcpMemCache = new GcpMemCache();
		return gcpMemCache; 
	}

}
 