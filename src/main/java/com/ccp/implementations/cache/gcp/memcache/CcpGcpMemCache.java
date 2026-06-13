package com.ccp.implementations.cache.gcp.memcache;

import com.ccp.dependency.injection.CcpInstanceProvider;
import com.ccp.especifications.cache.CcpCache;

/**
 * Provedor de DI que expõe GcpMemCache como implementação de CcpCache no Google Cloud Platform.
 */
public class CcpGcpMemCache implements CcpInstanceProvider<CcpCache> {

	public CcpCache getInstance() {
		GcpMemCache gcpMemCache = new GcpMemCache();
		return gcpMemCache;
	}

}
