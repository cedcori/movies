package com.sothawo.securest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.movies.services.LRUCache;

public class LRUCacheTest {
	private LRUCache<Long,String> cache;
	
	@Before
	public void setUp(){
		cache = new LRUCache<Long,String>();
	}
	
	@Test
	public void GivenKeyAndValueThenSizeEqual1() {
		Long id = 1l;
		String val = "value";
		cache.put(id,val);
		assertEquals(1, cache.size());
	}
	
	@Test
	public void Given2KeyAndValueThenSizeEqual2() {
		Long id1 = 1l;
		String val1 = "value1";
		cache.put(id1,val1);
		Long id2 = 2l;
		String val2 = "value2";
		cache.put(id2,val2);
		assertEquals(2, cache.size());
	}
	
	@Test
	public void Given2KeyAndValueWhenGetSecondValueThenEqualToSecInsertedValue() {
		Long id1 = 1l;
		String val1 = "value1";
		cache.put(id1,val1);
		Long id2 = 2l;
		String val2 = "value2";
		cache.put(id2,val2);
		assertEquals(val2, cache.get(id2));
	}
	
	
	
	@Test
	public void Given4KeyAndValueThenFirstValueIsFirstRemovable() {
		Long id1 = 1l;
		String val1 = "value1";
		cache.put(id1,val1);
		Long id2 = 2l;
		String val2 = "value2";
		cache.put(id2,val2);
		Long id3 = 3l;
		String val3 = "value3";
		cache.put(id3,val3);
		Long id4 = 4l;
		String val4 = "value4";
		cache.put(id4,val4);
		assertEquals(id1, cache.getFirtKeyRemovable());
	}
	
	
	
	@Test
	public void Given4KeyAndValueWhenGetFirtValueThen2ndValueIsFirstRemovable() {
		Long id1 = 1l;
		String val1 = "value1";
		cache.put(id1,val1);
		Long id2 = 2l;
		String val2 = "value2";
		cache.put(id2,val2);
		Long id3 = 3l;
		String val3 = "value3";
		cache.put(id3,val3);
		Long id4 = 4l;
		String val4 = "value4";
		cache.put(id4,val4);
		cache.get(id1);
		assertEquals(id2, cache.getFirtKeyRemovable());
	}
	@Test
	public void Given4KeyAndValueWhenGetFirtAnd3thValueThen3thValueIsFirstRemovable() {
		Long id1 = 1l;
		String val1 = "value1";
		cache.put(id1,val1);
		Long id2 = 2l;
		String val2 = "value2";
		cache.put(id2,val2);
		Long id3 = 3l;
		String val3 = "value3";
		cache.put(id3,val3);
		Long id4 = 4l;
		String val4 = "value4";
		cache.put(id4,val4);
		cache.get(id1);
		cache.get(id2);
		assertEquals(id3, cache.getFirtKeyRemovable());
	}
	@Test
	public void Given4KeyAndValueAndLimit3WhenGetFirtValueThenNull() {
		cache = new LRUCache<Long,String>(3);
		Long id1 = 1l;
		String val1 = "value1";
		cache.put(id1,val1);
		Long id2 = 2l;
		String val2 = "value2";
		cache.put(id2,val2);
		Long id3 = 3l;
		String val3 = "value3";
		cache.put(id3,val3);
		Long id4 = 4l;
		String val4 = "value4";
		cache.put(id4,val4);
		assertNull(cache.get(id1));
	}
	


}
