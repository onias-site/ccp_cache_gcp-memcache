package com.ccp.implementations.cache.gcp.memcache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.cache.CcpCache;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Concrete CcpCache implementation backed by the Google App Engine Memcache service.
 * Supports get, put with TTL and delete returning the previous value.
 */
class GcpMemCache implements CcpCache {
	 
	private static MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
 
	@SuppressWarnings("unchecked")
	public Object get(String key) {

		Object object = memcacheService.get(key);
		boolean isMap = object instanceof Map;

		boolean isNotMap = false == isMap; 

		if (isNotMap) {
			return object;
		}

		Map<String, Object> map = (Map<String, Object>) object;

		CcpJsonRepresentation cachedJson = new CcpJsonRepresentation(map);
		return cachedJson;
	}


	public CcpCache put(String key, Object value, int secondsDelay) {
		Expiration expiration = Expiration.byDeltaSeconds(secondsDelay);
		boolean isCcpJsonRepresentation = value instanceof CcpJsonRepresentation;
		if(isCcpJsonRepresentation) {
			CcpJsonRepresentation jsonValue = (CcpJsonRepresentation)value;
			value = new LinkedHashMap<>(jsonValue.content);
		}
		memcacheService.put(key, value, expiration);
		return this;
	}

	public void delete(String key) {
		memcacheService.delete(key);
	}

	/**
	 * Deletes all keys in a single Memcache call instead of one call per key.
	 */
	public void deleteAll(Collection<String> keys) {
		memcacheService.deleteAll(keys);
	}

}
