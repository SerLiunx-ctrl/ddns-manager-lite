package com.serliunx.ddns.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制台样式助手
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/2/4
 */
public final class ConsoleStyleHelper {

	/**
	 * ANSI 控制台经典样式编码映射表
	 */
	private static final Map<String, String> CLASSIC_STYLE_MAP = new HashMap<>();

	static {
		// 格式
		CLASSIC_STYLE_MAP.put("&r", "\033[0m"); // 重置
		CLASSIC_STYLE_MAP.put("&l", "\033[1m"); // 加粗或高亮

		// 颜色
		CLASSIC_STYLE_MAP.put("&0", "\033[30m"); // 黑色
		CLASSIC_STYLE_MAP.put("&1", "\033[31m"); // 红色
		CLASSIC_STYLE_MAP.put("&2", "\033[32m"); // 绿色
		CLASSIC_STYLE_MAP.put("&3", "\033[33m"); // 黄色
		CLASSIC_STYLE_MAP.put("&4", "\033[34m"); // 蓝色
		CLASSIC_STYLE_MAP.put("&5", "\033[35m"); // 品红
		CLASSIC_STYLE_MAP.put("&6", "\033[36m"); // 青色
		CLASSIC_STYLE_MAP.put("&7", "\033[37m"); // 白色
	}

	/**
	 * 格式化输出, 支持颜色代码
	 *
	 * @param format	格式
	 * @param args		参数
	 */
	public static void coloredPrintf(String format, final Object... args) {
		if (!format.endsWith("%n"))
			format = format + "%n";

		if (!format.endsWith("&r"))
			format = format + "&r";

		System.out.printf(replaceStyleCode(format), args);
	}

	/**
	 * 替换样式代码
	 *
	 * @param original	原始文本
	 * @return	替换后的文本
	 */
	private static String replaceStyleCode(String original) {
		Set<Map.Entry<String, String>> entries = CLASSIC_STYLE_MAP.entrySet();
		for (Map.Entry<String, String> entry : entries) {
			original = original.replace(entry.getKey(), entry.getValue());
		}
		return original;
	}
}
