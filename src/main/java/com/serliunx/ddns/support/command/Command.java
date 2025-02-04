package com.serliunx.ddns.support.command;

import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
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

	/**
	 * 获取参数列表
	 *
	 * @return	参数
	 */
	default List<String> getArgs() {
		return new ArrayList<>();
	}

	/**
	 * 命令参数补全
	 *
	 * @param reader		Jline的LineReader{@link LineReader}
	 * @param line			当前命令行的内容
	 * @param candidates	候选参数列表
	 */
	default void onComplete(LineReader reader, ParsedLine line, int index, List<Candidate> candidates) {
		// do nothing by default.
	}
}
