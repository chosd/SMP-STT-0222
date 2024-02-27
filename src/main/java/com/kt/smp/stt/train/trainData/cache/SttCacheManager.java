/**
 * cms version 1.0
 *
 *  Copyright ⓒ 2017 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @author kt
 * @since 2018. 7. 31.
 * @version 1.0
 * @see
 * @Copyright © 2017 By KT corp. All rights reserved.
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *  수정일               수정자                수정내용
 *  -------------        ----------       -------------------------
 *  2018. 7. 31.         	 hmook            최초생성
 *
 * </pre>
 **/

package com.kt.smp.stt.train.trainData.cache;

import org.ehcache.UserManagedCache;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @title 학습데이터 캐시 매니저
 * @since 2022.05.08
 * @author jieun.chang
 * @see <pre></pre>
 */
public class SttCacheManager {

	public final static int CACHEMAXROWS = 1000;

	private UserManagedCache<String, SttTrainDataBulkCache> trainDataCache;
	private static Object lockObj = new Object();

	/**
	 * <pre>
	 * Constructor of SttCacheManager class
	 * </pre>
	 *
	 */
	private SttCacheManager() {

		ResourcePoolsBuilder resourcePoolBuilder = null;

		resourcePoolBuilder = ResourcePoolsBuilder
				.newResourcePoolsBuilder()
				.heap(CACHEMAXROWS, EntryUnit.ENTRIES);

		setTrainDataCache(
				UserManagedCacheBuilder.newUserManagedCacheBuilder(String.class, SttTrainDataBulkCache.class)
						.withEventExecutors(Executors.newSingleThreadExecutor(), Executors.newCachedThreadPool())
						.withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.HOURS)))
						.withResourcePools(resourcePoolBuilder)
						.build(true));
	}

	/**
	 * 싱글톤 유지를 위한 instance 생성
	 */
	private static class Holder {
		public static final SttCacheManager INSTANCE = new SttCacheManager();
	}

	/**
	 * <pre>
	 * getInstance
	 * </pre>
	 *
	 * @return
	 */
	public static SttCacheManager getInstance() {
		return Holder.INSTANCE;
	}

	public void initializeTrainDataCache(String key) {
		if (getTrainDataCache().containsKey(key)) {
			this.removeTrainDataCache(key);
		}
	}

	/**
	 * <pre>
	 * 캐시 조회
	 * </pre>
	 *
	 * @param key
	 * @return
	 */
	public SttTrainDataBulkCache getTrainDataCache(String key) {
		SttTrainDataBulkCache returnValue = null;

		returnValue = getTrainDataCache().get(key);
		return returnValue;
	}

	public void put(String key, SttTrainDataBulkCache value) {
		synchronized (lockObj) {
			getTrainDataCache().remove(key);
			getTrainDataCache().put(key, value);
		}
	}

	public void removeTrainDataCache(String key) {
		synchronized (lockObj) {
			getTrainDataCache().remove(key);
		}
	}

	public UserManagedCache<String, SttTrainDataBulkCache> getTrainDataCache() {
		return trainDataCache;
	}

	public void setTrainDataCache(UserManagedCache<String, SttTrainDataBulkCache> trainDataCache) {
		this.trainDataCache = trainDataCache;
	}

}
