package com.serliunx.ddns.config.listener;

import com.serliunx.ddns.config.ConfigListener;
import com.serliunx.ddns.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置监听器：通知变更
 * <li> 仅输出变更信息
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/3
 */
public final class NotificationConfigListener implements ConfigListener {

	private static final Logger log = LoggerFactory.getLogger(NotificationConfigListener.class);

	@Override
	public void onChanged(Configuration configuration, String key, Object oldVal, Object newVal) throws Exception {
		if (log.isDebugEnabled())
			log.debug("配置更新: 配置项 {} 由 {} 调整至 {}", key, oldVal, newVal);
	}
}
