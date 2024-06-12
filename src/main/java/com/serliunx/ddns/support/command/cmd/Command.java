package com.serliunx.ddns.support.command.cmd;

import com.serliunx.ddns.support.command.CommandArg;

import java.util.List;

/**
 * 指令接口
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @since 1.0.0
 */
public interface Command {

	/**
	 * 获取指令名称
	 * @return 名称
	 */
	String getName();

	/**
	 * 指令逻辑
	 * @param args 参数
	 * @return 执行结果
	 */
	@SuppressWarnings("all")
	boolean execute(String[] args);

	/**
	 * 获取子命令
	 * @return 子命令
	 */
	Command[] getChildren();

	/**
	 * 获取指令参数信息
	 * @return 参数信息
	 */
	List<CommandArg> getArgs();
}
