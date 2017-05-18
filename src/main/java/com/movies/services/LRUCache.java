package com.movies.services;

import java.util.LinkedHashMap;

/**
 * For the LRUCache we can use the ReadWriteLock to read lock for get, and write
 * lock for put
 */
public class LRUCache<K, V> {
	private LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
	private int limit;

	public LRUCache(int limit) {
		this.limit = limit;
	}

	public LRUCache() {
		this(Integer.MAX_VALUE);
	}

	public void put(K key, V val) {
		if (map.size() >= limit) {
			K keyToRemove = getFirtKeyRemovable();
			map.remove(keyToRemove);
		}
		map.put(key, val);
	}

	public int size() {
		return map.size();
	}

	public V get(K key) {
		V oldValue = map.remove(key);
		if (oldValue != null) {
			map.put(key, oldValue);
		}
		return oldValue;
	}

	public K getFirtKeyRemovable() {
		return map.entrySet().iterator().next().getKey();
	}

}
