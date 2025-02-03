package com.serliunx.ddns.config.listener;

import com.serliunx.ddns.config.ConfigListener;
import com.serliunx.ddns.config.Configuration;
import com.serliunx.ddns.constant.ConfigurationKeys;
import com.serliunx.ddns.support.Assert;
import com.serliunx.ddns.support.ipprovider.ScheduledProvider;

import java.util.Collection;
import java.util.Collections;

/**
 * 配置监听器：IP更新间隔变动
 * <li> 刷新间隔发生变更时通知定时器结束并重新开始计时.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/3
 */
public final class IpRefreshIntervalListener implements ConfigListener {

	private final ScheduledProvider scheduledProvider;

	public IpRefreshIntervalListener(ScheduledProvider scheduledProvider) {
		Assert.notNull(scheduledProvider);
		this.scheduledProvider = scheduledProvider;
	}

	@Override
	public Collection<String> interestedIn() {
		return Collections.singletonList(ConfigurationKeys.KEY_TASK_REFRESH_INTERVAL_IP);
	}

	@Override
	public void onChanged(Configuration configuration, String key, Object oldVal, Object newVal) throws Exception {
		if (key == null
				|| !key.equals(ConfigurationKeys.KEY_TASK_REFRESH_INTERVAL_IP)
				|| oldVal == null
				|| newVal == null
				|| oldVal.equals(newVal))
			return;
		Long newInterval = configuration.getLong(ConfigurationKeys.KEY_TASK_REFRESH_INTERVAL_IP);
		scheduledProvider.changeTimePeriod(newInterval);
	}
}
