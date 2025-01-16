package com.serliunx.ddns.support.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 指令调度器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/15
 */
public final class CommandDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(CommandDispatcher.class);
	private static final CommandDispatcher INSTANCE = new CommandDispatcher();

	// private-ctor
	private CommandDispatcher() {}

	/**
	 * 最顶层指令缓存
	 */
	private final Map<String, Command> commands = new LinkedHashMap<>(128);

	/**
	 * 获取所有已注册的指令
	 *
	 * @return	已注册的指令
	 */
	public Map<String, Command> getCommands() {
		return commands;
	}

	/**
	 * 指令注册
	 * @param command	指令
	 */
	public void register(Command command) {
		commands.put(command.getName(), command);
	}

	/**
	 * 指令反注册
	 * @param command	指令
	 */
	public void unregister(Command command) {
		commands.remove(command.getName());
	}

	/**
	 * 处理输入的指令
	 * @param input	指令
	 */
	public void onCommand(String input) {
		if (input == null ||
				input.isEmpty()) {
			return;
		}

		String[] args = input.split(" ");
		String cmd = args[0];

		Command command = commands.get(cmd);
		if (command == null) {
			logger.warn("未知指令: {}, 请输入 help 查看帮助!", cmd);
			return;
		}
		if (!command.onCommand(splitArgs(args))) {
			logger.error("指令执行出现了错误: {}", Arrays.toString(args));
		}
	}

	/**
	 * 分割指令参数
	 * <li> cmd x1 x2  => x1 x2
	 *
	 * @param args	参数
	 * @return	去除指令本身的参数部分
	 */
	public static String[] splitArgs(String[] args) {
		String[] newArgs = new String[args.length - 1];
		System.arraycopy(args, 1, newArgs, 0, args.length - 1);
		return newArgs;
	}

	/**
	 * 检查是否存在参数
	 *
	 * @param args	参数
	 * @return	参数长度大于0返回真, 否则返回假
	 */
	public static boolean hasArgs(String[] args) {
		return args.length > 0;
	}

	/**
	 * 获取实例
	 */
	public static CommandDispatcher getInstance() {
		return INSTANCE;
	}
}
