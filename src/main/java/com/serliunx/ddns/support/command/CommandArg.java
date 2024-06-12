package com.serliunx.ddns.support.command;

/**
 * 指令参数信息包装
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public class CommandArg {

	/**
	 * 参数名称
	 */
	private final String arg;

	/**
	 * 参数说明
	 */
	private final String description;

	public CommandArg(String arg, String description) {
		this.arg = arg;
		this.description = description;
	}

	public String getArg() {
		return arg;
	}

	public String getDescription() {
		return description;
	}
}
