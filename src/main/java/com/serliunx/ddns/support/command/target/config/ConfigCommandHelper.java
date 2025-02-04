package com.serliunx.ddns.support.command.target.config;

import com.serliunx.ddns.config.Configuration;
import org.jline.reader.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * config 指令相关工具方法
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
final class ConfigCommandHelper {

	/**
	 * 获取所有配置键, 作为参数返回
	 *
	 * @param configuration	配置信息
	 * @return 配置键集合
	 */
	static List<String> getArgs(Configuration configuration) {
		final Map<String, String> allKeyAndValue;
		if (configuration == null ||
				(allKeyAndValue = configuration.getAllKeyAndValue()) == null) {
			return new ArrayList<>();
		}
		return new ArrayList<>(allKeyAndValue.keySet());
	}

	/**
	 * 补全配置键
	 *
	 * @param configuration	配置键
	 * @param currentWord 	当前输入内容
	 * @param candidates	候选参数列表
	 */
	static void completeConfigKeys(Configuration configuration, String currentWord, List<Candidate> candidates) {
		final Map<String, String> allKeyAndValue;
		if (configuration == null ||
				(allKeyAndValue = configuration.getAllKeyAndValue()) == null) {
			return;
		}
		allKeyAndValue.keySet().forEach(k -> {
			if (k.startsWith(currentWord)) {
				candidates.add(new Candidate(k));
			}
		});
	}
}
