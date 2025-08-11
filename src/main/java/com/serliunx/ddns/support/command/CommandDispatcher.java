package com.serliunx.ddns.support.command;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.serliunx.ddns.support.ConsoleStyleHelper.coloredPrintf;

/**
 * 指令调度器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.4
 * @since 2025/1/15
 */
public final class CommandDispatcher {

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
	 *
	 * @param command	指令
	 */
	public synchronized void register(Command command) {
		commands.put(command.getName(), command);
	}

	/**
	 * 指令反注册
	 *
	 * @param command	指令
	 */
	public synchronized void unregister(Command command) {
		commands.remove(command.getName());
	}

	/**
	 * 处理输入的指令
	 *
	 * @param input	指令
	 */
	public void onCommand(String input) {
		if (input == null || input.isEmpty())
			return;

		String[] args = input.split(" ");
		String cmd = args[0];

		Command command = commands.get(cmd);
		if (command == null) {
			System.out.println();
			coloredPrintf("&1未知指令&r: &2%s&r, &1请输入 &3help&r &1查看帮助!", cmd);
			System.out.println();
			return;
		}
		if (!command.onCommand(splitArgs(args)))
			coloredPrintf("&1指令执行出现了错误:&r &5%s", Arrays.toString(args));
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
	 * 获取实例
	 */
	public static CommandDispatcher getInstance() {
		return INSTANCE;
	}
}
