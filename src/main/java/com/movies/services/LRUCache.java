package com.movies.services;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache<K, V> {
	private LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
	private int limit;

	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();

	public LRUCache(int limit) {
		this.limit = limit;
	}

	public LRUCache() {
		this(Integer.MAX_VALUE);
	}

	public void put(K key, V val) {
		writeLock.lock();
		try {
			if (map.size() >= limit) {
				K keyToRemove = getFirtKeyRemovable();
				map.remove(keyToRemove);
			}
			map.put(key, val);
		} finally {
			writeLock.unlock();
		}
	}

	public int size() {
		readLock.lock();
		try {
			return map.size();
		} finally {
			readLock.unlock();
		}
	}

	public V get(K key) {
		readLock.lock();
		try {

			V oldValue = map.remove(key);
			if (oldValue != null) {
				map.put(key, oldValue);
			}
			return oldValue;
		} finally {
			readLock.unlock();
		}

	}

	public K getFirtKeyRemovable() {
		readLock.lock();
		try {
			if (!map.isEmpty()) {
				return map.entrySet().iterator().next().getKey();
			} else {
				return null;
			}

		} finally {
			readLock.unlock();
		}
	}

}
