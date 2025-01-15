package com.serliunx.ddns.support.command;

import java.util.List;

/**
 * 指令接口定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/15
 */
public interface Command {

	/**
	 * 指令执行逻辑
	 *
	 * @param args	当前指令参数
	 * @return	成功执行返回真, 否则返回假. (目前没影响)
	 */
	boolean onCommand(String[] args);

	/**
	 * 获取指令名称
	 */
	String getName();

	/**
	 * 获取子命令
	 * <li> 例: cmd c1 c2, 此时 c1为cmd的子命令
	 *
	 * @return 子命令
	 */
	List<Command> getSubCommands();

	/**
	 * 获取该指令的描述
	 */
	String getDescription();

	/**
	 * 获取该指令的用法
	 */
	String getUsage();
}
