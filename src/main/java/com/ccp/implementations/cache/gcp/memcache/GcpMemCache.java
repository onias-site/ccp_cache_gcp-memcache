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
 * {@code CcpCache} implementation backed by the Google App Engine Memcache service: get, put with expiration, delete
 * and batch delete. JSON values are stored as maps and read back as {@code CcpJsonRepresentation}.
 */
class GcpMemCache implements CcpCache {
	 
	/** The Memcache client shared by every instance. */
	private static MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
 
	/**
	 * Reads the value of the key; a cached map is returned as {@code CcpJsonRepresentation}.
	 * @param key the cache key
	 * @return the value, or {@code null} on a miss
	 */
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


	/**
	 * Stores the value with the expiration; a {@code CcpJsonRepresentation} is stored as a copy of its map.
	 * @param key the cache key
	 * @param value the value
	 * @param secondsDelay expiration in seconds
	 * @return this cache
	 */
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

	/**
	 * Deletes the key.
	 * @param key the cache key
	 */
	public void delete(String key) {
		memcacheService.delete(key);
	}

	/**
	 * Deletes all keys in a single Memcache call instead of one call per key.
	 * @param keys the keys to delete
	 */
	public void deleteAll(Collection<String> keys) {
		memcacheService.deleteAll(keys);
	}

}
